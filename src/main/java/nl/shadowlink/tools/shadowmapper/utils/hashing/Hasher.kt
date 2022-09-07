package nl.shadowlink.tools.shadowmapper.utils.hashing

interface Hasher {

    fun hash(value: String): Long
}