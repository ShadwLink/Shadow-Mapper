package nl.shadowlink.tools.shadowlib.paths

import nl.shadowlink.tools.io.ReadFunctions

/**
 * @author Shadow-Link
 */
class Link(rf: ReadFunctions) {
    val areaID = rf.readShort()
    val nodeID = rf.readShort()
    val unknown = rf.readByte()
    val linkLength = rf.readByte()
    val flags = rf.readShort()

    override fun toString(): String {
        return StringBuilder("-----------Link-------------")
            .append("AreaID: $areaID")
            .append("NodeID: $nodeID")
            .append("unknown: $unknown")
            .append("LinkLength: $linkLength")
            .append("Flags: $flags")
            .toString()
    }
}