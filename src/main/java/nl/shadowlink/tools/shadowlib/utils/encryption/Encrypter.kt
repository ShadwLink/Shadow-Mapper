package nl.shadowlink.tools.shadowlib.utils.encryption

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Encrypter(
    key: ByteArray
) {

    private val cipher: Cipher
    
    init {
        cipher = Cipher.getInstance("Rijndael/ECB/NoPadding").apply {
            init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "Rijndael"))
        }
    }

    @Throws(Exception::class)
    fun encryptAES(data: ByteArray): ByteArray {
        return cipher.doFinal(data)
    }
}
