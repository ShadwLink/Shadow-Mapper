/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IPL;

import com.nikhaldimann.inieditor.IniEditor;
import file_io.Message;
import file_io.ReadFunctions;
import file_io.Vector3D;

/**
 *
 * @author Kilian
 */
public class Item_ZONE extends IPL_Item{
    private int gameType;

    public Vector3D posLowerLeft;
    public Vector3D posUpperRight;

    public Item_ZONE(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        Message.displayMsgHigh(line);
    }

    @Override
    public void read(ReadFunctions rf) {
        posLowerLeft = rf.readVector3D();
        posUpperRight = rf.readVector3D();
        //display();
    }

    private void display(){
        System.out.println("LowerLeft: " + posLowerLeft.x + ", " + posLowerLeft.y + ", " + posLowerLeft.z);
        System.out.println("UpperRight: " + posUpperRight.x + ", " + posUpperRight.y + ", " + posUpperRight.z);
    }

    @Override
    public void read(ReadFunctions rf, IniEditor ini) {
        System.out.println("Not supported yet.");
    }

}
