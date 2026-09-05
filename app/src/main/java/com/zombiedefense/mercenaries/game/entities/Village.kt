package com.zombiedefense.mercenaries.game.entities

import android.graphics.Canvas
import android.graphics.Paint
import com.zombiedefense.mercenaries.game.GameEngine

class Village(x: Float, y: Float, width: Float, height: Float, override val maxHealth: Float) :
    Entity(x, y, width, height) {

    override var health: Float = maxHealth

    private val wallPaint = Paint().apply { color = 0xFF8D6E63.toInt() }
    private val roofPaint = Paint().apply { color = 0xFFB71C1C.toInt() }
    private val barPaint = Paint()
    private val barBgPaint = Paint()

    override fun update(dt: Float, engine: GameEngine) {
        // Le village est statique : il ne fait que subir les dégâts des zombies qui l'atteignent.
    }

    override fun render(canvas: Canvas) {
        val wallTop = y + height * 0.35f
        canvas.drawRect(x, wallTop, x + width, y + height, wallPaint)
        val roofPath = android.graphics.Path().apply {
            moveTo(x - 10f, wallTop)
            lineTo(x + width / 2f, y)
            lineTo(x + width + 10f, wallTop)
            close()
        }
        canvas.drawPath(roofPath, roofPaint)
        renderHealthBar(canvas, barPaint, barBgPaint)
    }
}
