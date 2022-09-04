package nl.shadowlink.tools.shadowlib.ipl;

import com.nikhaldimann.inieditor.IniEditor;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector3D;

/**
 * @author Shadow-Link
 */
public class Item_CULL extends IPL_Item {
    private int gameType;
    public Vector3D posLowerLeft;
    public Vector3D posUpperRight;
    public int unk1, unk2, unk3, unk4;
    public long hash;
    public String name;

    Item_CULL(int gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        // Message.displayMsgHigh(line);
    }

    @Override
    public void read(ReadFunctions rf) {
        posLowerLeft = rf.readVector3D();
        posUpperRight = rf.readVector3D();
        unk1 = rf.readInt();
        unk2 = rf.readInt();
        unk3 = rf.readInt();
        unk4 = rf.readInt();
        hash = rf.readInt();
        // display();
    }

    private void display() {
        System.out.println("Position: " + posLowerLeft.x + ", " + posLowerLeft.y + ", " + posLowerLeft.z);
        System.out.println("Rotation: " + posUpperRight.x + ", " + posUpperRight.y + ", " + posUpperRight.z);
        System.out.println("Hash: " + hash);// + " name: " + name);
        System.out.println("Unknown1: " + unk1);
        System.out.println("Unknown2: " + unk2);
        System.out.println("Unknown3: " + unk3);
        System.out.println("Unknown4: " + unk4);
        System.out.println("Name: " + name);
    }

    @Override
    public void read(ReadFunctions rf, IniEditor ini) {
        posLowerLeft = rf.readVector3D();
        posUpperRight = rf.readVector3D();
        unk1 = rf.readInt();
        unk2 = rf.readInt();
        unk3 = rf.readInt();
        unk4 = rf.readInt();

        long tempHash = rf.readUnsignedInt();
        name = "" + tempHash;
        hash = (int) tempHash;
        name = ini.get("Hashes", name); // temp

        display();
    }

}
