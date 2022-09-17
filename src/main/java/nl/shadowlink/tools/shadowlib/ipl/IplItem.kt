package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
abstract class IplItem {
    abstract fun read(line: String?)
    abstract fun read(rf: ReadFunctions?)
    abstract fun read(rf: ReadFunctions?, hashTable: HashTable?)
}