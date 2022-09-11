package nl.shadowlink.tools.shadowmapper.gui.install

import nl.shadowlink.tools.shadowlib.utils.GameType

data class Install(
    val id: String,
    val name: String,
    val path: String,
    val gameType: GameType,
)
