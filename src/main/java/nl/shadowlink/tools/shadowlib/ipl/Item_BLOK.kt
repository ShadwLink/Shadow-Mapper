package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class Item_BLOK internal constructor(private val gameType: GameType) : IplItem() {
    override fun read(line: String) {
        //Message.displayMsgHigh(line);
    }

    override fun read(rf: ReadFunctions) {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun read(rf: ReadFunctions, hashTable: HashTable) {
        Logger.getLogger("IPL").log(Level.INFO, javaClass.simpleName + " not supported yet.")
    }
}