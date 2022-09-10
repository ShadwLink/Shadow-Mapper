package nl.shadowlink.tools.shadowlib.paths

import nl.shadowlink.tools.io.ReadFunctions

/**
 * @author Shadow-Link
 */
class Nodes {
    private var nodeCount = 0
    private var carNodeCount = 0
    private var intersectionNodeCount = 0
    private var linkCount = 0

    init {
        val rf = ReadFunctions("E:/Nodes/nodes29.nod")
        readHeader(rf)
        for (i in 0 until nodeCount) {
            val node = Node(rf)
        }
        for (i in 0 until linkCount) {
            val link = Link(rf)
        }
    }

    private fun readHeader(rf: ReadFunctions) {
        nodeCount = rf.readInt()
        carNodeCount = rf.readInt()
        intersectionNodeCount = rf.readInt()
        linkCount = rf.readInt()
        println("Node Count: $nodeCount")
        println("Car Node Count: $carNodeCount")
        println("intersectionNode Count: $intersectionNodeCount")
        println("Link Count: $linkCount")
    }
}