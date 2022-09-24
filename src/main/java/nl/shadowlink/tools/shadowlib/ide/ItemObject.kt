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
class ItemObject(private val gameType: GameType) : IdeItem() {
    var id = 0

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

    override fun read(line: String) {
        var line = line
        line = line.replace(" ", "")
        val split = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        when (gameType) {
            GameType.GTA_IV -> {
                modelName = split[0]
                textureName = split[1]
                drawDistance = FloatArray(1)
                if (split.size > 4) {
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
                }
            }

            GameType.GTA_SA -> {
                id = Integer.valueOf(split[0])
                modelName = split[1]
                textureName = split[2]
                drawDistance = FloatArray(1)
                drawDistance[0] = java.lang.Float.valueOf(split[3])
                flag1 = Integer.valueOf(split[4])
            }

            else -> {
                id = Integer.valueOf(split[0])
                modelName = split[1]
                textureName = split[2]
                objectCount = Integer.valueOf(split[3])
                drawDistance = FloatArray(objectCount)
                var i = 0
                while (i < objectCount) {
                    drawDistance[i] = java.lang.Float.valueOf(split[4 + i])
                    i++
                }
                flag1 = Integer.valueOf(split[4 + objectCount])
            }
        }
        display()
    }

    fun display() {
        // if (gameType != Constants.gIV) {
        // //Message.displayMsgHigh("ID: " + id);
        // }
        // //Message.displayMsgHigh("ModelName: " + modelName);
        // //Message.displayMsgHigh("TextureName: " + textureName);
        // if (gameType == Constants.gIII || gameType == Constants.gVC) {
        // //Message.displayMsgHigh("ObjectCount: " + objectCount);
        // for (int i = 0; i < objectCount; i++) {
        // //Message.displayMsgHigh("DrawDistance" + i + ": " + drawDistance[i]);
        // }
        // }
        // if (gameType == Constants.gIV) {
        // //Message.displayMsgHigh("Flag: " + flag1);
        // //Message.displayMsgHigh("Flag2: " + flag2);
        // //Message.displayMsgHigh("BoundsMAX: " + boundsMax.x + ", " + boundsMax.y + ", " + boundsMax.z);
        // //Message.displayMsgHigh("BoundsMIN: " + boundsMin.x + ", " + boundsMin.y + ", " + boundsMin.z);
        // //Message.displayMsgHigh("BoundsSphere: " + boundsSphere.x + ", " + boundsSphere.y + ", " + boundsSphere.z +
        // ", " + boundsSphere.w);
        // //Message.displayMsgHigh("WDD: " + WDD);
        // }
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
            output.write(line)
            output.newLine()
            println("Line: $line")
        } catch (ex: IOException) {
            Logger.getLogger(ItemObject::class.java.name).log(Level.SEVERE, null, ex)
        }
    }
}