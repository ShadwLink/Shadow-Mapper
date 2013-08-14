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
public class Item_LCUL extends IPL_Item{
    private int gameType;

    public Vector3D posLowerLeft;
    public Vector3D posUpperRight;
    public int unk1;
    public long hash1, hash2, hash3, hash4, hash5;
    public long hash6, hash7, hash8, hash9, hash10;
    public String name1, name2, name3, name4, name5;
    public String name6, name7, name8, name9, name10;


    Item_LCUL(int gameType) {
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
        unk1 = rf.readInt();
        hash1 = rf.readUnsignedInt();
        hash2 = rf.readUnsignedInt();
        hash3 = rf.readUnsignedInt();
        hash4 = rf.readUnsignedInt();
        hash5 = rf.readUnsignedInt();
        hash6 = rf.readUnsignedInt();
        hash7 = rf.readUnsignedInt();
        hash8 = rf.readUnsignedInt();
        hash9 = rf.readUnsignedInt();
        hash10 = rf.readUnsignedInt();
        name1 = rf.readString(32);
        name2 = rf.readString(32);
        name3 = rf.readString(32);
        name4 = rf.readString(32);
        name5 = rf.readString(32);
        name6 = rf.readString(32);
        name7 = rf.readString(32);
        name8 = rf.readString(32);
        name9 = rf.readString(32);
        name10 = rf.readString(32);
        //display();
    }

    private void display(){
        System.out.println("LowerLeft: " + posLowerLeft.x + ", " + posLowerLeft.y + ", " + posLowerLeft.z);
        System.out.println("UpperRight: " + posUpperRight.x + ", " + posUpperRight.y + ", " + posUpperRight.z);
        System.out.println("Hash1: " + hash1 + " name: " + name1);
        System.out.println("Hash2: " + hash2 + " name: " + name2);
        System.out.println("Hash3: " + hash3 + " name: " + name3);
        System.out.println("Hash4: " + hash4 + " name: " + name4);
        System.out.println("Hash5: " + hash5 + " name: " + name5);
        System.out.println("Hash6: " + hash6 + " name: " + name6);
        System.out.println("Hash7: " + hash7 + " name: " + name7);
        System.out.println("Hash8: " + hash8 + " name: " + name8);
        System.out.println("Hash9: " + hash9 + " name: " + name9);
        System.out.println("Hash10: " + hash10 + " name: " + name10);
    }

    @Override
    public void read(ReadFunctions rf, IniEditor ini) {
        posLowerLeft = rf.readVector3D();
        posUpperRight = rf.readVector3D();
        unk1 = rf.readInt();
        hash1 = rf.readUnsignedInt();
        hash2 = rf.readUnsignedInt();
        hash3 = rf.readUnsignedInt();
        hash4 = rf.readUnsignedInt();
        hash5 = rf.readUnsignedInt();
        hash6 = rf.readUnsignedInt();
        hash7 = rf.readUnsignedInt();
        hash8 = rf.readUnsignedInt();
        hash9 = rf.readUnsignedInt();
        hash10 = rf.readUnsignedInt();
        name1 = rf.readString(32);
        name2 = rf.readString(32);
        name3 = rf.readString(32);
        name4 = rf.readString(32);
        name5 = rf.readString(32);
        name6 = rf.readString(32);
        name7 = rf.readString(32);
        name8 = rf.readString(32);
        name9 = rf.readString(32);
        name10 = rf.readString(32);
        //display();
    }

}
