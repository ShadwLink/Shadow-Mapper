package nl.shadowlink.tools.shadowlib.texturedic

import nl.shadowlink.tools.io.ByteReader
import nl.shadowlink.tools.shadowlib.utils.GameType

/**
 * @author Shadow-Link
 */
class TextureDic {
    var fileName: String
    private var gameType: GameType

    //    public int[] textureId;
    var textureCount = 0
    var flags = 0
    var size = 0

    @JvmField
    var textures = ArrayList<Texture>()
    var br: ByteReader
    var fileSize = -1
    private var compressed = true

    constructor(fileName: String, br: ByteReader, gameType: GameType, fileSize: Int) {
        this.fileName = fileName
        this.gameType = gameType
        this.br = br
        this.fileSize = fileSize
        loadTextureDic()
    }

    constructor(fileName: String, br: ByteReader, gameType: GameType) {
        this.fileName = fileName
        this.gameType = gameType
        this.br = br
        loadTextureDic()
    }

    constructor(fileName: String, br: ByteReader, gameType: GameType, compressed: Boolean, sysSize: Int) {
        this.fileName = fileName
        this.gameType = gameType
        this.br = br
        fileSize = sysSize
        this.compressed = compressed
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