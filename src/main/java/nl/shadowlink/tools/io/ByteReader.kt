package nl.shadowlink.tools.io

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer

/**
 * @author Shadow-Link
 */
class ByteReader(
    private val stream: ByteArray,
    private var currentOffset: Int
) {

    private var system = true
    private var sysSize = 0
    fun readUInt32(): Int {
        var i = 0
        val len = 4
        var cnt = 0
        val tmp = ByteArray(len)
        i = currentOffset
        while (i < currentOffset + len) {
            tmp[cnt] = stream[i]
            cnt++
            i++
        }
        var accum: Long = 0
        i = 0
        var shiftBy = 0
        while (shiftBy < 32) {
            accum = accum or ((tmp[i].toInt() and 0xff).toLong() shl shiftBy)
            i++
            shiftBy += 8
        }
        currentOffset += 4
        return accum.toInt()
    }

    fun readOffset(): Int {
        val value: Int
        val offset = readUInt32()
        value = if (offset == 0) {
            0
        } else {
            if (offset shr 28 != 5) {
                throw IllegalStateException("Expected an offset")
            } else {
                offset and 0x0fffffff
            }
        }
        return value
    }

    fun readVector4D(): Vector4D {
        val x = readFloat()
        val y = readFloat()
        val z = readFloat()
        val w = readFloat()
        return Vector4D(x, y, z, w)
    }

    fun readFloat(): Float {
        var accum = 0
        var i = 0
        var shiftBy = 0
        while (shiftBy < 32) {
            accum = (accum.toLong() or ((stream[currentOffset + i].toInt() and 0xff).toLong() shl shiftBy)).toInt()
            i++
            shiftBy += 8
        }
        currentOffset += 4
        return java.lang.Float.intBitsToFloat(accum)
    }

    fun readUInt16(): Int {
        val low = stream[currentOffset].toInt() and 0xff
        val high = stream[currentOffset + 1].toInt() and 0xff
        currentOffset += 2
        return (high shl 8 or low)
    }

    fun readInt16(): Short {
        val ret = (stream[currentOffset + 1].toShort().toInt() shl 8 or (stream[currentOffset].toShort()
            .toInt() and 0xff)).toShort()
        currentOffset += 2
        return ret
    }

    fun readDataOffset(): Int {
        val value: Int
        val offset = readUInt32()
        value = if (offset == 0) {
            0
        } else {
            if (offset shr 28 != 6) {
            }
            offset and 0x0fffffff
        }
        return value
    }

    /**
     * Returns a String from the DataInputStream with the given size till 0
     *
     * @param data_in
     * @param size
     * @return a String of the size size
     */
    fun readNullTerminatedString(size: Int): String {
        var woord = ""
        var gotNull = false
        for (i in 0 until size) {
            val b = readByte()
            if (!gotNull) {
                if (b.toInt() != 0) woord += Char(b.toUShort()) else gotNull = true
            }
        }
        return woord
    }

    fun readNullTerminatedString(): String {
        var sb = ""
        var c = Char(stream[currentOffset].toUShort())
        while (c.code != 0) {
            // nl.shadowlink.tools.io.Message.displayMsgHigh("String: " + sb + " c: " + c);
            sb = sb + c
            currentOffset++
            c = Char(stream[currentOffset].toUShort())
        }
        return sb
    }

    fun readString(length: Int): String {
        var sb = ""
        for (i in 0 until length) {
            val c = Char(stream[currentOffset].toUShort())
            sb += c
            currentOffset++
        }
        return sb
    }

    fun toArray(bytes: Int): ByteArray {
        val arr = ByteArray(bytes)
        for (i in 0 until bytes) {
            arr[i] = stream[currentOffset]
            currentOffset++
        }
        return arr
    }

    fun toArray(): ByteArray {
        return stream
    }

    fun toArray(start: Int, end: Int): ByteArray {
        val retSize = end - start
        val retStream = ByteArray(retSize)
        setCurrentOffset(start)
        for (i in 0 until retSize) {
            retStream[i] = stream[currentOffset]
            currentOffset++
        }
        return retStream
    }

    fun readByte(): Byte {
        currentOffset++
        return stream[currentOffset - 1]
    }

    fun getCurrentOffset(): Int {
        return currentOffset
    }

    fun setCurrentOffset(offset: Int) {
        currentOffset = offset
        if (!system) {
            currentOffset += sysSize
        }
    }

    fun setSysSize(size: Int) {
        sysSize = size
    }

    fun setSystemMemory(system: Boolean) {
        this.system = system
    }

    fun getByteBuffer(size: Int): ByteBuffer {
        val buffer = ByteArray(size)
        val bbuf = ByteBuffer.allocate(size)
        for (i in 0 until size) {
            buffer[i] = readByte()
        }
        bbuf.put(buffer)
        bbuf.rewind()
        return bbuf
    }

    fun readBytes(pCount: Int): ByteArray {
        val buffer = ByteArray(pCount)
        for (i in 0 until pCount) {
            buffer[i] = readByte()
        }
        return buffer
    }

    val inputStream: ByteArrayInputStream
        get() = ByteArrayInputStream(stream, currentOffset, stream.size - currentOffset)

    fun skipBytes(bytes: Int) {
        currentOffset += bytes
    }

    /**
     * returns if a certain flag is on
     *
     * @param flags
     * @param flag
     * @return if a flag has been set
     */
    fun hasFlag(flags: Int, flag: Int): Boolean {
        return flags and flag == flag
    }

    fun moreToRead(): Int {
        return stream.size - currentOffset
    }

    fun unsignedInt(): Long {
        val i = readUInt32()
        return i.toLong() and 0xffffffffL
    }
}