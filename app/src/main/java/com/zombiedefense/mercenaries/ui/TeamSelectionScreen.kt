package com.zombiedefense.mercenaries.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.zombiedefense.mercenaries.game.CharacterCatalog
import com.zombiedefense.mercenaries.game.CharacterType
import com.zombiedefense.mercenaries.game.TeamConfig

@Composable
fun TeamSelectionScreen(onStart: (TeamConfig) -> Unit) {
    var playerCharacterId by remember { mutableStateOf(CharacterCatalog.SOLDIER.id) }
    val includedIds = remember {
        mutableStateOf(CharacterCatalog.ALL.map { it.id }.toSet())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B0D))
            .padding(16.dp),
    ) {
        Text(
            text = "Choisis ton équipe",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Sélectionne le mercenaire que tu joues directement, et coche ceux qui t'accompagnent au combat.",
            color = Color(0xFFBBBBBB),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CharacterCatalog.ALL.forEach { type ->
                CharacterCard(
                    type = type,
                    isPlayable = playerCharacterId == type.id,
                    isIncluded = includedIds.value.contains(type.id),
                    onSelectPlayable = {
                        playerCharacterId = type.id
                        includedIds.value = includedIds.value + type.id
                    },
                    onToggleIncluded = { checked ->
                        includedIds.value = if (checked) {
                            includedIds.value + type.id
                        } else {
                            includedIds.value - type.id
                        }
                    },
                )
            }
        }

        Button(
            onClick = {
                val roster = CharacterCatalog.ALL.filter { includedIds.value.contains(it.id) }
                onStart(TeamConfig(roster = roster, playerCharacterId = playerCharacterId))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        ) {
            Text("Commencer la partie")
        }
    }
}

@Composable
private fun CharacterCard(
    type: CharacterType,
    isPlayable: Boolean,
    isIncluded: Boolean,
    onSelectPlayable: () -> Unit,
    onToggleIncluded: (Boolean) -> Unit,
) {
    val borderColor = if (isPlayable) Color(0xFFFFC107) else Color(0x33FFFFFF)
    Column(
        modifier = Modifier
            .width(150.dp)
            .fillMaxHeight()
            .background(Color(0xFF1B2A1B), RoundedCornerShape(12.dp))
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(type.bodyColor), CircleShape),
        )
        Text(
            text = type.displayName,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 6.dp),
        )
        Text(text = roleLabel(type), color = Color(0xFFBBBBBB), style = MaterialTheme.typography.bodySmall)
        Text(text = "PV : ${type.maxHealth.toInt()}", color = Color(0xFFBBBBBB), style = MaterialTheme.typography.bodySmall)
        if (type.isHealer) {
            Text(text = "Soin : ${type.healAmount.toInt()}", color = Color(0xFFBBBBBB), style = MaterialTheme.typography.bodySmall)
        } else {
            Text(text = "Dégâts : ${type.damage.toInt()}", color = Color(0xFFBBBBBB), style = MaterialTheme.typography.bodySmall)
        }
        Text(text = "Portée : ${type.attackRange.toInt()}", color = Color(0xFFBBBBBB), style = MaterialTheme.typography.bodySmall)

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 6.dp)) {
            RadioButton(selected = isPlayable, onClick = onSelectPlayable)
            Text("Jouable", color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isIncluded, onCheckedChange = onToggleIncluded, enabled = !isPlayable)
            Text("Dans l'équipe", color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun roleLabel(type: CharacterType): String = when {
    type.isHealer -> "Soigneur"
    type.aggroBonus > 0f -> "Tank"
    type.isRanged -> "Distance"
    else -> "Corps-à-corps"
}
