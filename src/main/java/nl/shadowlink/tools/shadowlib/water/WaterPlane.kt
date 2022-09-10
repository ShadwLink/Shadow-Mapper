package nl.shadowlink.tools.shadowlib.water

/**
 *
 * @author Shadow-Link
 */
class WaterPlane(line: String) {

    @JvmField
    var points = arrayOfNulls<WaterPoint?>(4)
    var param: Int
    var unknown: Float

    @JvmField
    var selected = false

    @JvmField
    var u = 0.0f

    @JvmField
    var v = 1.0f

    init {
        val split = line.split("   ").dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in split.indices) {
            points[i] = WaterPoint(split[i])
        }
        val subSplit = split[3].split(" ").dropLastWhile { it.isEmpty() }.toTypedArray()
        param = Integer.valueOf(subSplit[7])
        unknown = java.lang.Float.valueOf(subSplit[8])
        v = points[0]!!.coord.x - points[1]!!.coord.x
        if (Integer.signum(v.toInt()) == -1) v *= -1
        v /= 10
    }
}