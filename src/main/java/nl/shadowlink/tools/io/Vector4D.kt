package nl.shadowlink.tools.io

/**
 * @author Shadow-Link
 */
class Vector4D(
    @JvmField
    var x: Float = 0.0f,
    @JvmField
    var y: Float = 0.0f,
    @JvmField
    var z: Float = 0.0f,
    @JvmField
    var w: Float = 0.0f
) {

    operator fun plus(v4d: Vector4D) {
        x += v4d.x
        y += v4d.y
        z += v4d.z
        w += v4d.w
    }

    operator fun minus(v4d: Vector4D) {
        x -= v4d.x
        y -= v4d.y
        z -= v4d.z
        w -= v4d.w
    }

    operator fun plus(v3d: Vector3D) {
        x += v3d.x
        y += v3d.y
        z += v3d.z
    }

    operator fun minus(v3d: Vector3D) {
        x -= v3d.x
        y -= v3d.y
        z -= v3d.z
    }

    fun print(name: String) {
        println("$name: $x, $y, $z, $w")
    }

    // float scale = (float)Math.sqrt(item.rotation.x * item.rotation.x + item.rotation.y * item.rotation.y +
    // item.rotation.z * item.rotation.z);
    val axisAngle: Vector4D
        get() {
            // float scale = (float)Math.sqrt(item.rotation.x * item.rotation.x + item.rotation.y * item.rotation.y +
            // item.rotation.z * item.rotation.z);
            val scale = -1.0f
            val rot = Vector4D()
            rot.x = x / scale
            rot.y = y / scale
            rot.z = z / scale
            rot.w = (Math.acos(w.toDouble()) * 2.0f).toFloat()
            rot.w = (rot.w * (180 / 3.14159265)).toFloat() // from radians to degrees
            return rot
        }

    override fun toString(): String {
        return "$x, $y, $z, $w"
    }

    fun toVector3D(): Vector3D {
        return Vector3D(x, y, z)
    }
}