package nl.shadowlink.tools.shadowlib.ipl;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector3D;
import nl.shadowlink.tools.io.Vector4D;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable;

/**
 * @author Shadow-Link
 */
public class Item_STRBIG extends IplItem {
    private GameType gameType;

    public String modelName;
    public int unk1, unk2, unk3;
    public Vector3D pos;
    public Vector4D rot;

    Item_STRBIG(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void read(String line) {
        // Message.displayMsgHigh(line);
    }

    @Override
    public void read(ReadFunctions rf) {
        modelName = rf.readString(24);
        unk1 = rf.readInt();
        unk2 = rf.readInt();
        unk3 = rf.readInt();
        pos = rf.readVector3D();
        rot = rf.readVector4D();
        // display();
    }

    private void display() {
        System.out.println("ModelName: " + modelName);
        System.out.println("Unk1: " + unk1);
        System.out.println("Unk2: " + unk2);
        System.out.println("Unk3: " + unk3);
        System.out.println("Position: " + pos.x + ", " + pos.y + ", " + pos.z);
        System.out.println("Rotation: " + rot.x + ", " + rot.y + ", " + rot.z + ", " + rot.w);
    }

    @Override
    public void read(ReadFunctions rf, HashTable hashTable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
