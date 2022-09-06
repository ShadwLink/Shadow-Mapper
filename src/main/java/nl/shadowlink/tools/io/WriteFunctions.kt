package nl.shadowlink.tools.io

import jogamp.graph.font.typecast.ot.table.Table.name
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class WriteFunctions(
    private var dataOut: RandomAccessFile
) {

    constructor(name: String) : this(RandomAccessFile(name, "rw"))

    fun closeFile(): Boolean {
        try {
            dataOut.close()
        } catch (ex: IOException) {
            Logger.getLogger(WriteFunctions::class.java.name).log(Level.SEVERE, null, ex)
            return false
        }
        return true
    }

    fun writeByte(value: Int) {
        try {
            dataOut.writeByte(value)
        } catch (ex: IOException) {
            // Nothing we can do about this
        }
    }

    fun write(value: Int) {
        var value = value
        val bbuf = ByteBuffer.allocate(4)
        bbuf.order(ByteOrder.BIG_ENDIAN)
        bbuf.putInt(value)
        bbuf.order(ByteOrder.LITTLE_ENDIAN)
        value = bbuf.getInt(0)
        try {
            dataOut.writeInt(value)
        } catch (ex: IOException) {
            value = -1
        }
    }

    fun writeShort(value: Int) {
        var value = value
        val bbuf = ByteBuffer.allocate(4)
        bbuf.order(ByteOrder.BIG_ENDIAN)
        bbuf.putInt(value)
        bbuf.order(ByteOrder.LITTLE_ENDIAN)
        value = bbuf.getShort(2).toInt()
        try {
            dataOut.writeShort(value)
        } catch (ex: IOException) {
            value = -1
        }
    }

    fun write(value: Float) {
        var value = value
        val bbuf = ByteBuffer.allocate(4)
        bbuf.order(ByteOrder.BIG_ENDIAN)
        bbuf.putFloat(value)
        bbuf.order(ByteOrder.LITTLE_ENDIAN)
        value = bbuf.getFloat(0)
        try {
            dataOut.writeFloat(value)
        } catch (ex: IOException) {
            Logger.getLogger(WriteFunctions::class.java.name).log(Level.SEVERE, null, ex)
        }
    }

    fun writeChar(value: Char): Char {
        val letter = '\u0000'
        try {
            dataOut.writeByte(value.code)
        } catch (ex: IOException) {
            // Logger.getLogger(loadSAFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return letter
    }

    fun writeString(value: String) {
        for (i in 0 until value.length) {
            writeChar(value[i])
        }
    }

    fun writeNullTerminatedString(value: String) {
        for (i in 0 until value.length) {
            writeChar(value[i])
        }
        writeByte(0)
    }

    fun write(vector: Vector3D) {
        write(vector.x)
        write(vector.y)
        write(vector.z)
    }

    fun write(vector: Vector4D) {
        write(vector.x)
        write(vector.y)
        write(vector.z)
        write(vector.w)
    }

    fun write(array: ByteArray) {
        try {
            dataOut.write(array)
        } catch (ex: IOException) {
            Logger.getLogger(WriteFunctions::class.java.name).log(Level.SEVERE, null, ex)
        }
    }

    fun seek(pos: Int) {
        try {
            dataOut.seek(pos.toLong())
        } catch (ex: IOException) {
            Logger.getLogger(WriteFunctions::class.java.name).log(Level.SEVERE, null, ex)
        }
    }

    fun gotoEnd() {
        try {
            dataOut.seek(dataOut.length())
        } catch (ex: IOException) {
            Logger.getLogger(WriteFunctions::class.java.name).log(Level.SEVERE, null, ex)
        }
    }

    val fileSize: Int
        get() = try {
            dataOut.length().toInt()
        } catch (ex: IOException) {
            0
        }
}