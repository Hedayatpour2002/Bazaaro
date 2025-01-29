package com.example.bazaaro.presentation.category

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bazaaro.app.DETAIL_SCREEN_ROUTE
import com.example.bazaaro.app.ui.components.ErrorView
import com.example.bazaaro.app.ui.components.LoadingView
import com.example.bazaaro.app.ui.components.ProductCard

@Composable
fun CategoryScreen(
    categoryViewModel: CategoryViewModel = hiltViewModel(), navController: NavHostController
) {
    val categoriesState = categoryViewModel.categoriesState.collectAsStateWithLifecycle()
    val selectedCategory = categoryViewModel.selectedCategory.collectAsStateWithLifecycle().value

    when (val state = categoriesState.value) {
        is CategoryState.Error -> ErrorView(errorMessage = state.errorMessage, onClick = {
            categoryViewModel.getAllCategories()
        })

        CategoryState.Loading -> LoadingView()
        is CategoryState.Success -> {
            if (state.categoryList.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoriesView(
                        categoryList = state.categoryList, selectedCategory = selectedCategory
                    ) {
                        categoryViewModel.changeSelectedCategory(newCategory = it)
                    }

                    ProductsSection(viewModel = categoryViewModel) {
                        navController.navigate("$DETAIL_SCREEN_ROUTE/$it")
                    }
                }
            } else {
                EmptyView()
            }
        }
    }
}

@Composable
fun CategoriesView(
    categoryList: List<String>, selectedCategory: String, changeSelectedCategory: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categoryList) { category ->
            val isActive = category == selectedCategory
            CategoryView(title = category, isActive = isActive) {
                changeSelectedCategory(category)
            }
        }
    }
}


@Composable
fun CategoryView(
    title: String, isActive: Boolean, onClickHandler: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.85f, animationSpec = tween(300), label = "scale"
    )

    val clickScale by animateFloatAsState(
        targetValue = if (pressed) 0.90f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "clickScale"
    )


    ElevatedButton(
        onClick = onClickHandler,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) Color.Black else Color(0xFFF5F5F5),
            contentColor = if (isActive) Color.White else Color.DarkGray
        ),
        modifier = Modifier.scale(scale * clickScale)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductsSection(
    viewModel: CategoryViewModel, onClickHandler: (Int) -> Unit
) {
    val productsState = viewModel.productsState.collectAsStateWithLifecycle()

    when (val state = productsState.value) {
        is ProductState.Error -> ErrorView(errorMessage = state.errorMessage) {
            viewModel.getProductsByCategory()
        }

        ProductState.Loading -> LoadingView()
        is ProductState.Success -> PullToRefreshBox(isRefreshing = false, onRefresh = {
            viewModel.getProductsByCategory()

        }) {
            LazyColumn {
                items(state.products.chunked(2)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        it.forEach { product ->
                            ProductCard(
                                product.id,
                                product.title,
                                product.image,
                                product.rating,
                                product.price,
                                product.category,
                                onClickHandler
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EmptyView() {
    Text("Empty")
}

