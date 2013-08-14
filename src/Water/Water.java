/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Water;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kilian
 */
public class Water {
    public String fileName;
    public int gameType;

    public ArrayList<WaterPlane> planes = new ArrayList();

    private FileReader fileReader;
    private BufferedReader input;

    public Water(String fileName, int gameType){
        this.fileName = fileName;
        this.gameType = gameType;
        Read();
    }

    public void Read(){
        if(openWater()){
            try {
                //System.out.println("Opened water.dat");
                String line;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                    WaterPlane wp = new WaterPlane(line);
                    planes.add(wp);
                }
            } catch (IOException ex) {
                Logger.getLogger(Water.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        closeWater();
    }

    public void Write(){

    }

    public boolean openWater(){
        try {
            fileReader = new FileReader(fileName);
            input =  new BufferedReader(fileReader);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean closeWater(){
        try {
            input.close();
            fileReader.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

}
