package com.zombiedefense.mercenaries.game

/** Le contexte narratif du jeu — à enrichir au fur et à mesure (autres villages, boss, chapitres...). */
object Lore {
    const val VILLAGE_NAME = "Valombre"
    const val SUBTITLE = "La nuit où les morts se sont levés"

    val introParagraphs = listOf(
        "Valombre était un village paisible, niché entre les collines et l'ancien cimetière des " +
            "Cendres — jusqu'à cette nuit où les morts se sont levés sans prévenir.",
        "Une malédiction oubliée, réveillée par des fouilleurs de tombes trop curieux, a rendu les " +
            "cadavres du cimetière inarrêtables : pour chaque zombie abattu, un autre semble sortir " +
            "de terre. Personne ne sait où s'arrête la horde, ni si elle a seulement une fin.",
        "Le village garde pourtant un pont stratégique sur la rivière Grise, le seul passage avant " +
            "des kilomètres. S'il tombe, plus rien n'arrêtera les morts avant la capitale.",
        "Une compagnie de mercenaires a été engagée à prix d'or pour tenir la ligne : la Garde du " +
            "Pont. Combien de temps devront-ils résister ? Personne ne le sait — pas même eux.",
    )
}
