package com.zombiedefense.mercenaries.game

data class GameUiState(
    val villageHealth: Float = 100f,
    val villageMaxHealth: Float = 100f,
    val survivedSeconds: Int = 0,
    val zombiesKilled: Int = 0,
    val bossesDefeated: Int = 0,
    /** Non nul quand un boss est actuellement en vie sur le champ de bataille. */
    val bossName: String? = null,
    val bossHealth: Float = 0f,
    val bossMaxHealth: Float = 1f,
    /** Bannière temporaire affichée à l'apparition d'un boss. */
    val announcement: String? = null,
    val isGameOver: Boolean = false,
)
