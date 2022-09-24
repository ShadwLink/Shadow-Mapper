package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
class Item_GRGE internal constructor(private val gameType: GameType) : IplItem() {
    var lowLeftPos: Vector3D? = null
    var lineX = 0f
    var lineY = 0f
    var topRightPos: Vector3D? = null
    var doorType = 0
    var garageType = 0
    var hash = 0
    var name: String? = null
    var unknown = 0
    override fun read(line: String) {
        // Message.displayMsgHigh(line);
    }

    override fun read(rf: ReadFunctions) {}
    override fun read(rf: ReadFunctions, hashTable: HashTable) {
        lowLeftPos = rf.readVector3D()
        lineX = rf.readFloat()
        lineY = rf.readFloat()
        topRightPos = rf.readVector3D()
        doorType = rf.readInt()
        garageType = rf.readInt()
        val tempHash = rf.readUnsignedInt()
        name = "" + tempHash
        hash = tempHash.toInt()
        name = hashTable!![tempHash]
        unknown = rf.readInt()
        // display();
    }

    private fun display() {
        println("LowLeftPos: " + lowLeftPos!!.x + ", " + lowLeftPos!!.y + ", " + lowLeftPos!!.z)
        println("Line x: $lineX y: $lineY")
        println("TopRightPos: " + topRightPos!!.x + ", " + topRightPos!!.y + ", " + topRightPos!!.z)
        println("Doortype: $doorType")
        println("Garagetype: $garageType")
        println("Hash: $hash name: $name")
        println("Unknown: $unknown")
    }

    fun write(wf: WriteFunctions) {
        wf.write(lowLeftPos!!)
        wf.write(lineX)
        wf.write(lineY)
        wf.write(topRightPos!!)
        wf.write(doorType)
        wf.write(garageType)
        wf.write(hash)
        wf.write(unknown)
        display()
    }
}