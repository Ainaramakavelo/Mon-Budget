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
    /** Rayon de dégâts de zone au point d'impact (0 = dégâts sur cible unique seulement). */
    val splashRadius: Float = 0f,
    /** Un soigneur ne combat pas les zombies : il rend `healAmount` de vie à l'allié le plus proche. */
    val isHealer: Boolean = false,
    val healAmount: Float = 0f,
    /** Plus c'est élevé, plus les zombies préfèrent attaquer ce mercenaire (rôle de tank). */
    val aggroBonus: Float = 0f,
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

    val ARCHER = CharacterType(
        id = "archer",
        displayName = "Archer",
        bodyColor = 0xFF2E7D32.toInt(),
        maxHealth = 100f,
        damage = 16f,
        attackRange = 260f,
        attackCooldown = 0.5f,
        moveSpeed = 200f,
        isRanged = true,
        projectileSpeed = 560f,
        width = 44f,
        height = 82f,
    )

    val HEALER = CharacterType(
        id = "healer",
        displayName = "Soigneur",
        bodyColor = 0xFF80DEEA.toInt(),
        maxHealth = 80f,
        damage = 0f,
        attackRange = 140f,
        attackCooldown = 1.5f,
        moveSpeed = 190f,
        isRanged = false,
        isHealer = true,
        healAmount = 25f,
    )

    val ARTILLERIST = CharacterType(
        id = "artillerist",
        displayName = "Artilleur",
        bodyColor = 0xFFBF360C.toInt(),
        maxHealth = 110f,
        damage = 45f,
        attackRange = 380f,
        attackCooldown = 2.2f,
        moveSpeed = 150f,
        isRanged = true,
        projectileSpeed = 380f,
        splashRadius = 90f,
        width = 48f,
        height = 86f,
    )

    val TANK = CharacterType(
        id = "tank",
        displayName = "Tank",
        bodyColor = 0xFF455A64.toInt(),
        maxHealth = 320f,
        damage = 18f,
        attackRange = 65f,
        attackCooldown = 0.8f,
        moveSpeed = 140f,
        isRanged = false,
        aggroBonus = 220f,
        width = 54f,
        height = 92f,
    )

    val ALL = listOf(SOLDIER, TANK, ARCHER, MAGE, ARTILLERIST, HEALER)
}
