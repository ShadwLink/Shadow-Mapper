package nl.shadowlink.tools.shadowmapper.utils.hashing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class OneAtATimeHasherTest {

    private val hasher = OneAtATimeHasher()

    @Test
    fun `value is hashed`() {
        assertEquals(2173617389, hasher.hash("nj5_cstgrnda08"))
        assertEquals(3324439124, hasher.hash("W_Birch_MD_INGAME"))
    }
}
