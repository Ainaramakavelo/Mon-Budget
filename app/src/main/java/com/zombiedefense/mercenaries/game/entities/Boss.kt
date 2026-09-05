package com.zombiedefense.mercenaries.game.entities

import android.graphics.Canvas
import android.graphics.Paint
import com.zombiedefense.mercenaries.game.BossType
import com.zombiedefense.mercenaries.game.GameEngine

class Boss(
    x: Float,
    y: Float,
    val type: BossType,
    healthMultiplier: Float,
) : Entity(x, y, type.width, type.height) {

    override val maxHealth: Float = type.baseHealth * healthMultiplier
    override var health: Float = maxHealth

    private var attackCooldownRemaining = 0f
    private var summonCooldownRemaining = type.summonCooldown * 0.5f

    private val bodyPaint = Paint().apply { color = type.bodyColor }
    private val eyePaint = Paint().apply { color = type.eyeColor }
    private val barPaint = Paint()
    private val barBgPaint = Paint()

    override fun update(dt: Float, engine: GameEngine) {
        if (!isAlive) return
        attackCooldownRemaining = (attackCooldownRemaining - dt).coerceAtLeast(0f)

        if (type.canSummon) {
            summonCooldownRemaining = (summonCooldownRemaining - dt).coerceAtLeast(0f)
            if (summonCooldownRemaining <= 0f) {
                engine.spawnZombiesNear(centerX(), type.summonCount)
                summonCooldownRemaining = type.summonCooldown
            }
        }

        val target = engine.findNearestTargetForAttacker(this)
        if (target != null && distanceTo(target) <= type.attackRange) {
            if (attackCooldownRemaining <= 0f) {
                performAttack(target, engine)
                attackCooldownRemaining = type.attackCooldown
            }
        } else {
            x += type.moveSpeed * dt
        }
    }

    private fun performAttack(target: Entity, engine: GameEngine) {
        if (type.isRanged) {
            val movingRight = target.centerX() > centerX()
            engine.spawnProjectile(
                x = if (movingRight) x + width else x,
                y = y + height * 0.3f,
                speed = type.projectileSpeed,
                movingRight = movingRight,
                damage = type.damage,
                ownerColor = type.eyeColor,
                splashRadius = type.splashRadius,
                isHostile = true,
            )
        } else {
            target.health = (target.health - type.damage).coerceAtLeast(0f)
        }
    }

    override fun render(canvas: Canvas) {
        canvas.drawRoundRect(x, y, x + width, y + height, 14f, 14f, bodyPaint)
        canvas.drawCircle(x + width * 0.3f, y + height * 0.18f, 6f, eyePaint)
        canvas.drawCircle(x + width * 0.7f, y + height * 0.18f, 6f, eyePaint)
        renderHealthBar(canvas, barPaint, barBgPaint)
    }
}
