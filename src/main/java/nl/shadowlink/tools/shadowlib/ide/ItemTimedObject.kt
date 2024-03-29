package nl.shadowlink.tools.shadowlib.ide

import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.io.Vector4D
import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.BufferedWriter
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class ItemTimedObject(private val gameType: GameType) : IdeItem() {
    lateinit var modelName: String
    var textureName: String? = null
    var objectCount = 0
    var drawDistance: FloatArray = floatArrayOf()
    var flag1 = 0
    var flag2 = 0
    var boundsMin = Vector3D(0.0f, 0.0f, 0.0f)
    var boundsMax = Vector3D(0.0f, 0.0f, 0.0f)
    var boundsSphere = Vector4D(0.0f, 0.0f, 0.0f, 0.0f)
    var WDD: String? = null
    var timedFlags = 0

    override fun read(line: String) {
        var line = line
        line = line.replace(" ", "")
        val split = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        modelName = split[0]
        textureName = split[1]
        drawDistance = FloatArray(1)
        drawDistance[0] = java.lang.Float.valueOf(split[2])
        flag1 = Integer.valueOf(split[3])
        flag2 = Integer.valueOf(split[4])
        boundsMin = Vector3D(
            java.lang.Float.valueOf(split[5]), java.lang.Float.valueOf(split[6]), java.lang.Float.valueOf(
                split[7]
            )
        )
        boundsMax = Vector3D(
            java.lang.Float.valueOf(split[8]), java.lang.Float.valueOf(split[9]), java.lang.Float.valueOf(
                split[10]
            )
        )
        boundsSphere = Vector4D(
            java.lang.Float.valueOf(split[11]), java.lang.Float.valueOf(split[12]), java.lang.Float.valueOf(
                split[13]
            ), java.lang.Float.valueOf(split[14])
        )
        WDD = split[15]
        timedFlags = Integer.valueOf(split[16])
    }

    fun save(output: BufferedWriter) {
        try {
            var line = modelName
            line += ", $textureName"
            line += ", " + drawDistance[0]
            line += ", $flag1"
            line += ", $flag2"
            line += ", " + boundsMin.x
            line += ", " + boundsMin.y
            line += ", " + boundsMin.z
            line += ", " + boundsMax.x
            line += ", " + boundsMax.y
            line += ", " + boundsMax.z
            line += ", " + boundsSphere.x
            line += ", " + boundsSphere.y
            line += ", " + boundsSphere.z
            line += ", " + boundsSphere.w
            line += ", $WDD"
            line += ", $timedFlags"
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