package nl.shadowlink.tools.shadowlib.texturedic

/**
 *
 * @author Shadow-Link
 */
class Texture(
    val diffuseTexName: String,
    val dxtCompressionType: String?,
    val width: Int,
    val height: Int,
    val data: ByteArray,
) {
    @JvmField
    var compression = 0
    var offset = 0

    @JvmField
    var conversionOffset = 0

    @JvmField
    var dataSize = 0

    var alphaTexName: String? = null
}