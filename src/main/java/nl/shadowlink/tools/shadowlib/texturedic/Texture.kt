package nl.shadowlink.tools.shadowlib.texturedic

/**
 *
 * @author Shadow-Link
 */
class Texture(
    val diffuseTexName: String
) {
    @JvmField
    var width = 0

    @JvmField
    var height = 0

    @JvmField
    var compression = 0
    var offset = 0

    @JvmField
    var conversionOffset = 0

    @JvmField
    var dataSize = 0

    var alphaTexName: String? = null

    @JvmField
    var data: ByteArray? = null
}