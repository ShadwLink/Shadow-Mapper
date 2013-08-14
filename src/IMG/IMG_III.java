/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IMG;

import file_io.Message;
import file_io.ReadFunctions;
import shadowmapper.Util;
import java.util.ArrayList;

/**
 *
 * @author Kilian
 */
public class IMG_III {

    public void loadImg(IMG image) {
        ArrayList<IMG_Item> items = new ArrayList();

        ReadFunctions rf = new ReadFunctions();
        rf.openFile(image.getFileName().replace(".img", ".dir"));

        int itemCount = 0;

        while(rf.moreToRead() != 0){
            IMG_Item item = new IMG_Item();
            int itemOffset = rf.readInt()*2048;
            int itemSize = rf.readInt()*2048;
            String itemName = rf.readNullTerminatedString(24);
            int itemType = Util.getFileType(itemName, image);
            Message.displayMsgHigh("Offset: " + Util.getHexString(itemOffset));
            Message.displayMsgHigh("Size: " + itemSize + " bytes");
            Message.displayMsgHigh("Name: " + itemName);
            Message.displayMsgHigh("Type: " + itemType);
            item.setOffset(itemOffset);
            item.setName(itemName);
            item.setSize(itemSize);
            item.setType(itemType);
            items.add(item);
            itemCount++;
        }

        Message.displayMsgHigh("Final Item Count: " + itemCount);

        if(items.size() > 0) image.setItems(items);
        else image.setItems(null);
    }

}
