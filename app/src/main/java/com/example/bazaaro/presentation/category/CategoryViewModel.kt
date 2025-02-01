package com.example.bazaaro.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazaaro.data.model.FavoriteEntity
import com.example.bazaaro.data.model.Product
import com.example.bazaaro.data.repository.CategoriesRepository
import com.example.bazaaro.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _categoriesState: MutableStateFlow<CategoryState> =
        MutableStateFlow(CategoryState.Loading)
    var categoriesState: StateFlow<CategoryState> = _categoriesState.asStateFlow()

    val selectedCategory = MutableStateFlow("")

    private val _productsState = MutableStateFlow<ProductState>(ProductState.Loading)
    val productsState: StateFlow<ProductState> = _productsState.asStateFlow()

    init {
        getAllCategories()
    }

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            _categoriesState.emit(CategoryState.Loading)
            categoriesRepository.getAllCategories().onSuccess { categories ->
                _categoriesState.emit(CategoryState.Success(categoryList = categories))

                changeSelectedCategory(categories.firstOrNull() ?: "")
            }.onFailure { exception ->
                _categoriesState.emit((CategoryState.Error(errorMessage = exception.message)))
            }
        }
    }

    fun getProductsByCategory(categoryName: String = selectedCategory.value) {
        viewModelScope.launch(Dispatchers.IO) {
            _productsState.emit(ProductState.Loading)
            categoriesRepository.getProductsByCategory(categoryName).onSuccess { products ->
                _productsState.emit(ProductState.Success(products ?: emptyList()))
            }.onFailure { exception ->
                _productsState.emit(ProductState.Error(exception.message))
            }
        }
    }

    fun changeSelectedCategory(newCategory: String) {
        selectedCategory.value = newCategory
        getProductsByCategory()
    }

    fun toggleFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(favoriteEntity)
        }
    }

    fun isFavorite(id: Int): Flow<Boolean> {
        return favoriteRepository.isFavorite(id)
    }
}

sealed interface ProductState {
    data object Loading : ProductState
    data class Success(val products: List<Product>) : ProductState
    data class Error(val errorMessage: String?) : ProductState
}