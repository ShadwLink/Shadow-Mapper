package nl.shadowlink.tools.shadowlib.ide

import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.BufferedWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class ItemCars(private val gameType: GameType) : IdeItem() {
    lateinit var modelName: String
    var textureName: String? = null
    var type: String? = null
    var handlingID: String? = null
    override fun read(line: String) {
        var line = line
        line = line.replace("\t".toRegex(), "")
        line = line.replace(" ".toRegex(), "")
        println("Car: $line")
        val split = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        modelName = split[0]
        textureName = split[1]
        type = split[2]
        handlingID = split[3]
        println("Model Name: " + split[0])
        println("Texture Name: " + split[1])
        println("Type: " + split[2])
        println("HandLingID: " + split[3])
        println("Game Name: " + split[4])
        println("Anims: " + split[5])
        println("Anims2: " + split[6])
        println("Frq: " + split[7])
        println("MaxNum: " + split[8])
        println("Wheel Radius Front: " + split[9])
        println("Wheel Radius Rear: " + split[10])
        println("DefDirtLevel: " + split[11])
        println("Swankness: " + split[12])
        println("lodMult: " + split[13])
        println("Flags: " + split[14])
        //System.out.println("Extra stuff?: " + split[15]);
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