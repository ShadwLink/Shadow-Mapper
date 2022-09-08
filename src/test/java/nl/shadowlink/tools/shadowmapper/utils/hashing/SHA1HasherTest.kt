package nl.shadowlink.tools.shadowmapper.utils.hashing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SHA1HasherTest {

    @Test
    fun `value is hashed`() {
        assertEquals("A94A8FE5CCB19BA61C4C0873D391E987982FBBD3", SHA1Hasher.hash("test".toByteArray()))
    }
}