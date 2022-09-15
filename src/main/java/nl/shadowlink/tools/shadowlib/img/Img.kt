package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.Utils
import java.io.File

/**
 * @author Shadow-Link
 */
class Img(
    var fileName: String,
    var gameType: GameType,
    key: ByteArray,
    autoLoad: Boolean,
    containsProps: Boolean
) {

    var items: ArrayList<ImgItem> = ArrayList()

    var isChanged = false

    var isEncrypted: Boolean = false

    fun toggleEncryption(enabled: Boolean) {
        isEncrypted = enabled
        isChanged = true
    }

    var containsProps = false

    var key = ByteArray(32)

    val totalItemCount: Int
        get() = items.count()

    init {
        this.key = key
        this.containsProps = containsProps
        if (autoLoad) {
            loadImg()
        }
    }

    fun getItemsOfType(extension: String): List<ImgItem> {
        return items.filter { item -> item.name.endsWith(extension) }
    }

    fun getItemOfTypeCount(extension: String): Int {
        return items.count { item -> item.name.endsWith(extension) }
    }

    private fun loadImg() {
        when (gameType) {
            GameType.GTA_III -> ImgV1().loadImg(this)
            GameType.GTA_VC -> ImgV1().loadImg(this)
            GameType.GTA_SA -> imgV2().loadImg(this)
            GameType.GTA_IV -> ImgV3().loadImg(this)
            else -> throw IllegalStateException("GameType $gameType not supported")
        }
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
            val wf = WriteFunctions(fileName)
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
            isChanged = true
            rf.closeFile()
        }
    }

    fun removeItem(imgItem: ImgItem) {
        items.remove(imgItem)
        isChanged = true
    }

    fun save() {
        when (gameType) {
            /* case Finals.gIII: new IMG_III().saveImg(this); break; case Finals.gVC: new IMG_VC().saveImg(this); break;
		 * case Finals.gSA: new IMG_SA().saveImg(this); break; */
            GameType.GTA_IV -> ImgV3().saveImg(this)
            GameType.GTA_III -> TODO()
            GameType.GTA_VC -> TODO()
            GameType.GTA_SA -> TODO()
            GameType.GTA_V -> TODO()
            null -> TODO()
        }
    }
}
