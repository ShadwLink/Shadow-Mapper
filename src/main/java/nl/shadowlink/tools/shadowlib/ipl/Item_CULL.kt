package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
class Item_CULL internal constructor(private val gameType: GameType) : IplItem() {
    var posLowerLeft: Vector3D? = null
    var posUpperRight: Vector3D? = null
    var unk1 = 0
    var unk2 = 0
    var unk3 = 0
    var unk4 = 0
    var hash: Long = 0
    var name: String? = null
    override fun read(line: String) {
        // Message.displayMsgHigh(line);
    }

    override fun read(rf: ReadFunctions) {
        posLowerLeft = rf.readVector3D()
        posUpperRight = rf.readVector3D()
        unk1 = rf.readInt()
        unk2 = rf.readInt()
        unk3 = rf.readInt()
        unk4 = rf.readInt()
        hash = rf.readInt().toLong()
        // display();
    }

    private fun display() {
        println("Position: " + posLowerLeft!!.x + ", " + posLowerLeft!!.y + ", " + posLowerLeft!!.z)
        println("Rotation: " + posUpperRight!!.x + ", " + posUpperRight!!.y + ", " + posUpperRight!!.z)
        println("Hash: $hash") // + " name: " + name);
        println("Unknown1: $unk1")
        println("Unknown2: $unk2")
        println("Unknown3: $unk3")
        println("Unknown4: $unk4")
        println("Name: $name")
    }

    override fun read(rf: ReadFunctions, hashTable: HashTable) {
        posLowerLeft = rf.readVector3D()
        posUpperRight = rf.readVector3D()
        unk1 = rf.readInt()
        unk2 = rf.readInt()
        unk3 = rf.readInt()
        unk4 = rf.readInt()
        val tempHash = rf.readUnsignedInt()
        name = "" + tempHash
        hash = tempHash.toInt().toLong()
        name = hashTable!![tempHash]
        display()
    }
}