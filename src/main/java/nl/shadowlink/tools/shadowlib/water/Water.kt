package nl.shadowlink.tools.shadowlib.water

import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class Water(
    val fileName: String,
    val gameType: GameType
) {

    @JvmField
    var planes: ArrayList<WaterPlane> = ArrayList()

    init {
        read()
    }

    fun read() {
        val input = BufferedReader(FileReader(fileName))

        try {
            var line: String?
            while (input.readLine().also { line = it } != null) {
                println(line)
                val wp = WaterPlane(line!!)
                planes.add(wp)
            }
        } catch (ex: IOException) {
            Logger.getLogger(Water::class.java.name).log(Level.SEVERE, null, ex)
        }
        input.close()
    }
}