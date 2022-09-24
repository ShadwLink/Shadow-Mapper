package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class Item_ZONE(private val gameType: GameType) : IplItem() {
    var posLowerLeft: Vector3D? = null
    var posUpperRight: Vector3D? = null
    override fun read(line: String) {
        // Message.displayMsgHigh(line);
    }

    override fun read(rf: ReadFunctions) {
        posLowerLeft = rf!!.readVector3D()
        posUpperRight = rf.readVector3D()
        // display();
    }

    private fun display() {
        println("LowerLeft: " + posLowerLeft!!.x + ", " + posLowerLeft!!.y + ", " + posLowerLeft!!.z)
        println("UpperRight: " + posUpperRight!!.x + ", " + posUpperRight!!.y + ", " + posUpperRight!!.z)
    }

    override fun read(rf: ReadFunctions, hashTable: HashTable) {
        Logger.getLogger("IPL").log(Level.INFO, javaClass.simpleName + " not supported yet.")
    }
}