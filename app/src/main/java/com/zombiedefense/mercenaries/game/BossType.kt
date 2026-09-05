package com.zombiedefense.mercenaries.game

/**
 * Un boss = un ennemi surpuissant lié à la malédiction du cimetière des Cendres. Ajouter un
 * nouveau boss revient à ajouter une entrée ici et à l'inclure dans ALL.
 */
data class BossType(
    val id: String,
    val displayName: String,
    /** Phrase affichée à son apparition. */
    val announcement: String,
    /** Courte phrase de contexte narratif. */
    val lore: String,
    val bodyColor: Int,
    val eyeColor: Int,
    val baseHealth: Float,
    val damage: Float,
    val attackRange: Float,
    val attackCooldown: Float,
    val moveSpeed: Float,
    val isRanged: Boolean,
    val projectileSpeed: Float = 0f,
    val splashRadius: Float = 0f,
    /** S'il peut relever des sbires : combien, et tous les combien de secondes. */
    val canSummon: Boolean = false,
    val summonCooldown: Float = 0f,
    val summonCount: Int = 0,
    val width: Float = 70f,
    val height: Float = 120f,
)

object BossCatalog {
    val FOSSOYEUR = BossType(
        id = "fossoyeur",
        displayName = "Le Fossoyeur Maudit",
        announcement = "Le Fossoyeur Maudit surgit du cimetière des Cendres !",
        lore = "Il n'a jamais fini d'enterrer les vivants. Maintenant, il déterre les morts.",
        bodyColor = 0xFF3E2A1F.toInt(),
        eyeColor = 0xFFFF6D00.toInt(),
        baseHealth = 900f,
        damage = 40f,
        attackRange = 75f,
        attackCooldown = 1.0f,
        moveSpeed = 90f,
        isRanged = false,
    )

    val NECROMANCIEN = BossType(
        id = "necromancien",
        displayName = "Le Nécromancien des Cendres",
        announcement = "Le Nécromancien des Cendres apparaît — la source de la malédiction elle-même !",
        lore = "La malédiction a un visage. Tant qu'il respire, les morts ne resteront jamais couchés.",
        bodyColor = 0xFF4A148C.toInt(),
        eyeColor = 0xFF00E5FF.toInt(),
        baseHealth = 700f,
        damage = 25f,
        attackRange = 340f,
        attackCooldown = 1.8f,
        moveSpeed = 70f,
        isRanged = true,
        projectileSpeed = 300f,
        splashRadius = 50f,
        canSummon = true,
        summonCooldown = 8f,
        summonCount = 2,
        width = 60f,
        height = 100f,
    )

    val ALL = listOf(FOSSOYEUR, NECROMANCIEN)

    /** Les boss alternent et deviennent plus coriaces à chaque nouvelle apparition. */
    fun forEncounter(index: Int): BossType = ALL[index % ALL.size]
}
