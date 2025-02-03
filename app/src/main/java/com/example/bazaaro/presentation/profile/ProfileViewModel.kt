package com.example.bazaaro.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bazaaro.data.repository.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val languageRepository: LanguageRepository
) : ViewModel() {

    private val _languageCode = MutableStateFlow(languageRepository.getSavedLanguage())

    fun changeLanguage(languageCode: String) {
        viewModelScope.launch {
            languageRepository.updateLanguage(languageCode)
            _languageCode.value = languageCode
        }
    }
}
