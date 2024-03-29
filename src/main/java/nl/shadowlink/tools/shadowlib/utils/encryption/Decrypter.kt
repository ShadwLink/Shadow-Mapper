package nl.shadowlink.tools.shadowlib.utils.encryption

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.img.ImgV3
import java.util.logging.Level
import java.util.logging.Logger
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Decrypter(
    key: ByteArray
) {

    private val cipher: Cipher

    private var tempBuffer = ByteArray(16)

    init {
        cipher = Cipher.getInstance("Rijndael/ECB/NoPadding").apply {
            init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "Rijndael"))
        }
    }

    fun decrypt16byteBlock(rf: ReadFunctions): ByteArray {
        rf.readBytes(tempBuffer)

        for (j in 1..16) { // 16 (pointless) repetitions
            try {
                tempBuffer = cipher.doFinal(tempBuffer)
            } catch (ex: Exception) {
                Logger.getLogger(ImgV3::class.java.name).log(Level.SEVERE, null, ex)
            }

        }
        return tempBuffer
    }

    fun decrypt(data: ByteArray): ByteArray {
        var tempData = data
        for (j in 1..16) { // 16 (pointless) repetitions
            try {
                tempData = cipher.doFinal(tempData)
            } catch (ex: Exception) {
                Logger.getLogger(ImgV3::class.java.name).log(Level.SEVERE, null, ex)
            }

        }
        return tempData
    }
}
