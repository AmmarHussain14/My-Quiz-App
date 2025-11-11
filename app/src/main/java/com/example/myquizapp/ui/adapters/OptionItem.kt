package com.example.myquizapp.ui.adapters

data class OptionItem(
    val text: String,
    val letter: String,
    val isSelected: Boolean = false,
    val isCorrect: Boolean = false,
    val isWrong: Boolean = false,
    val index: Int
)
