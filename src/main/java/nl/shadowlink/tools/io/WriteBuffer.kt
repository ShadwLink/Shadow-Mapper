package nl.shadowlink.tools.io

import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * @author Shadow-Link
 */
class WriteBuffer {
    private val byteBuffer: ArrayList<Byte> = ArrayList()
    fun writeByte(waarde: Int): Int {
        byteBuffer.add(waarde.toByte())
        return 1
    }

    fun writeInt(waarde: Int): Int {
        val bytes = int2arr(waarde)
        for (i in bytes.indices) {
            writeByte(bytes[i].toInt())
        }
        return 4
    }

    fun writeVector(x: Float, y: Float, z: Float, w: Float): Int {
        writeFloat(x)
        writeFloat(y)
        writeFloat(z)
        if (w != -1f) {
            writeFloat(w)
        } else {
            writeInt(2139095041)
        }
        return 16
    }

    fun writeVector(vec: Vector3D): Int {
        writeFloat(vec.x)
        writeFloat(vec.y)
        writeFloat(vec.z)
        writeInt(2139095041)
        return 16
    }

    fun writeOffset(waarde: Int): Int {
        var sWaarde = waarde.toShort()
        val bbuf = ByteBuffer.allocate(2)
        bbuf.order(ByteOrder.BIG_ENDIAN)
        bbuf.putShort(sWaarde)
        bbuf.order(ByteOrder.LITTLE_ENDIAN)
        sWaarde = bbuf.getShort(0)
        writeShort(waarde)
        writeShort(20480)
        return 4
    }

    fun writeDataOffset(waarde: Int): Int {
        val bbuf = ByteBuffer.allocate(4)
        bbuf.order(ByteOrder.BIG_ENDIAN)
        bbuf.putInt(waarde)
        bbuf.order(ByteOrder.LITTLE_ENDIAN)
        val test1 = bbuf[0]
        val test2 = bbuf[1]
        val test3 = bbuf[2]
        writeByte(test1.toInt())
        writeByte(test3.toInt())
        writeByte(test2.toInt())
        writeByte(0x60)
        return 4
    }

    fun writeShort(waarde: Int): Int {
        var sWaarde = waarde.toShort()
        val bbuf = ByteBuffer.allocate(2)
        bbuf.order(ByteOrder.BIG_ENDIAN)
        bbuf.putShort(sWaarde)
        bbuf.order(ByteOrder.LITTLE_ENDIAN)
        sWaarde = bbuf.getShort(0)
        val bytes = short2arr(waarde)
        for (i in bytes.indices) {
            writeByte(bytes[i].toInt())
        }
        return 2
    }

    fun writeFloat(waarde: Float): Int {
        val bytes = float2arr(waarde)
        for (i in bytes.indices) {
            writeByte(bytes[i].toInt())
        }
        return 4
    }

    fun writeChar(waarde: Char): Int {
        writeByte(waarde.code)
        return 1
    }

    fun writeString(waarde: String): Int {
        var length = 0
        for (i in 0 until waarde.length) {
            length += writeChar(waarde[i])
        }
        length += writeByte(0)
        return length
    }

    fun writeArray(array: ByteArray): Int {
        for (i in array.indices) {
            writeByte(array[i].toInt())
        }
        return array.size
    }

    fun replaceOffset(offsetOffset: Int, newOffset: Int) {
        val bbuf = ByteBuffer.allocate(4)
        bbuf.order(ByteOrder.BIG_ENDIAN)
        bbuf.putInt(newOffset)
        byteBuffer[offsetOffset] = bbuf[3]
        byteBuffer[offsetOffset + 1] = bbuf[2]
        byteBuffer[offsetOffset + 2] = bbuf[1]
    }

    fun replaceDataOffset(offsetOffset: Int, newOffset: Int) {
        println("Newoffset: 0x" + Integer.toString(newOffset, 16) + " " + newOffset)
        val bbuf = ByteBuffer.allocate(4)
        bbuf.order(ByteOrder.BIG_ENDIAN)
        bbuf.putInt(newOffset)
        byteBuffer[offsetOffset] = bbuf[3]
        byteBuffer[offsetOffset + 1] = bbuf[2]
        byteBuffer[offsetOffset + 2] = bbuf[1]
    }

    val array: ByteArray
        get() {
            val dataBuffer = ByteArray(byteBuffer.size)
            for (i in dataBuffer.indices) {
                dataBuffer[i] = byteBuffer[i]!!
            }
            return dataBuffer
        }

    companion object {
        fun float2arr(f: Float): ByteArray {
            val n = java.lang.Float.floatToIntBits(f)
            val bytes = ByteArray(4)
            for (i in bytes.indices) {
                bytes[i] = (n shr i * 8 and 0xFF).toByte() // 8!!!!
            }
            return bytes
        }

        fun short2arr(i: Int): ByteArray {
            val bytes = ByteArray(2)
            val s = i.toShort()
            bytes[1] = (s.toInt() shr 8 and 0xff).toByte()
            bytes[0] = (s.toInt() and 0xff).toByte()
            return bytes
        }

        fun int2arr(value: Int): ByteArray {
            val bytes = ByteArray(4)
            for (i in bytes.indices) {
                bytes[i] = (value shr i * 8 and 0xFF).toByte() // 8!!!!
            }
            return bytes
        }
    }
}