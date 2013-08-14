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
public class Nodes {
    private int nodeCount;
    private int carNodeCount;
    private int intersectionNodeCount;
    private int linkCount;

    public Nodes(){
        ReadFunctions rf = new ReadFunctions();
        rf.openFile("E:/Nodes/nodes29.nod");

        readHeader(rf);
        for(int i = 0; i < nodeCount; i++){
            Node node = new Node(rf);
        }
        for(int i = 0; i < linkCount; i++){
            Link link = new Link(rf);
        }
    }

    private void readHeader(ReadFunctions rf){
        nodeCount = rf.readInt();
        carNodeCount = rf.readInt();
        intersectionNodeCount = rf.readInt();
        linkCount = rf.readInt();
        System.out.println("Node Count: " + nodeCount);
        System.out.println("Car Node Count: " + carNodeCount);
        System.out.println("intersectionNode Count: " + intersectionNodeCount);
        System.out.println("Link Count: " + linkCount);
    }

}
