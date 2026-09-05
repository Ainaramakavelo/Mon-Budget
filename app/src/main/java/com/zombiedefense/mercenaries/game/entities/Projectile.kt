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
) : Entity(x, y, 14f, 14f) {

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
        for (zombie in engine.zombies) {
            if (!zombie.isAlive) continue
            if (bounds().intersect(zombie.bounds())) {
                zombie.takeDamage(damage)
                hasHit = true
                health = 0f
                break
            }
        }
    }

    override fun render(canvas: Canvas) {
        canvas.drawCircle(x + width / 2f, y + height / 2f, width / 2f, paint)
    }
}
