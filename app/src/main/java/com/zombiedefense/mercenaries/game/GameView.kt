package com.zombiedefense.mercenaries.game

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Vue de rendu : une SurfaceView dessinée depuis un thread dédié (le pattern classique pour un
 * jeu 2D Android, plus fluide qu'une recomposition Compose à chaque frame).
 */
class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    val engine = GameEngine()
    private var gameThread: GameThread? = null

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        engine.setWorldSize(width.toFloat(), height.toFloat())
        gameThread = GameThread(holder, engine).apply {
            running = true
            start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
        engine.setWorldSize(w.toFloat(), h.toFloat())
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameThread?.let {
            it.running = false
            it.join()
        }
        gameThread = null
    }
}

private class GameThread(
    private val holder: SurfaceHolder,
    private val engine: GameEngine,
) : Thread("GameThread") {

    @Volatile var running = false

    override fun run() {
        var lastTimeNanos = System.nanoTime()
        while (running) {
            val nowNanos = System.nanoTime()
            val dt = ((nowNanos - lastTimeNanos) / 1_000_000_000f).coerceIn(0f, 0.05f)
            lastTimeNanos = nowNanos

            engine.update(dt)

            val canvas = try {
                holder.lockCanvas()
            } catch (e: IllegalArgumentException) {
                null
            }
            if (canvas != null) {
                try {
                    engine.render(canvas)
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }

            val frameMillis = (System.nanoTime() - nowNanos) / 1_000_000
            val sleepMillis = 16L - frameMillis
            if (sleepMillis > 0) {
                sleep(sleepMillis)
            }
        }
    }
}
