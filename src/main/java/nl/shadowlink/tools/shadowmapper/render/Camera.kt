package nl.shadowlink.tools.shadowmapper.render

import nl.shadowlink.tools.io.Vector3D
import kotlin.math.cos
import kotlin.math.sin

/**
 * This class is used for the Camera movement
 *
 * @author Shadow-Link
 */
class Camera(
    posX: Float,
    posY: Float,
    posZ: Float,
    viewX: Float,
    viewY: Float,
    viewZ: Float,
    upX: Float,
    upY: Float,
    upZ: Float
) {
    @JvmField
    var pos = Vector3D()

    @JvmField
    var view = Vector3D()

    @JvmField
    var up = Vector3D()
    private val camYaw = 0.0f
    private val camPitch = 0.0f

    /**
     * Constructor of the camera.
     * @param posX The X Position of the camera
     * @param posY The Y Position of the camera
     * @param posZ The Z Position of the camera
     * @param viewX The X View of the camera
     * @param viewY The Y View of the camera
     * @param viewZ The Z View of the camera
     * @param upX The X Up of the camera
     * @param upY The Y Up of the camera
     * @param upZ The Z Up of the camera
     */
    init {
        pos.x = posX
        pos.y = posY
        pos.z = posZ
        view.x = viewX
        view.y = viewY
        view.z = viewZ
        up.x = upX
        up.y = upY
        up.z = upZ
    }

    /**
     * Set the camera position to a certain position
     * Use this with pointCamera
     * @param posX The X Position of the camera
     * @param posY The Y Position of the camera
     * @param posZ The Z Position of the camera
     */
    fun setCameraPosition(posX: Float, posY: Float, posZ: Float) {
        pos.x = posX
        pos.y = posY
        pos.z = posZ
    }

    /**
     * Move Camera in the direction you are viewing
     * @param speed The speed of the camera
     */
    fun moveCamera(speed: Float) {
        val x = view.x - pos.x
        val y = view.y - pos.y
        val z = view.z - pos.z
        pos.x = pos.x + x * speed
        pos.y = pos.y + y * speed
        pos.z = pos.z + z * speed
        view.x = view.x + x * speed
        view.y = view.y + y * speed
        view.z = view.z + z * speed
    }

    /**
     * Change the camera rotation
     * @param speed The speed of the camera
     */
    fun rotateView(speed: Float) {
        val x = view.x - pos.x
        val z = view.z - pos.z
        view.z = (pos.z + sin(speed.toDouble()) * x + cos(speed.toDouble()) * z).toFloat()
        view.x = (pos.x + cos(speed.toDouble()) * x - sin(speed.toDouble()) * z).toFloat()
    }

    /**
     * Strafe the camera
     * @param speed The speed of the camera
     */
    fun strafeCamera(speed: Float) {
        val x = view.x - pos.x
        val z = view.z - pos.z
        val oX = -z
        val oZ = x
        pos.x = pos.x + oX * speed
        pos.z = pos.z + oZ * speed
        view.x = view.x + oX * speed
        view.z = view.z + oZ * speed
    }

    /**
     * Returns the current X position
     * @return The X Position of the camera
     */
    val posX: Float
        get() = pos.x

    /**
     * Returns the current Y position
     * @return The Y Position of the camera
     */
    val posY: Float
        get() = pos.y

    /**
     * Returns the current Z position
     * @return The Z position of the camera
     */
    val posZ: Float
        get() = pos.z

    /**
     * Returns the current X view
     * @return The X view
     */
    val viewX: Float
        get() = view.x
    /**
     * Returns the current Y view
     * @return The Y view
     */
    /**
     * Set Y view
     * @param y Set the Y view
     */
    var viewY: Float
        get() = view.y
        set(y) {
            view.y = y
        }

    /**
     * Returns the current Z view
     * @return The Z view
     */
    val viewZ: Float
        get() = view.z

    /**
     * Returns the current X up
     * @return The X up
     */
    val upX: Float
        get() = up.x

    /**
     * Returns the current Y up
     * @return The Y up
     */
    val upY: Float
        get() = up.y

    /**
     * Returns the current Z up
     * @return The Z up
     */
    val upZ: Float
        get() = up.z
}