package nl.shadowlink.tools.shadowlib.utils.hashing

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

object SHA1Hasher {

    /**
     * Hashes the input array using SHA-1
     *
     * @return hash or `null` if something went wrong
     */
    @JvmStatic
    fun hash(input: ByteArray): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-1")
            return byteArray2Hex(md.digest(input))
        } catch (ex: NoSuchAlgorithmException) {
            null
        }
    }

    private fun byteArray2Hex(hash: ByteArray): String =
        Formatter().apply {
            for (b in hash) {
                format("%02X", b)
            }
        }.toString()
}