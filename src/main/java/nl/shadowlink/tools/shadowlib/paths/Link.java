package nl.shadowlink.tools.shadowlib.paths;

import nl.shadowlink.tools.io.ReadFunctions;

/**
 * @author Shadow-Link
 */
public class Link {

    public Link(ReadFunctions rf) {
        int areaID = rf.readShort();
        int nodeID = rf.readShort();
        byte unknown = rf.readByte();
        byte linkLength = rf.readByte();
        int flags = rf.readShort();
        System.out.println("-----------Link-------------");
        System.out.println("AreaID: " + areaID);
        System.out.println("NodeID: " + nodeID);
        System.out.println("unknown: " + unknown);
        System.out.println("LinkLength: " + linkLength);
        System.out.println("Flags: " + flags);
    }

}
