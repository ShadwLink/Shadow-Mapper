/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Paths;

import file_io.ReadFunctions;

/**
 *
 * @author Kilian
 */
public class Link {

    public Link(ReadFunctions rf){
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
