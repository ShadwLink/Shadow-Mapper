package nl.shadowlink.tools.shadowmapper.utils.hashing

class OneAtATimeHasher: Hasher {

    override fun hash(value: String): Long {
        var hash = 0

        for (i in value.indices) {
            hash += value[i].code and 0xFF
            hash += hash shl 10
            hash = hash xor (hash ushr 6)
        }
        hash += hash shl 3
        hash = hash xor (hash ushr 11)
        hash += hash shl 15
        return hash.toLong() and 0xffffffffL
    }
}