package com.zombiedefense.mercenaries.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zombiedefense.mercenaries.game.GameView
import com.zombiedefense.mercenaries.game.TeamConfig

@Composable
fun GameScreen(teamConfig: TeamConfig, onBackToSelection: () -> Unit) {
    val context = LocalContext.current
    val gameView = remember(teamConfig) {
        GameView(context).apply { engine.configureTeam(teamConfig) }
    }
    val uiState by gameView.engine.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { gameView }, modifier = Modifier.fillMaxSize())

        // HUD : vie du village, temps survécu, zombies éliminés.
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Text(
                text = "Village",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
            )
            LinearProgressIndicator(
                progress = { (uiState.villageHealth / uiState.villageMaxHealth).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Survécu : ${uiState.survivedSeconds}s", color = Color.White)
                Text(text = "Zombies éliminés : ${uiState.zombiesKilled}", color = Color.White)
            }
        }

        // Contrôles : déplacement à gauche, saut + attaque à droite.
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HoldButton(label = "◀", onPress = { gameView.engine.setPlayerMoveDirection(-1f) }, onRelease = { gameView.engine.setPlayerMoveDirection(0f) })
            HoldButton(label = "▶", onPress = { gameView.engine.setPlayerMoveDirection(1f) }, onRelease = { gameView.engine.setPlayerMoveDirection(0f) })
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TapButton(label = "Saut", onTap = { gameView.engine.requestPlayerJump() })
            TapButton(label = "Attaque", onTap = { gameView.engine.requestPlayerAttack() })
        }

        if (uiState.isGameOver) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x99000000)),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Le village est tombé", color = Color.White, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "Survécu ${uiState.survivedSeconds}s — ${uiState.zombiesKilled} zombies éliminés",
                        color = Color.White,
                    )
                    Row(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Button(onClick = { gameView.engine.reset() }) {
                            Text("Recommencer")
                        }
                        Button(onClick = onBackToSelection) {
                            Text("Changer d'équipe")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HoldButton(label: String, onPress: () -> Unit, onRelease: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(Color(0x88FFFFFF), CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        onPress()
                        tryAwaitRelease()
                        onRelease()
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
private fun TapButton(label: String, onTap: () -> Unit) {
    Button(onClick = onTap) {
        Text(label)
    }
}
