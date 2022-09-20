package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils
import nl.shadowlink.tools.shadowlib.utils.saving.Saveable
import nl.shadowlink.tools.shadowlib.utils.saving.SaveableFile
import java.io.File
import java.nio.file.Path
import kotlin.io.path.name

/**
 * @author Shadow-Link
 */
class Img(
    val path: Path,
    val items: MutableList<ImgItem> = mutableListOf(),
    var isEncrypted: Boolean = false
) : Saveable by SaveableFile() {
    val fileName: String
        get() = path.name

    private constructor(path: Path) : this(path, items = mutableListOf()) {
        setSaveRequired()
    }

    fun toggleEncryption(enabled: Boolean) {
        if (isEncrypted != enabled) {
            isEncrypted = enabled
            setSaveRequired()
        }
    }

    val totalItemCount: Int
        get() = items.count()

    fun getItemsOfType(extension: String): List<ImgItem> {
        return items.filter { item -> item.name.endsWith(extension) }
    }

    fun getItemOfTypeCount(extension: String): Int {
        return items.count { item -> item.name.endsWith(extension) }
    }

    /**
     * Find the index of the item with the given [name].
     *
     * @return the index of the item or `-1` when item could not be found
     */
    fun findItemIndex(name: String): Int {
        return items.indexOfFirst { item -> item.name == name }
    }

    /**
     * Find the item with the given name
     *
     * @return the item or `null` if item can not be found
     */
    fun findItem(name: String): ImgItem? {
        return items.firstOrNull { item -> item.name == name }
    }

    /**
     * Adds the [file] to the IMG
     */
    fun addItem(file: File) {
        if (file.isFile && file.canRead()) {
            val rf = ReadFunctions(file.absolutePath)
            println("File: " + file.absolutePath)
            val wf = WriteFunctions(file.absolutePath)
            println("File size: " + file.length())
            val newFile: ByteArray = rf.readArray(file.length().toInt())
            val tempItem = ImgItem(file.name)
            tempItem.type = Utils.getResourceType(file.name)
            tempItem.offset = wf.fileSize
            tempItem.size = file.length().toInt()
            if (tempItem.isResource) {
                rf.seek(0x8)
                tempItem.flags = rf.readInt()
            }
            items.add(tempItem)
            wf.gotoEnd()
            wf.write(newFile)
            if (wf.closeFile()) {
                println("Closed file")
            } else {
                println("Unable to close the file")
            }
            setSaveRequired()
            rf.closeFile()
        }
    }

    fun removeItem(imgItem: ImgItem) {
        items.remove(imgItem)
        setSaveRequired()
    }

    fun save() {
        TODO("IMG Saving is not implemented")
    }

    companion object {

        fun createNewImg(path: Path): Img {
            return Img(path)
        }
    }
}
