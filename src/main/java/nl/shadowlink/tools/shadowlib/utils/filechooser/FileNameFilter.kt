package nl.shadowlink.tools.shadowlib.utils.filechooser

import java.io.File
import javax.swing.filechooser.FileFilter

/**
 * @author Shadow-Link
 */
class FileNameFilter(
    private val fileNames: Set<String>,
    private val description: String,
) : FileFilter() {

    override fun accept(file: File): Boolean {
        if (file.isDirectory) return true

        return fileNames.any { fileName -> fileName == file.name }
    }

    override fun getDescription(): String = "$description (${fileNames.joinToString(", ")})"
}