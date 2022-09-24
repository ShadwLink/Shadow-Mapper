package nl.shadowlink.tools.shadowlib.ide

import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.BufferedWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class ItemTxdPack(private val gameType: GameType) : IdeItem() {
    var texDic: String? = null
    var texDicParent: String? = null
    override fun read(line: String) {
        var line = line
        line = line.replace(" ", "")
        val split = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        texDic = split[0]
        texDicParent = split[1]
    }

    fun save(output: BufferedWriter) {
        try {
            var line = texDic
            line += ", $texDicParent"
            output.write(
                """
    $line
    
    """.trimIndent()
            )
        } catch (ex: IOException) {
            Logger.getLogger(ItemObject::class.java.name).log(Level.SEVERE, null, ex)
        }
    }
}