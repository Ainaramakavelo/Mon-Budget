package com.zombiedefense.mercenaries.game

/**
 * Un type de mercenaire = une fiche de stats + une couleur (l'art est un placeholder en formes
 * géométriques pour l'instant). Point d'extension : ajouter un type "dizaines de personnages" plus
 * tard revient à ajouter une entrée ici, sans toucher au moteur de jeu.
 */
data class CharacterType(
    val id: String,
    val displayName: String,
    val bodyColor: Int,
    val maxHealth: Float,
    val damage: Float,
    val attackRange: Float,
    val attackCooldown: Float,
    val moveSpeed: Float,
    val isRanged: Boolean,
    val projectileSpeed: Float = 0f,
    val width: Float = 46f,
    val height: Float = 84f,
)

object CharacterCatalog {
    val SOLDIER = CharacterType(
        id = "soldier",
        displayName = "Soldat",
        bodyColor = 0xFF2E5AAC.toInt(),
        maxHealth = 160f,
        damage = 22f,
        attackRange = 60f,
        attackCooldown = 0.6f,
        moveSpeed = 220f,
        isRanged = false,
    )

    val MAGE = CharacterType(
        id = "mage",
        displayName = "Mage",
        bodyColor = 0xFF7B2FBF.toInt(),
        maxHealth = 90f,
        damage = 30f,
        attackRange = 320f,
        attackCooldown = 1.1f,
        moveSpeed = 170f,
        isRanged = true,
        projectileSpeed = 480f,
    )

    val ALL = listOf(SOLDIER, MAGE)
}
