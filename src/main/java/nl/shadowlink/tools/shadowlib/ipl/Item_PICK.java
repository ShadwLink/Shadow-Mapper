package nl.shadowlink.tools.shadowlib.ipl;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable;

/**
 * @author Shadow-Link
 */
public class Item_PICK extends IplItem {
    private GameType gameType;

    Item_PICK(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        // Message.displayMsgHigh(line);
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
