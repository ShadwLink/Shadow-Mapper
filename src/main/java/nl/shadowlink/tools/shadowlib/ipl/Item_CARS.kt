package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
class Item_CARS(private val gameType: GameType) : IplItem() {
    var selected = false
    var position = Vector3D()
    var rotation = Vector3D()
    var hash = 0
    var name: String? = null
    var unknown1 = 0
    var unknown2 = 0
    var unknown3 = 0
    var unknown4 = 0
    var unknown5 = 0
    var unknown6 = 0
    var unknown7 = 0
    var type = 0
    override fun read(line: String) {
        // Message.displayMsgHigh(line);
    }

    override fun read(rf: ReadFunctions) {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun read(rf: ReadFunctions, hashTable: HashTable) {
        position = rf.readVector3D()
        rotation = rf.readVector3D()
        val tempHash = rf.readUnsignedInt()
        name = "" + tempHash
        hash = tempHash.toInt()
        // TODO: Fix this?
//        if (ini.hasOption("Cars", name)) {
//            name = ini.get("Cars", name); // temp
//        } else {
//            name = "";
//        }
        unknown1 = rf.readInt()
        unknown2 = rf.readInt()
        unknown3 = rf.readInt()
        unknown4 = rf.readInt()
        unknown5 = rf.readInt()
        unknown6 = rf.readInt()
        unknown7 = rf.readInt()
        //		display();
    }

    private fun display() {
        println("--CAR--")
        println("Position: " + position.x + ", " + position.y + ", " + position.z)
        println("Rotation: " + rotation.x + ", " + rotation.y + ", " + rotation.z)
        println("Hash: $hash name: $name")
        println("Unknown1: $unknown1")
        println("Unknown2: $unknown2")
        println("Unknown3: $unknown3")
        println("Unknown4: $unknown4")
        println("Unknown5: $unknown5")
        println("Unknown6: $unknown6")
        println("Unknown7: $unknown7")
    }

    fun write(wf: WriteFunctions) {
        wf.write(position)
        wf.write(rotation)
        wf.write(hash)
        wf.write(unknown1)
        wf.write(unknown2)
        wf.write(unknown3)
        wf.write(unknown4)
        wf.write(unknown5)
        wf.write(unknown6)
        wf.write(unknown7)
    }
}