package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils

/**
 * @author Shadow-Link
 */
class ImgV1 {

    fun loadImg(image: Img) {
        val items = ArrayList<ImgItem>()

        val rf = ReadFunctions(image.fileName.replace(".img", ".dir"))


        while (rf.moreToRead() != 0) {
            val item = ImgItem()
            val itemOffset = rf.readInt() * 2048
            val itemSize = rf.readInt() * 2048
            val itemName = rf.readNullTerminatedString(24)
            val itemType = Utils.getFileType(itemName)
            item.offset = itemOffset
            item.name = itemName
            item.size = itemSize
            item.type = itemType
            items.add(item)
        }

        image.items = items
        // Message.displayMsgHigh("Final Item Count: " + itemCount);
    }
}
