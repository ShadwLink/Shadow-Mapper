package nl.shadowlink.tools.shadowmapper

import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowmapper.gui.install.Install
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

internal class FileManagerTest {

    private val testInstall = Install(id = "id", name = "name", path = "c:/test/", gameType = GameType.GTA_IV)

    private lateinit var fm: FileManager

    @BeforeEach
    fun beforeEach() {
        fm = FileManager().apply {
            setInstall(testInstall)
        }
    }

    @Test
    fun `when new img is not on the game path, then img is not created`() {
        val result = fm.addNewImg(Path("c:/other_path/test.img"))

        assertIs<CommandResult.Failed>(result)
    }

    @Test
    fun `when img can be added, then img is added to img list`() {
        val imgFile = Path("c:/test/test.img")
        val result = fm.addNewImg(imgFile)

        val addedImg = fm.imgs.last()
        assertIs<CommandResult.Success>(result)
        assertEquals(1, fm.imgs.size)
        assertEquals(imgFile, addedImg.path)
        assertTrue(addedImg.isSaveRequired)
    }
}