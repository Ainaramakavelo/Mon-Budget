package com.zombiedefense.mercenaries.game.entities

import android.graphics.Canvas
import android.graphics.Paint
import com.zombiedefense.mercenaries.game.CharacterType
import com.zombiedefense.mercenaries.game.GameEngine

class Mercenary(
    x: Float,
    y: Float,
    val type: CharacterType,
    val isPlayerControlled: Boolean,
) : Entity(x, y, type.width, type.height) {

    override val maxHealth: Float = type.maxHealth
    override var health: Float = maxHealth

    /** -1 = gauche, 0 = immobile, 1 = droite. Écrit par le thread UI (boutons), lu par le thread jeu. */
    @Volatile var moveDirection: Float = 0f
    @Volatile var attackRequested: Boolean = false
    @Volatile var jumpRequested: Boolean = false

    private var velocityY: Float = 0f
    private var isOnGround: Boolean = true
    private var attackCooldownRemaining = 0f
    private val bodyPaint = Paint().apply { color = type.bodyColor }
    private val headPaint = Paint().apply { color = 0xFFE0B18C.toInt() }
    private val crossPaint = Paint().apply { color = 0xFFFFFFFF.toInt() }
    private val barPaint = Paint()
    private val barBgPaint = Paint()

    override fun update(dt: Float, engine: GameEngine) {
        if (!isAlive) return
        attackCooldownRemaining = (attackCooldownRemaining - dt).coerceAtLeast(0f)

        if (isPlayerControlled) {
            x += moveDirection * type.moveSpeed * dt
            if (jumpRequested && isOnGround) {
                velocityY = -engine.jumpImpulse
                isOnGround = false
            }
            jumpRequested = false
            velocityY += engine.gravity * dt
            y += velocityY * dt
            val groundedY = engine.groundY - height
            if (y >= groundedY) {
                y = groundedY
                velocityY = 0f
                isOnGround = true
            }
        } else {
            val target: Entity? = if (type.isHealer) {
                engine.findNearestWoundedAlly(this)
            } else {
                engine.findNearestEnemyFrom(centerX())
            }
            if (target != null && distanceTo(target) > type.attackRange * 0.8f) {
                x += (if (target.centerX() > centerX()) 1f else -1f) * type.moveSpeed * dt
            }
        }
        x = x.coerceIn(0f, engine.worldWidth - width)

        val shouldAct = if (isPlayerControlled) attackRequested else true
        if (shouldAct && attackCooldownRemaining <= 0f) {
            if (type.isHealer) {
                val ally = engine.findNearestWoundedAlly(this)
                if (ally != null && distanceTo(ally) <= type.attackRange) {
                    ally.health = (ally.health + type.healAmount).coerceAtMost(ally.maxHealth)
                    attackCooldownRemaining = type.attackCooldown
                }
            } else {
                val target = engine.findNearestEnemyFrom(centerX())
                if (target != null && distanceTo(target) <= type.attackRange) {
                    performAttack(target, engine)
                    attackCooldownRemaining = type.attackCooldown
                }
            }
        }
        if (isPlayerControlled) attackRequested = false
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
                ownerColor = type.bodyColor,
                splashRadius = type.splashRadius,
            )
        } else {
            target.takeDamage(type.damage)
        }
    }

    override fun render(canvas: Canvas) {
        canvas.drawRoundRect(x, y + height * 0.25f, x + width, y + height, 8f, 8f, bodyPaint)
        canvas.drawCircle(x + width / 2f, y + height * 0.15f, width * 0.28f, headPaint)
        if (type.isHealer) {
            val cx = x + width / 2f
            val cy = y + height * 0.55f
            canvas.drawRect(cx - 10f, cy - 3f, cx + 10f, cy + 3f, crossPaint)
            canvas.drawRect(cx - 3f, cy - 10f, cx + 3f, cy + 10f, crossPaint)
        }
        renderHealthBar(canvas, barPaint, barBgPaint)
    }
}
