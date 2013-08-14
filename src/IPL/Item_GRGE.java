/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package IPL;

import com.nikhaldimann.inieditor.IniEditor;
import file_io.Message;
import file_io.ReadFunctions;
import file_io.Vector3D;
import file_io.WriteFunctions;

/**
 *
 * @author Kilian
 */
public class Item_GRGE extends IPL_Item{
    private int gameType;

    public Vector3D lowLeftPos;
    public float lineX, lineY;
    public Vector3D topRightPos;
    public int doorType;
    public int garageType;
    public int hash;
    public String name;
    public int unknown;

    Item_GRGE(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        Message.displayMsgHigh(line);
    }

    @Override
    public void read(ReadFunctions rf) {

    }

    @Override
    public void read(ReadFunctions rf, IniEditor ini) {
        lowLeftPos = rf.readVector3D();
        lineX = rf.readFloat();
        lineY = rf.readFloat();
        topRightPos = rf.readVector3D();
        doorType = rf.readInt();
        garageType = rf.readInt();
        long tempHash = rf.readUnsignedInt();
        name = "" + tempHash;
        hash = (int) tempHash;
        name = ini.get("Hashes", name); //temp
        unknown = rf.readInt();
        //display();
    }

    private void display(){
        System.out.println("LowLeftPos: " + lowLeftPos.x + ", " + lowLeftPos.y + ", " + lowLeftPos.z);
        System.out.println("Line x: " + lineX +" y: " + lineY);
        System.out.println("TopRightPos: " + topRightPos.x + ", " + topRightPos.y + ", " + topRightPos.z);
        System.out.println("Doortype: " + doorType);
        System.out.println("Garagetype: " + garageType);
        System.out.println("Hash: " + hash + " name: " + name);
        System.out.println("Unknown: " + unknown);
    }

    public void write(WriteFunctions wf) {
        wf.writeVector3D(lowLeftPos);
        wf.writeFloat(lineX);
        wf.writeFloat(lineY);
        wf.writeVector3D(topRightPos);
        wf.writeInt(doorType);
        wf.writeInt(garageType);
        wf.writeInt(hash);
        wf.writeInt(unknown);
        display();
    }

}
