package nl.shadowlink.tools.shadowlib.ipl;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class Item_BLOK extends IPL_Item {
    private GameType gameType;

    Item_BLOK(GameType gameType) {
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
        Logger.getLogger("IPL").log(Level.INFO, getClass().getSimpleName() + " not supported yet.");
    }

}
