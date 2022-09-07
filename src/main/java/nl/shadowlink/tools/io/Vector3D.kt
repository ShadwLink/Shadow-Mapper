package nl.shadowlink.tools.io

/**
 * @author Shadow-Link
 */
class Vector3D(
    @JvmField
    var x: Float = 0.0f,
    @JvmField
    var y: Float = 0.0f,
    @JvmField
    var z: Float = 0.0f,
) {

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

    operator fun times(v3d: Vector3D) {
        x *= v3d.x
        y *= v3d.y
        z *= v3d.z
    }

    override fun toString(): String {
        return "$x, $y, $z"
    }
}