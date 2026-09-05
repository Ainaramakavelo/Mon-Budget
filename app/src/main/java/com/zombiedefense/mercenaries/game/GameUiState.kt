package com.zombiedefense.mercenaries.game

data class GameUiState(
    val villageHealth: Float = 100f,
    val villageMaxHealth: Float = 100f,
    val survivedSeconds: Int = 0,
    val zombiesKilled: Int = 0,
    val isGameOver: Boolean = false,
)
