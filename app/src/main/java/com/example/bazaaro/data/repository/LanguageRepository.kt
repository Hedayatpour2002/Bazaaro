package com.example.bazaaro.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val PREFS_NAME = "LANGUAGE_PREFS"
private const val LANGUAGE_KEY = "LANGUAGE"

class LanguageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getSavedLanguage(defaultLang: String = "en"): String {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(LANGUAGE_KEY, defaultLang) ?: defaultLang
    }

    fun updateLanguage(languageCode: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
        saveLanguage(languageCode)
    }

    private fun saveLanguage(languageCode: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().apply {
            putString(LANGUAGE_KEY, languageCode)
            apply()
        }
    }
}

