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
    /** > 0 pour un tir d'artillerie : inflige des dégâts à tous les zombies dans ce rayon d'impact. */
    private val splashRadius: Float = 0f,
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
        val hitZombie = engine.zombies.firstOrNull { it.isAlive && bounds().intersect(it.bounds()) }
        if (hitZombie != null) {
            hasHit = true
            if (splashRadius > 0f) {
                val impactX = hitZombie.centerX()
                engine.zombies.forEach { zombie ->
                    if (zombie.isAlive && kotlin.math.abs(zombie.centerX() - impactX) <= splashRadius) {
                        zombie.takeDamage(damage)
                    }
                }
            } else {
                hitZombie.takeDamage(damage)
            }
            health = 0f
        }
    }

    override fun render(canvas: Canvas) {
        canvas.drawCircle(x + width / 2f, y + height / 2f, width / 2f, paint)
    }
}
