package com.zombiedefense.mercenaries.game

import android.graphics.Canvas
import android.graphics.Paint
import com.zombiedefense.mercenaries.game.entities.Entity
import com.zombiedefense.mercenaries.game.entities.Mercenary
import com.zombiedefense.mercenaries.game.entities.Projectile
import com.zombiedefense.mercenaries.game.entities.Village
import com.zombiedefense.mercenaries.game.entities.Zombie
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Contient toute la logique de simulation (physique simple, IA, vagues de zombies) et le rendu.
 * Ne dépend d'aucune vue Android précise : GameView se contente d'appeler update()/render().
 */
class GameEngine {

    var worldWidth: Float = 1080f
        private set
    var worldHeight: Float = 600f
        private set
    var groundY: Float = 500f
        private set

    val gravity: Float = 1800f
    val jumpImpulse: Float = 750f

    lateinit var village: Village
        private set
    val mercenaries = mutableListOf<Mercenary>()
    val zombies = mutableListOf<Zombie>()
    private val projectiles = mutableListOf<Projectile>()

    private val uiStateFlow = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = uiStateFlow

    private var elapsedSeconds = 0f
    private var spawnTimer = 0f
    private var spawnInterval = 3.0f
    private var zombiesKilled = 0
    private var isGameOver = false
    private var initialized = false

    private val backgroundPaint = Paint().apply { color = 0xFF0D1B0D.toInt() }
    private val groundPaint = Paint().apply { color = 0xFF3E2723.toInt() }

    fun setWorldSize(width: Float, height: Float) {
        if (width <= 0f || height <= 0f) return
        worldWidth = width
        worldHeight = height
        groundY = height * 0.82f
        if (!initialized) {
            reset()
            initialized = true
        }
    }

    fun reset() {
        elapsedSeconds = 0f
        spawnTimer = 0f
        spawnInterval = 3.0f
        zombiesKilled = 0
        isGameOver = false

        val villageWidth = 140f
        val villageHeight = 160f
        village = Village(
            x = worldWidth - villageWidth - 20f,
            y = groundY - villageHeight,
            width = villageWidth,
            height = villageHeight,
            maxHealth = 400f,
        )

        mercenaries.clear()
        zombies.clear()
        projectiles.clear()

        CharacterCatalog.ALL.forEachIndexed { index, type ->
            val startX = village.x - 140f - index * 80f
            mercenaries.add(
                Mercenary(
                    x = startX,
                    y = groundY - type.height,
                    type = type,
                    isPlayerControlled = type.id == CharacterCatalog.SOLDIER.id,
                )
            )
        }

        publishUiState()
    }

    fun setPlayerMoveDirection(direction: Float) {
        mercenaries.firstOrNull { it.isPlayerControlled }?.moveDirection = direction
    }

    fun requestPlayerJump() {
        mercenaries.firstOrNull { it.isPlayerControlled }?.jumpRequested = true
    }

    fun requestPlayerAttack() {
        mercenaries.firstOrNull { it.isPlayerControlled }?.attackRequested = true
    }

    fun spawnProjectile(
        x: Float,
        y: Float,
        speed: Float,
        movingRight: Boolean,
        damage: Float,
        ownerColor: Int,
        splashRadius: Float = 0f,
    ) {
        projectiles.add(Projectile(x, y, speed, movingRight, damage, ownerColor, splashRadius))
    }

    fun findNearestZombieFrom(x: Float): Zombie? =
        zombies.filter { it.isAlive }.minByOrNull { kotlin.math.abs(it.centerX() - x) }

    fun findNearestWoundedAlly(from: Mercenary): Mercenary? =
        mercenaries
            .filter { it.isAlive && it !== from && it.health < it.maxHealth }
            .minByOrNull { it.distanceTo(from) }

    /**
     * Un zombie s'en prend à la cible la plus proche (mercenaire croisé sur le chemin, ou le
     * village), sauf si un tank à fort "aggroBonus" attire son attention de plus loin.
     */
    fun findNearestTargetForZombie(zombie: Zombie): Entity? {
        val candidates: List<Entity> = mercenaries.filter { it.isAlive } + village
        return candidates.minByOrNull { candidate ->
            val aggroBonus = (candidate as? Mercenary)?.type?.aggroBonus ?: 0f
            (zombie.distanceTo(candidate) - aggroBonus).coerceAtLeast(0f)
        }
    }

    fun update(dt: Float) {
        if (!initialized || isGameOver) return
        elapsedSeconds += dt

        spawnTimer += dt
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0f
            spawnZombieWave()
            spawnInterval = (spawnInterval * 0.96f).coerceAtLeast(0.6f)
        }

        mercenaries.forEach { it.update(dt, this) }
        zombies.forEach { it.update(dt, this) }
        projectiles.forEach { it.update(dt, this) }

        zombiesKilled += zombies.count { !it.isAlive }
        zombies.removeAll { !it.isAlive }
        projectiles.removeAll { !it.isAlive }

        village.update(dt, this)

        if (village.health <= 0f) {
            isGameOver = true
        }

        publishUiState()
    }

    private fun spawnZombieWave() {
        val difficulty = 1f + elapsedSeconds / 45f
        val type = if (Random.nextFloat() < (0.25f + elapsedSeconds / 200f).coerceAtMost(0.6f)) {
            ZombieCatalog.RUNNER
        } else {
            ZombieCatalog.WALKER
        }
        val zombie = Zombie(
            x = -type.width - 5f,
            y = groundY - type.height,
            type = type,
            healthMultiplier = difficulty,
        )
        zombies.add(zombie)
    }

    private fun publishUiState() {
        uiStateFlow.value = GameUiState(
            villageHealth = if (::village.isInitialized) village.health else 0f,
            villageMaxHealth = if (::village.isInitialized) village.maxHealth else 1f,
            survivedSeconds = elapsedSeconds.toInt(),
            zombiesKilled = zombiesKilled,
            isGameOver = isGameOver,
        )
    }

    fun render(canvas: Canvas) {
        canvas.drawRect(0f, 0f, worldWidth, worldHeight, backgroundPaint)
        canvas.drawRect(0f, groundY, worldWidth, worldHeight, groundPaint)
        if (::village.isInitialized) village.render(canvas)
        mercenaries.forEach { it.render(canvas) }
        zombies.forEach { it.render(canvas) }
        projectiles.forEach { it.render(canvas) }
    }
}
