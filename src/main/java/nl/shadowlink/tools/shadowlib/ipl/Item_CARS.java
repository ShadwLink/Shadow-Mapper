package nl.shadowlink.tools.shadowlib.ipl;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector3D;
import nl.shadowlink.tools.io.WriteFunctions;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable;

/**
 * @author Shadow-Link
 */
public class Item_CARS extends IplItem {
    private GameType gameType;
    public boolean selected = false;

    public Vector3D position = new Vector3D();
    public Vector3D rotation = new Vector3D();
    public int hash;
    public String name;
    public int unknown1, unknown2, unknown3, unknown4, unknown5, unknown6, unknown7;
    public int type = 0;

    public Item_CARS(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        // Message.displayMsgHigh(line);
    }

    @Override
    public void read(ReadFunctions rf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(ReadFunctions rf, HashTable hashTable) {
        position = rf.readVector3D();
        rotation = rf.readVector3D();
        long tempHash = rf.readUnsignedInt();
        name = "" + tempHash;
        hash = (int) tempHash;
        // TODO: Fix this?
//        if (ini.hasOption("Cars", name)) {
//            name = ini.get("Cars", name); // temp
//        } else {
//            name = "";
//        }
        unknown1 = rf.readInt();
        unknown2 = rf.readInt();
        unknown3 = rf.readInt();
        unknown4 = rf.readInt();
        unknown5 = rf.readInt();
        unknown6 = rf.readInt();
        unknown7 = rf.readInt();
//		display();
    }

    private void display() {
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
        wf.write(position);
        wf.write(rotation);
        wf.write(hash);
        wf.write(unknown1);
        wf.write(unknown2);
        wf.write(unknown3);
        wf.write(unknown4);
        wf.write(unknown5);
        wf.write(unknown6);
        wf.write(unknown7);
    }

}
