package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils
import java.io.File

/**
 * @author Shadow-Link
 */
class Img(
    var file: File,
    var items: MutableList<ImgItem> = mutableListOf(),
    var isEncrypted: Boolean = false
) {
    val fileName: String
        get() = file.name

    var isSaveRequired = false

    private constructor(file: File) : this(file, items = mutableListOf()) {
        isSaveRequired = true
    }

    fun toggleEncryption(enabled: Boolean) {
        isEncrypted = enabled
        isSaveRequired = true
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
            isSaveRequired = true
            rf.closeFile()
        }
    }

    fun removeItem(imgItem: ImgItem) {
        items.remove(imgItem)
        isSaveRequired = true
    }

    fun save() {
        TODO("IMG Saving is not implemented")
    }

    companion object {

        fun createNewImg(file: File): Img {
            return Img(file)
        }
    }
}
