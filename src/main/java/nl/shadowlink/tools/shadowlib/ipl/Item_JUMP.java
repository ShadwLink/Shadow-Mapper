package nl.shadowlink.tools.shadowlib.ipl;

import com.nikhaldimann.inieditor.IniEditor;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable;

/**
 * @author Shadow-Link
 */
public class Item_JUMP extends IPL_Item {
    private int gameType;

    Item_JUMP(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        //Message.displayMsgHigh(line);
    }

    @Override
    public void read(ReadFunctions rf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(ReadFunctions rf, HashTable hashTable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
