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
public class Item_CARS extends IPL_Item{
    private int gameType;
    public boolean selected = false;

    public Vector3D position = new Vector3D();
    public Vector3D rotation = new Vector3D();
    public int hash;
    public String name;
    public int unknown1, unknown2, unknown3, unknown4, unknown5, unknown6, unknown7;
    public int type = 0;

    public Item_CARS(int gameType) {
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
        position = rf.readVector3D();
        rotation = rf.readVector3D();
        long tempHash = rf.readUnsignedInt();
        name = "" + tempHash;
        hash = (int) tempHash;
        if(ini.hasOption("Cars", name)){
            name = ini.get("Cars", name); //temp
        }else{
            name = "";
        }
        unknown1 = rf.readInt();
        unknown2 = rf.readInt();
        unknown3 = rf.readInt();
        unknown4 = rf.readInt();
        unknown5 = rf.readInt();
        unknown6 = rf.readInt();
        unknown7 = rf.readInt();
        display();
    }

    private void display(){
        System.out.println("--CAR--");
        System.out.println("Position: " + position.x + ", " + position.y + ", " + position.z);
        System.out.println("Rotation: " + rotation.x + ", " + rotation.y + ", " + rotation.z);
        System.out.println("Hash: " + hash + " name: " + name);
        System.out.println("Unknown1: " + unknown1);
        System.out.println("Unknown2: " + unknown2);
        System.out.println("Unknown3: " + unknown3);
        System.out.println("Unknown4: " + unknown4);
        System.out.println("Unknown5: " + unknown5);
        System.out.println("Unknown6: " + unknown6);
        System.out.println("Unknown7: " + unknown7);
    }

    public void write(WriteFunctions wf) {
        wf.writeVector3D(position);
        wf.writeVector3D(rotation);
        wf.writeInt(hash);
        wf.writeInt(unknown1);
        wf.writeInt(unknown2);
        wf.writeInt(unknown3);
        wf.writeInt(unknown4);
        wf.writeInt(unknown5);
        wf.writeInt(unknown6);
        wf.writeInt(unknown7);
    }

}
