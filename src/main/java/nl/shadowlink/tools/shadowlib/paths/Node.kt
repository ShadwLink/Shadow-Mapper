package nl.shadowlink.tools.shadowlib.paths

import nl.shadowlink.tools.io.ReadFunctions

/**
 * @author Shadow-Link
 */
class Node(rf: ReadFunctions) {
    val memAdress = rf.readInt()
    val zero = rf.readInt()
    val areaID = rf.readShort()
    val nodeID = rf.readShort()
    val unknown = rf.readInt()
    val always7FFE = rf.readShort()
    val linkID = rf.readShort()
    val posX = rf.readShort()
    val posY = rf.readShort()
    val posZ = rf.readShort()
    val pathWidth = rf.readByte()
    val pathType = rf.readByte()
    val flags = rf.readInt()

    override fun toString(): String {
        return StringBuilder("--------------Node---------------")
            .append("Mem Adress: $memAdress")
            .append("Zero: $zero")
            .append("AreaID: $areaID")
            .append("NodeID: $nodeID")
            .append("unknown: $unknown")
            .append("Always7FFE: $always7FFE")
            .append("LinkID: $linkID")
            .append("PosX: " + (posX / 8).toFloat())
            .append("PosY: " + (posY / 8).toFloat())
            .append("PosZ: " + (posZ / 128).toFloat())
            .append("PathWidth: $pathWidth")
            .append("PathType: $pathType")
            .append("Flags: $flags")
            .toString()
    }
}