package com.zombiedefense.mercenaries.game.entities

import android.graphics.Canvas
import android.graphics.Paint
import com.zombiedefense.mercenaries.game.GameEngine
import com.zombiedefense.mercenaries.game.ZombieType

class Zombie(
    x: Float,
    y: Float,
    val type: ZombieType,
    healthMultiplier: Float,
) : Entity(x, y, type.width, type.height) {

    override val maxHealth: Float = type.baseHealth * healthMultiplier
    override var health: Float = maxHealth

    private var attackCooldownRemaining = 0f
    private val bodyPaint = Paint().apply { color = type.bodyColor }
    private val eyePaint = Paint().apply { color = 0xFFD32F2F.toInt() }
    private val barPaint = Paint()
    private val barBgPaint = Paint()

    override fun update(dt: Float, engine: GameEngine) {
        if (!isAlive) return
        attackCooldownRemaining = (attackCooldownRemaining - dt).coerceAtLeast(0f)

        val target = engine.findNearestTargetForAttacker(this)
        if (target != null && distanceTo(target) <= type.attackRange) {
            if (attackCooldownRemaining <= 0f) {
                target.health = (target.health - type.damage).coerceAtLeast(0f)
                attackCooldownRemaining = type.attackCooldown
            }
        } else {
            x += type.moveSpeed * dt
        }
    }

    override fun render(canvas: Canvas) {
        canvas.drawRoundRect(x, y, x + width, y + height, 10f, 10f, bodyPaint)
        canvas.drawCircle(x + width * 0.7f, y + height * 0.15f, 4f, eyePaint)
        renderHealthBar(canvas, barPaint, barBgPaint)
    }
}
