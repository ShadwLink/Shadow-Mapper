package nl.shadowlink.tools.shadowlib.texturedic

import nl.shadowlink.tools.io.ByteReader
import nl.shadowlink.tools.shadowlib.utils.GameType

/**
 * @author Shadow-Link
 */
class TextureDic(
    var fileName: String,
    var br: ByteReader,
    private val gameType: GameType,
    private val compressed: Boolean,
    sysSize: Int
) {

    var textureCount = 0
    var flags = 0
    var size = 0

    @JvmField
    var textures = ArrayList<Texture>()
    var fileSize = sysSize

    init {
        loadTextureDic()
    }

    private fun loadTextureDic(): Boolean {
        when (gameType) {
            GameType.GTA_IV -> if (compressed) {
                TextureDicIV().loadTextureDic(this, compressed, 0)
            } else {
                TextureDicIV().loadTextureDic(this, compressed, fileSize)
            }

            else -> TextureDic_III_ERA().loadTextureDic(this)
        }
        return true
    }

    fun addTexture(tex: Texture) {
        textures.add(tex)
    }
}