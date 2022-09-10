package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils

/**
 * @author Shadow-Link
 */
class IMG_SA {

    fun loadImg(image: IMG) {
        val items = ArrayList<IMG_Item>()

        val rf = ReadFunctions(image.fileName)

        var itemCount = 0

        // Message.displayMsgHigh("Ver2: " + rf.readChar() + rf.readChar() + rf.readChar() + rf.readChar());
        itemCount = rf.readInt()
        // Message.displayMsgHigh("Item Count: " + itemCount);

        for (curItem in 0 until itemCount) {
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
        }

        image.items = items
    }

    /* private void updateCounter(int itemType){ switch(itemType){ case 0: //modelCount++; break; } } */

}
