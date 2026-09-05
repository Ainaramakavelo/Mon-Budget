package com.zombiedefense.mercenaries.game

/** L'équipe choisie sur l'écran de sélection : qui part au combat, et lequel est jouable. */
data class TeamConfig(
    val roster: List<CharacterType>,
    val playerCharacterId: String,
)
