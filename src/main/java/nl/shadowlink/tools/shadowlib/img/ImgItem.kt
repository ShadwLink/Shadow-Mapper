package nl.shadowlink.tools.shadowlib.img

/**
 * @author Shadow-Link
 */
class ImgItem {

    var type: Int = 0
        set(type) {
            if (type < 1000) {
                isResource = true
            }
            field = type
        }

    var offset: Int = 0
    var size: Int = 0
    var name: String? = null

    var isResource = false
    var flags = 0
}
