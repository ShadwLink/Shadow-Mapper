package nl.shadowlink.tools.shadowlib.ide

import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.BufferedWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class ItemWeapon(
    private val gameType: GameType
) : IdeItem() {

    private lateinit var line: String

    override fun read(line: String) {
        this.line = line
    }

    fun save(output: BufferedWriter) {
        try {
            output.write(line)
        } catch (ex: IOException) {
            Logger.getLogger(ItemWeapon::class.java.name).log(Level.SEVERE, null, ex)
        }
    }
}