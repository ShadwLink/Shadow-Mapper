package nl.shadowlink.tools.io

import okio.BufferedSource

fun BufferedSource.readNullTerminatedString(): String {
    val sb = StringBuilder()
    var b = readByte()
    while (b.toInt() != 0) {
        sb.append(Char(b.toUShort()))
        b = readByte()
    }
    return sb.toString()
}