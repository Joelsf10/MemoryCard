package com.uni.memorycard.ui.model

data class CardData(
    val id: Int,
    val imageRes: Int,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)