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
public class Item_CARS extends IDE_Item{
    private int gameType;
    public String modelName;
    public String textureName;
    public String type;
    public String handlingID;

    public Item_CARS(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        line = line.replaceAll("\t", "");
        line = line.replaceAll(" ", "");
        System.out.println("Car: " + line);
        String[] split = line.split(",");
        modelName = split[0];
        textureName = split[1];
        type = split[2];
        handlingID = split[3];
        System.out.println("Model Name: " + split[0]);
        System.out.println("Texture Name: " + split[1]);
        System.out.println("Type: " + split[2]);
        System.out.println("HandLingID: " + split[3]);
        System.out.println("Game Name: " + split[4]);
        System.out.println("Anims: " + split[5]);
        System.out.println("Anims2: " + split[6]);
        System.out.println("Frq: " + split[7]);
        System.out.println("MaxNum: " + split[8]);
        System.out.println("Wheel Radius Front: " + split[9]);
        System.out.println("Wheel Radius Rear: " + split[10]);
        System.out.println("DefDirtLevel: " + split[11]);
        System.out.println("Swankness: " + split[12]);
        System.out.println("lodMult: " + split[13]);
        System.out.println("Flags: " + split[14]);
        //System.out.println("Extra stuff?: " + split[15]);
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
