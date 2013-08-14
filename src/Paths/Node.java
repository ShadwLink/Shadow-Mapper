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
public class Node {

    public Node(ReadFunctions rf){
        int memAdress = rf.readInt();
        int zero = rf.readInt();
        int areaID = rf.readShort();
        int nodeID = rf.readShort();
        int unknown = rf.readInt();
        int always7FFE = rf.readShort();
        int linkID = rf.readShort();
        int posX = rf.readShort();
        int posY = rf.readShort();
        int posZ = rf.readShort();
        byte pathWidth = rf.readByte();
        byte pathType = rf.readByte();
        int flags = rf.readInt();
        System.out.println("--------------Node---------------");
        System.out.println("Mem Adress: " + memAdress);
        System.out.println("Zero: " + zero);
        System.out.println("AreaID: " + areaID);
        System.out.println("NodeID: " + nodeID);
        System.out.println("unknown: " + unknown);
        System.out.println("Always7FFE: " + always7FFE);
        System.out.println("LinkID: " + linkID);
        System.out.println("PosX: " + (float)(posX/8));
        System.out.println("PosY: " + (float)(posY/8));
        System.out.println("PosZ: " + (float)(posZ/128));
        System.out.println("PathWidth: " + pathWidth);
        System.out.println("PathType: " + pathType);
        System.out.println("Flags: " + flags);
    }


}
