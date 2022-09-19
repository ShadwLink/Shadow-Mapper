package nl.shadowlink.tools.shadowlib.utils.hashing

interface Hasher {

    fun hash(value: String): Long
}