package nl.shadowlink.tools.shadowlib.water

import nl.shadowlink.tools.io.Vector3D

/**
 * @author Shadow-Link
 */
class WaterPoint(line: String) {
    @JvmField
    var coord: Vector3D
    var speedX: Float
    var speedY: Float
    var unknown: Float
    var waveHeight: Float

    init {
        val split = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        coord = Vector3D(
            java.lang.Float.valueOf(split[0]), java.lang.Float.valueOf(split[1]), java.lang.Float.valueOf(
                split[2]
            )
        )
        speedX = java.lang.Float.valueOf(split[3])
        speedY = java.lang.Float.valueOf(split[4])
        unknown = java.lang.Float.valueOf(split[5])
        waveHeight = java.lang.Float.valueOf(split[6])
    }
}