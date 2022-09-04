package nl.shadowlink.tools.shadowlib.ipl;

import com.nikhaldimann.inieditor.IniEditor;
import nl.shadowlink.tools.io.ReadFunctions;

/**
 * @author Shadow-Link
 */
public class Item_PATH extends IPL_Item {
    private int gameType;

    Item_PATH(int gameType) {
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
    public void read(ReadFunctions rf, IniEditor ini) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
