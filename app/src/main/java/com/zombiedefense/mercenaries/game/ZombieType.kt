package com.zombiedefense.mercenaries.game

data class ZombieType(
    val id: String,
    val bodyColor: Int,
    val baseHealth: Float,
    val damage: Float,
    val attackRange: Float,
    val attackCooldown: Float,
    val moveSpeed: Float,
    val width: Float = 44f,
    val height: Float = 80f,
)

object ZombieCatalog {
    val WALKER = ZombieType(
        id = "walker",
        bodyColor = 0xFF4C7A3F.toInt(),
        baseHealth = 60f,
        damage = 10f,
        attackRange = 50f,
        attackCooldown = 1.0f,
        moveSpeed = 60f,
    )

    val RUNNER = ZombieType(
        id = "runner",
        bodyColor = 0xFF7A9E3F.toInt(),
        baseHealth = 35f,
        damage = 8f,
        attackRange = 45f,
        attackCooldown = 0.8f,
        moveSpeed = 130f,
        width = 40f,
        height = 76f,
    )

    val ALL = listOf(WALKER, RUNNER)
}
