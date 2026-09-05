package com.zombiedefense.mercenaries.game.entities

import android.graphics.Canvas
import android.graphics.RectF
import com.zombiedefense.mercenaries.game.GameEngine

abstract class Entity(
    var x: Float,
    var y: Float,
    val width: Float,
    val height: Float,
) {
    abstract var health: Float
    abstract val maxHealth: Float

    val isAlive: Boolean get() = health > 0f

    fun bounds(): RectF = RectF(x, y, x + width, y + height)

    fun centerX(): Float = x + width / 2f

    fun distanceTo(other: Entity): Float = kotlin.math.abs(centerX() - other.centerX())

    abstract fun update(dt: Float, engine: GameEngine)
    abstract fun render(canvas: Canvas)

    protected fun renderHealthBar(canvas: Canvas, barPaint: android.graphics.Paint, bgPaint: android.graphics.Paint) {
        val barWidth = width
        val barHeight = 6f
        val barY = y - 14f
        bgPaint.color = 0x66000000
        canvas.drawRect(x, barY, x + barWidth, barY + barHeight, bgPaint)
        val ratio = (health / maxHealth).coerceIn(0f, 1f)
        barPaint.color = if (ratio > 0.5f) 0xFF4CAF50.toInt() else if (ratio > 0.2f) 0xFFFFC107.toInt() else 0xFFF44336.toInt()
        canvas.drawRect(x, barY, x + barWidth * ratio, barY + barHeight, barPaint)
    }
}
