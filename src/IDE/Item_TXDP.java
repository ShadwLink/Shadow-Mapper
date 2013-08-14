/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IDE;

import file_io.Message;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kilian
 */
public class Item_TXDP extends IDE_Item{
    private int gameType;
    public String texDic;
    public String texDicParent;

    public Item_TXDP(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        line = line.replace(" ", "");
        String split[] = line.split(",");
        texDic = split[0];
        texDicParent = split[1];
        System.out.println("<TXDP>" + line);
    }

    public void save(BufferedWriter output){
        try {
            String line = texDic;
            line += ", " + texDicParent;
            output.write(line + "\n");
            System.out.println("Line: " + line);
        } catch (IOException ex) {
            Logger.getLogger(Item_OBJS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
