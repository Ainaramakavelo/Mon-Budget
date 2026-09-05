package com.zombiedefense.mercenaries.game.entities

import android.graphics.Canvas
import android.graphics.Paint
import com.zombiedefense.mercenaries.game.GameEngine

class Projectile(
    x: Float,
    y: Float,
    private val speed: Float,
    private val movingRight: Boolean,
    val damage: Float,
    private val ownerColor: Int,
    /** > 0 pour un tir en zone (artillerie, sort de malédiction) : touche tout ce qui est dans ce rayon. */
    private val splashRadius: Float = 0f,
    /** false = tiré par un mercenaire (touche les zombies) ; true = tiré par un boss (touche les mercenaires). */
    private val isHostile: Boolean = false,
) : Entity(x, y, if (splashRadius > 0f) 20f else 14f, if (splashRadius > 0f) 20f else 14f) {

    override var health: Float = 1f
    override val maxHealth: Float = 1f
    var hasHit: Boolean = false
        private set

    private val paint = Paint().apply { color = ownerColor }

    override fun update(dt: Float, engine: GameEngine) {
        x += (if (movingRight) speed else -speed) * dt
        if (x < -50f || x > engine.worldWidth + 50f) {
            health = 0f
            return
        }
        val potentialTargets: List<Entity> = if (isHostile) {
            engine.mercenaries.filter { it.isAlive }
        } else {
            engine.allEnemies()
        }
        val hitTarget = potentialTargets.firstOrNull { bounds().intersect(it.bounds()) }
        if (hitTarget != null) {
            hasHit = true
            if (splashRadius > 0f) {
                val impactX = hitTarget.centerX()
                potentialTargets.forEach { target ->
                    if (kotlin.math.abs(target.centerX() - impactX) <= splashRadius) {
                        target.health = (target.health - damage).coerceAtLeast(0f)
                    }
                }
            } else {
                hitTarget.health = (hitTarget.health - damage).coerceAtLeast(0f)
            }
            health = 0f
        }
    }

    override fun render(canvas: Canvas) {
        canvas.drawCircle(x + width / 2f, y + height / 2f, width / 2f, paint)
    }
}
