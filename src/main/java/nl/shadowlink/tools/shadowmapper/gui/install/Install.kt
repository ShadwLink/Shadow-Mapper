package nl.shadowlink.tools.shadowmapper.gui.install

import nl.shadowlink.tools.shadowlib.utils.GameType
import kotlin.io.path.Path
import kotlin.io.path.exists

data class Install(
    val id: String,
    val name: String,
    val path: String,
    val gameType: GameType,
) {

    val isValid: Boolean
        get() = Path(path).exists()
}
