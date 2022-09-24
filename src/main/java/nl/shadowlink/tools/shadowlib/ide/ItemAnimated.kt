package nl.shadowlink.tools.shadowlib.ide

import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.BufferedWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class ItemAnimated(private val gameType: GameType) : IdeItem() {
    lateinit var modelName: String
    var textureName: String? = null
    override fun read(line: String) {
        var line = line
        line = line.replace(" ", "")
        val split = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        modelName = split[0]
        textureName = split[1]

//		ModelName, TextureName, Wad, DrawDist, Flag1, Flag2, (Bounds min)X,Y,Z, (Bounds max)X,Y,Z, (Bounds Sphere)X,Y,Z, Radius, WDD
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