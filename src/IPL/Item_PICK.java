/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IPL;

import com.nikhaldimann.inieditor.IniEditor;
import file_io.Message;
import file_io.ReadFunctions;

/**
 *
 * @author Kilian
 */
public class Item_PICK extends IPL_Item{
    private int gameType;

    Item_PICK(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        Message.displayMsgHigh(line);
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
