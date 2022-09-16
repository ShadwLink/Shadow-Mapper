package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils
import java.io.File

/**
 * @author Shadow-Link
 */
class ImgV2 : ImgLoader {

    override fun load(file: File): Img {
        val items = ArrayList<ImgItem>()

        val rf = ReadFunctions(file.absolutePath)

        var itemCount = 0

        // Message.displayMsgHigh("Ver2: " + rf.readChar() + rf.readChar() + rf.readChar() + rf.readChar());
        itemCount = rf.readInt()

        for (curItem in 0 until itemCount) {
            val itemOffset = rf.readInt() * 2048
            val itemSize = rf.readInt() * 2048
            val itemName = rf.readNullTerminatedString(24)
            val itemType = Utils.getFileType(itemName)
            val item = ImgItem(itemName)
            item.offset = itemOffset
            item.size = itemSize
            item.type = itemType
            items.add(item)
        }

        return Img(file = file, items = items)
    }
}
