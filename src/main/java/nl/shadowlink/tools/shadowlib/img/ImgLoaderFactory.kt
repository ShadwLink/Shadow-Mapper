package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.shadowlib.utils.GameType

object ImgLoaderFactory {

    fun getImgLoader(gameType: GameType, encryptionKey: ByteArray? = null): ImgLoader {
        return when (gameType) {
            GameType.GTA_III -> ImgV1()
            GameType.GTA_VC -> ImgV1()
            GameType.GTA_SA -> ImgV2()
            GameType.GTA_IV -> ImgV3(requireNotNull(encryptionKey) { "Encryption key is required to load GTA: IV images" })
            else -> throw IllegalStateException("GameType is unknown '$gameType'")
        }
    }
}