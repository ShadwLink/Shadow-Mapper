package nl.shadowlink.tools.shadowmapper.gui.install

import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.File

data class Install(
    val id: String,
    val name: String,
    val path: String,
    val gameType: GameType,
) {

    val isValid: Boolean
        get() = File(path).exists()
}
