package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
class Item_LCUL internal constructor(private val gameType: GameType) : IplItem() {
    var posLowerLeft: Vector3D? = null
    var posUpperRight: Vector3D? = null
    var unk1 = 0
    var hash1: Long = 0
    var hash2: Long = 0
    var hash3: Long = 0
    var hash4: Long = 0
    var hash5: Long = 0
    var hash6: Long = 0
    var hash7: Long = 0
    var hash8: Long = 0
    var hash9: Long = 0
    var hash10: Long = 0
    var name1: String? = null
    var name2: String? = null
    var name3: String? = null
    var name4: String? = null
    var name5: String? = null
    var name6: String? = null
    var name7: String? = null
    var name8: String? = null
    var name9: String? = null
    var name10: String? = null
    override fun read(line: String) {
        // Message.displayMsgHigh(line);
    }

    override fun read(rf: ReadFunctions) {
        posLowerLeft = rf.readVector3D()
        posUpperRight = rf.readVector3D()
        unk1 = rf.readInt()
        hash1 = rf.readUnsignedInt()
        hash2 = rf.readUnsignedInt()
        hash3 = rf.readUnsignedInt()
        hash4 = rf.readUnsignedInt()
        hash5 = rf.readUnsignedInt()
        hash6 = rf.readUnsignedInt()
        hash7 = rf.readUnsignedInt()
        hash8 = rf.readUnsignedInt()
        hash9 = rf.readUnsignedInt()
        hash10 = rf.readUnsignedInt()
        name1 = rf.readString(32)
        name2 = rf.readString(32)
        name3 = rf.readString(32)
        name4 = rf.readString(32)
        name5 = rf.readString(32)
        name6 = rf.readString(32)
        name7 = rf.readString(32)
        name8 = rf.readString(32)
        name9 = rf.readString(32)
        name10 = rf.readString(32)
        // display();
    }

    private fun display() {
        println("LowerLeft: " + posLowerLeft!!.x + ", " + posLowerLeft!!.y + ", " + posLowerLeft!!.z)
        println("UpperRight: " + posUpperRight!!.x + ", " + posUpperRight!!.y + ", " + posUpperRight!!.z)
        println("Hash1: $hash1 name: $name1")
        println("Hash2: $hash2 name: $name2")
        println("Hash3: $hash3 name: $name3")
        println("Hash4: $hash4 name: $name4")
        println("Hash5: $hash5 name: $name5")
        println("Hash6: $hash6 name: $name6")
        println("Hash7: $hash7 name: $name7")
        println("Hash8: $hash8 name: $name8")
        println("Hash9: $hash9 name: $name9")
        println("Hash10: $hash10 name: $name10")
    }

    override fun read(rf: ReadFunctions, hashTable: HashTable) {
        posLowerLeft = rf.readVector3D()
        posUpperRight = rf.readVector3D()
        unk1 = rf.readInt()
        hash1 = rf.readUnsignedInt()
        hash2 = rf.readUnsignedInt()
        hash3 = rf.readUnsignedInt()
        hash4 = rf.readUnsignedInt()
        hash5 = rf.readUnsignedInt()
        hash6 = rf.readUnsignedInt()
        hash7 = rf.readUnsignedInt()
        hash8 = rf.readUnsignedInt()
        hash9 = rf.readUnsignedInt()
        hash10 = rf.readUnsignedInt()
        name1 = rf.readString(32)
        name2 = rf.readString(32)
        name3 = rf.readString(32)
        name4 = rf.readString(32)
        name5 = rf.readString(32)
        name6 = rf.readString(32)
        name7 = rf.readString(32)
        name8 = rf.readString(32)
        name9 = rf.readString(32)
        name10 = rf.readString(32)
        // display();
    }
}