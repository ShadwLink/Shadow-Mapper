package nl.shadowlink.tools.shadowlib.utils.filechooser

import java.awt.Component
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

object FileChooserUtil {

    @JvmStatic
    fun openFileChooser(parent: Component, filter: FileFilter, initialPath: File? = null): File? {
        val chooser = JFileChooser(initialPath).apply {
            removeChoosableFileFilter(acceptAllFileFilter)
            isMultiSelectionEnabled = false
            fileFilter = filter
            dialogTitle = "Open file.."
        }

        return if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            chooser.selectedFile
        } else null
    }
}