package nl.shadowlink.tools.shadowlib.utils.filechooser

import java.io.File
import javax.swing.filechooser.FileFilter

/**
 * @author Shadow-Link
 */
class ExtensionFilter(
    private val extensions: Set<String>,
    private val description: String,
) : FileFilter() {

    override fun accept(file: File): Boolean {
        if (file.isDirectory) return true

        return extensions.any { extension -> extension == file.extension }
    }

    override fun getDescription(): String = "$description (${extensions.joinToString(", ")})"
}