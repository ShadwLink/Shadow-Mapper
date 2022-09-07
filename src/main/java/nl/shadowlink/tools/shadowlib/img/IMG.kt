package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.model.model.Model
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic
import nl.shadowlink.tools.shadowlib.utils.Constants
import nl.shadowlink.tools.shadowlib.utils.Constants.GameType
import nl.shadowlink.tools.shadowlib.utils.Utils
import java.io.File

/**
 * @author Shadow-Link
 */
class IMG(var fileName: String?, var gameType: GameType?, key: ByteArray, autoLoad: Boolean, containsProps: Boolean) {

    var items: ArrayList<IMG_Item> = ArrayList()

    var isChanged = false

    var encrypted = false
    var containsProps = false

    var key = ByteArray(32)

    val itemCount: Int
        get() = items.count()

    val cutCount: Int
        get() = getItemOfTypeCount("cut")

    val wtdCount: Int
        get() = getItemOfTypeCount("wtd")
    val wbdCount: Int
        get() = getItemOfTypeCount("wbd")
    val wbnCount: Int
        get() = getItemOfTypeCount("wbn")
    val wplCount: Int
        get() = getItemOfTypeCount("wpl")
    val wddCount: Int
        get() = getItemOfTypeCount("wdd")
    val wdrCount: Int
        get() = getItemOfTypeCount("wdr")
    val wadCount: Int
        get() = getItemOfTypeCount("wad")
    val wftCount: Int
        get() = getItemOfTypeCount("wft")

    init {
        this.key = key
        this.containsProps = containsProps
        if (autoLoad) {
            loadImg()
        }
    }

    private fun getItemOfTypeCount(extension: String): Int {
        return items.count { item -> item.name?.endsWith(extension) == true }
    }

    private fun loadImg(): Boolean {
        when (gameType) {
            Constants.GameType.GTA_III -> IMG_III().loadImg(this)
            Constants.GameType.GTA_VC -> IMG_VC().loadImg(this)
            Constants.GameType.GTA_SA -> IMG_SA().loadImg(this)
            Constants.GameType.GTA_IV -> IMG_IV().loadImg(this)
            else -> throw IllegalStateException("Gametype $gameType not supported")
        }
        // TODO: Change this to something more meaningful
        return items != null
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
    fun findItem(name: String): IMG_Item? {
        return items.firstOrNull { item -> item.name == name }
    }

    fun addItem(mdl: Model, name: String) {
        var name = name
        val wf = WriteFunctions(fileName!!)
        name = name.toLowerCase()
        name = name.replace(".dff".toRegex(), ".wdr")
        val tempItem = IMG_Item()
        tempItem.name = name
        tempItem.type = Constants.rtWDR
        tempItem.offset = wf.fileSize
        wf.gotoEnd()
        mdl.convertToWDR(wf)
        tempItem.size = mdl.size
        tempItem.flags = mdl.flags

        items.add(tempItem)
        if (wf.closeFile()) {
            println("Closed file")
        } else {
            println("Unable to close the file")
        }
        isChanged = true
    }

    fun addItem(txd: TextureDic, name: String) {
        var name = name
        val wf = WriteFunctions(fileName!!)
        name = name.toLowerCase()
        name = name.replace(".txd".toRegex(), ".wtd")
        val tempItem = IMG_Item()
        tempItem.name = name
        tempItem.type = Constants.rtWTD
        tempItem.offset = wf.fileSize
        wf.gotoEnd()
        txd.convertToWTD(wf)
        tempItem.size = txd.size
        tempItem.flags = txd.flags

        items.add(tempItem)
        if (wf.closeFile()) {
            println("Closed file")
        } else {
            println("Unable to close the file")
        }
        isChanged = true
    }

    /**
     * Adds the [file] to the IMG
     */
    fun addItem(file: File) {
        if (file.isFile && file.canRead()) {
            var rf: ReadFunctions? = ReadFunctions()
            println("File: " + file.absolutePath)
            if (rf!!.openFile(file.absolutePath)) {
                var wf = WriteFunctions(fileName!!)
                println("File size: " + file.length())
                var newFile: ByteArray = rf.readArray(file.length().toInt())
                val tempItem = IMG_Item()
                tempItem.name = file.name
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
                rf = null
            } else {
                // JOptionPane.show//MessageDialog(this, "Unable to open " +
                // file.getName() + " for reading!");
            }
        }
    }

    fun save() {
        when (gameType) {
            /* case Finals.gIII: new IMG_III().saveImg(this); break; case Finals.gVC: new IMG_VC().saveImg(this); break;
		 * case Finals.gSA: new IMG_SA().saveImg(this); break; */
            GameType.GTA_IV -> IMG_IV().saveImg(this)
            GameType.GTA_III -> TODO()
            GameType.GTA_VC -> TODO()
            GameType.GTA_SA -> TODO()
            GameType.GTA_V -> TODO()
            null -> TODO()
        }
    }
}
