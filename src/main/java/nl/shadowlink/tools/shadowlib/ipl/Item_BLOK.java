package nl.shadowlink.tools.shadowlib.ipl;

import com.nikhaldimann.inieditor.IniEditor;

import nl.shadowlink.tools.io.ReadFunctions;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class Item_BLOK extends IPL_Item {
    private int gameType;

    Item_BLOK(int gameType) {
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
    public void read(ReadFunctions rf, IniEditor ini) {
        Logger.getLogger("IPL").log(Level.INFO, getClass().getSimpleName() + " not supported yet.");
    }

}
