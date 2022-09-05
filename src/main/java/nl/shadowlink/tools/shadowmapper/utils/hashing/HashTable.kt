package nl.shadowlink.tools.shadowmapper.utils.hashing

class HashTable(
    private val hasher: Hasher
) {

    private val hashes = mutableMapOf<Long, String>()

    fun add(value: String) {
        hashes[hasher.hash(value)] = value
    }

    fun add(hash: Long, value: String) {
        hashes[hash] = value
    }

    operator fun get(hash: Long): String? {
        return hashes[hash]
    }
}