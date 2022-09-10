package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils

/**
 * @author Shadow-Link
 */
class imgV2 {

    fun loadImg(image: Img) {
        val items = ArrayList<ImgItem>()

        val rf = ReadFunctions(image.fileName)

        var itemCount = 0

        // Message.displayMsgHigh("Ver2: " + rf.readChar() + rf.readChar() + rf.readChar() + rf.readChar());
        itemCount = rf.readInt()
        // Message.displayMsgHigh("Item Count: " + itemCount);

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

        image.items = items
    }
}
