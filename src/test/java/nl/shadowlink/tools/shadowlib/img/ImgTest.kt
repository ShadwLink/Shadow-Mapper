package nl.shadowlink.tools.shadowlib.img

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

internal class ImgTest {

    @Test
    fun `when encryption state was not changed when toggled, then is requires save is not set`() {
        val img = Img(Path(""), mutableListOf(), isEncrypted = true)

        img.toggleEncryption(true)

        assertFalse(img.isSaveRequired)
    }

    @Test
    fun `when encryption state is toggled, then is requires save is not set`() {
        val img = Img(Path(""), mutableListOf(), isEncrypted = true)

        img.toggleEncryption(false)

        assertTrue(img.isSaveRequired)
    }
}