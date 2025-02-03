package com.example.bazaaro.data.model

enum class Language(
    val code: String,
    val title: String,
) {
    ENGLISH(
        code = "en",
        title = "English (US)",
    ),
    PARSI(
        code = "fa",
        title = "پارسی",
    );


    companion object {
        fun fromCode(code: String): Language? {
            return entries.find { it.code == code }
        }
    }
}
