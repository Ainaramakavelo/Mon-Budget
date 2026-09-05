package com.zombiedefense.mercenaries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.zombiedefense.mercenaries.game.TeamConfig
import com.zombiedefense.mercenaries.ui.GameScreen
import com.zombiedefense.mercenaries.ui.StoryScreen
import com.zombiedefense.mercenaries.ui.TeamSelectionScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var showStory by remember { mutableStateOf(true) }
                    var teamConfig by remember { mutableStateOf<TeamConfig?>(null) }
                    val currentConfig = teamConfig

                    when {
                        showStory -> StoryScreen(onContinue = { showStory = false })
                        currentConfig == null -> TeamSelectionScreen(
                            onStart = { teamConfig = it },
                            onViewStory = { showStory = true },
                        )
                        else -> GameScreen(
                            teamConfig = currentConfig,
                            onBackToSelection = { teamConfig = null },
                        )
                    }
                }
            }
        }
    }
}
