package nl.shadowlink.tools.shadowlib.ide

import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.BufferedWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class Item2DFX(private val gameType: GameType) : IdeItem() {

    lateinit var name: String
    override fun read(line: String) {
        var line = line
        line = line.replace(" ", "")
        val split = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        name = split[0]
        // TODO: Implement other params
    }

    fun save(output: BufferedWriter) {
        try {
            val line = ""
            output.write(
                """
    $line
    
    """.trimIndent()
            )
            println("Line: $line")
        } catch (ex: IOException) {
            Logger.getLogger(ItemObject::class.java.name).log(Level.SEVERE, null, ex)
        }
    }
}