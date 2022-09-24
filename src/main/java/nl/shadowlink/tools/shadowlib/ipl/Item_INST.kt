package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.io.Vector4D
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable
import nl.shadowlink.tools.shadowlib.utils.hashing.OneAtATimeHasher.Companion.getHashKey

/**
 * @author Shadow-Link
 */
class Item_INST(private val gameType: GameType) : IplItem() {
    var id = 0
    lateinit var name: String // III, VC, SA, IV(Hash)
    var hash = 0
    var interior = 0
    var position = Vector3D(0.0f, 0.0f, 0.0f)
    var scale = Vector3D(0.0f, 0.0f, 0.0f)
    var rotation = Vector4D(0.0f, 0.0f, 0.0f, 1.0f)
    var lod = 0
    var unknown1 = 0
    var unknown2 = 0
    var unknown3 = 0f
    var drawDistance = 300.0f // default value
    var axisAngle = Vector4D(0.0f, 0.0f, 0.0f, 0.0f)
    private val hidden = false
    var selected = false
    var glListID = 0
    override fun read(line: String) {
        var line = line
        line = line!!.replace(" ", "")
        val split = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        when (gameType) {
            GameType.GTA_SA -> {
                id = Integer.valueOf(split[0])
                name = split[1]
                interior = Integer.valueOf(split[2])
                position = Vector3D(
                    java.lang.Float.valueOf(split[3]), java.lang.Float.valueOf(split[4]), java.lang.Float.valueOf(
                        split[5]
                    )
                )
                rotation = Vector4D(
                    java.lang.Float.valueOf(split[6]), java.lang.Float.valueOf(split[7]), java.lang.Float.valueOf(
                        split[8]
                    ), java.lang.Float.valueOf(split[9])
                )
                lod = Integer.valueOf(split[10])
            }

            GameType.GTA_VC -> {
                id = Integer.valueOf(split[0])
                name = split[1]
                interior = Integer.valueOf(split[2])
                position = Vector3D(
                    java.lang.Float.valueOf(split[3]), java.lang.Float.valueOf(split[4]), java.lang.Float.valueOf(
                        split[5]
                    )
                )
                scale = Vector3D(
                    java.lang.Float.valueOf(split[6]), java.lang.Float.valueOf(split[7]), java.lang.Float.valueOf(
                        split[8]
                    )
                )
                rotation = Vector4D(
                    java.lang.Float.valueOf(split[9]), java.lang.Float.valueOf(split[10]), java.lang.Float.valueOf(
                        split[11]
                    ), java.lang.Float.valueOf(split[12])
                )
            }

            GameType.GTA_III -> {
                id = Integer.valueOf(split[0])
                name = split[1]
                position = Vector3D(
                    java.lang.Float.valueOf(split[2]), java.lang.Float.valueOf(split[3]), java.lang.Float.valueOf(
                        split[4]
                    )
                )
                scale = Vector3D(
                    java.lang.Float.valueOf(split[5]), java.lang.Float.valueOf(split[6]), java.lang.Float.valueOf(
                        split[7]
                    )
                )
                rotation = Vector4D(
                    java.lang.Float.valueOf(split[8]), java.lang.Float.valueOf(split[9]), java.lang.Float.valueOf(
                        split[10]
                    ), java.lang.Float.valueOf(split[11])
                )
            }

            else -> throw IllegalArgumentException("Unsupported GameType $gameType")
        }
        axisAngle = rotation.axisAngle
        display()
    }

    override fun read(rf: ReadFunctions, hashTable: HashTable) {
        position = rf.readVector3D()
        rotation = rf.readVector4D()
        val tempHash = rf.readUnsignedInt()
        name = "" + tempHash
        hash = tempHash.toInt()
        name = hashTable[tempHash]!!
        if (name == null) {
            name = "HASH($tempHash)"
            println("ERROR Hash bestaat niet '$tempHash'")
        }

        // Message.displayMsgLow("iName: " + name);
        unknown1 = rf.readInt()
        // System.out.println("Unknown1: " + unknown1);
        lod = rf.readInt()
        unknown2 = rf.readInt()
        // System.out.println("Unknown2: " + unknown2);
        unknown3 = rf.readFloat()
        // System.out.println("Unknown3: " + unknown3);
        // this.display();
    }

    fun display() {
        if (gameType !== GameType.GTA_IV) // Message.displayMsgHigh("ID: " + id);
            println("Name: $name")
        if (gameType === GameType.GTA_VC || gameType === GameType.GTA_SA) // Message.displayMsgHigh("Interior: " + interior);
            println("Position: " + position.x + ", " + position.y + ", " + position.z)
        if (gameType === GameType.GTA_III || gameType === GameType.GTA_VC) // Message.displayMsgHigh("Scale: " + scale.x + ", " + scale.y + ", " + scale.z);
            println("Rotation: " + rotation.x + ", " + rotation.y + ", " + rotation.z + ", " + rotation.w)
        println("Lod: $lod")
    }

    override fun read(rf: ReadFunctions) {
        throw UnsupportedOperationException("Not supported yet.")
    }

    fun write(wf: WriteFunctions) {
        wf.write(position)
        wf.write(rotation)
        if (hash == 0) {
            hash = getHashKey(name).toInt()
        }
        wf.write(hash)
        wf.write(unknown1)
        wf.write(lod)
        wf.write(unknown2)
        wf.write(unknown3)
        display()
    }
}