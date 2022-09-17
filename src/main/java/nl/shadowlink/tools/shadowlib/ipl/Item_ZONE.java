package nl.shadowlink.tools.shadowlib.ipl;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector3D;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class Item_ZONE extends IplItem {
    private GameType gameType;

    public Vector3D posLowerLeft;
    public Vector3D posUpperRight;

    public Item_ZONE(GameType gameType) {
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
        // display();
    }

    private void display() {
        System.out.println("LowerLeft: " + posLowerLeft.x + ", " + posLowerLeft.y + ", " + posLowerLeft.z);
        System.out.println("UpperRight: " + posUpperRight.x + ", " + posUpperRight.y + ", " + posUpperRight.z);
    }

    @Override
    public void read(ReadFunctions rf, HashTable hashTable) {
        Logger.getLogger("IPL").log(Level.INFO, getClass().getSimpleName() + " not supported yet.");
    }

}
