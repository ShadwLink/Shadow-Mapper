package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils

/**
 * @author Shadow-Link
 */
class IMG_III {

    fun loadImg(image: IMG) {
        val items = ArrayList<IMG_Item>()

        val rf = ReadFunctions(image.fileName.replace(".img", ".dir"))

        var itemCount = 0

        while (rf.moreToRead() != 0) {
            val item = IMG_Item()
            val itemOffset = rf.readInt() * 2048
            val itemSize = rf.readInt() * 2048
            val itemName = rf.readNullTerminatedString(24)
            val itemType = Utils.getFileType(itemName)
            // Message.displayMsgHigh("Offset: " + Utils.getHexString(itemOffset));
            // Message.displayMsgHigh("Size: " + itemSize + " bytes");
            // Message.displayMsgHigh("Name: " + itemName);
            // Message.displayMsgHigh("Type: " + itemType);
            item.offset = itemOffset
            item.name = itemName
            item.size = itemSize
            item.type = itemType
            items.add(item)
            itemCount++
        }

        image.items = items
        // Message.displayMsgHigh("Final Item Count: " + itemCount);
    }
}
