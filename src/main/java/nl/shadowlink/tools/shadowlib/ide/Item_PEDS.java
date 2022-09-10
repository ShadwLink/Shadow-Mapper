package nl.shadowlink.tools.shadowlib.ide;

import nl.shadowlink.tools.shadowlib.utils.GameType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class Item_PEDS extends IdeItem {
    private GameType gameType;

    public Item_PEDS(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        // TODO: Implement
    }

    public void save(BufferedWriter output) {
        try {
            String line = "";
            output.write(line + "\n");
            System.out.println("Line: " + line);
        } catch (IOException ex) {
            Logger.getLogger(Item_OBJS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
