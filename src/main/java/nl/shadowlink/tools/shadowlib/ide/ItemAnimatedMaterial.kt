package nl.shadowlink.tools.shadowlib.ide

import java.io.BufferedWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class ItemAnimatedMaterial(private val gameType: Int) {
    fun read(line: String?) {
        // TODO: Implement
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