

package nl.shadowlink.tools.shadowlib.ide;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shadow-Link
 */
public class Item_2DFX extends IDE_Item{
    private int gameType;

    public String name;

    public Item_2DFX(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        line = line.replace(" ", "");
        String split[] = line.split(",");
        name = split[0];
        // TODO: Implement other params
    }

    public void save(BufferedWriter output){
        try {
            String line = "";
            output.write(line + "\n");
            System.out.println("Line: " + line);
        } catch (IOException ex) {
            Logger.getLogger(Item_OBJS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
