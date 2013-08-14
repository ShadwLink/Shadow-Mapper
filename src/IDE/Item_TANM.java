/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IDE;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kilian
 */
public class Item_TANM extends IDE_Item{
    private int gameType;

    public Item_TANM(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        System.out.println("Not supported yet.");
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
