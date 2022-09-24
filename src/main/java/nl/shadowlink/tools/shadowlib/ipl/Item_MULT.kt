package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
class Item_MULT internal constructor(private val gameType: GameType) : IplItem() {
    override fun read(line: String) {
        // Message.displayMsgHigh(line);
    }

    override fun read(rf: ReadFunctions) {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun read(rf: ReadFunctions, hashTable: HashTable) {
        throw UnsupportedOperationException("Not supported yet.")
    }
}