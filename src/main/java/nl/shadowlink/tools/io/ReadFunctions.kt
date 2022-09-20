package nl.shadowlink.tools.io

import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.file.Path
import java.util.logging.Level
import java.util.logging.Logger

class ReadFunctions(
    name: String
) {
    private val dataIn: RandomAccessFile

    constructor(file: File) : this(file.absolutePath)

    constructor(path: Path) : this(path.toFile())

    init {
        dataIn = RandomAccessFile(name, "r")
    }

    fun closeFile(): Boolean {
        var ret = true
        try {
            dataIn.close()
        } catch (ex: IOException) {
            println("Unable to close file")
            ret = false
        }
        return ret
    }

    fun readByte(): Byte {
        var waarde: Byte = -1
        waarde = try {
            dataIn.readByte()
        } catch (ex: IOException) {
            -1
        }
        return waarde
    }

    fun readBytes(bytes: ByteArray) {
        try {
            dataIn.read(bytes)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun readInt(): Int {
        var waarde = -1
        waarde = try {
            swapInt(dataIn.readInt())
        } catch (ex: IOException) {
            -1
        }
        return waarde
    }

    fun readShort(): Int {
        var waarde = -1
        waarde = try {
            swapShort(dataIn.readShort())
        } catch (ex: IOException) {
            -1
        }
        return waarde
    }

    fun readFloat(): Float {
        var waarde = -1f
        val bytes = ByteArray(4)
        for (i in 3 downTo 0) {
            bytes[i] = readByte()
        }
        val data = ByteBuffer.wrap(bytes)
        waarde = data.float
        return waarde
    }

    fun readString(size: Int): String {
        var letter = 'n'
        var woord = ""
        //letter = readChar(data_in);
        for (i in 0 until size) {
            letter = readChar()
            woord += letter
        }
        return woord
    }

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
        var woord = ""
        var b = readByte()
        while (b.toInt() != 0) {
            woord += Char(b.toUShort())
            b = readByte()
        }
        return woord
    }

    fun readChar(): Char {
        var letter = '\u0000'
        try {
            letter = Char(dataIn.readByte().toUShort())
        } catch (ex: IOException) {
            //Logger.getLogger(loadSAFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return letter
    }

    /**
     * Converts a big endian int to a little endian int
     *
     * @param v
     * @return the little endian int of a big endian int
     */
    fun swapInt(v: Int): Int {
        return v ushr 24 or (v shl 24) or (v shl 8 and 0x00FF0000) or (v shr 8 and 0x0000FF00)
    }

    /**
     * Converts a big endian short to a little endian short
     *
     * @param i
     * @return the little endian short of a big endian short
     */
    fun swapShort(i: Short): Int {
        return (i.toInt() shr 8 and 0xff) + (i.toInt() shl 8 and 0xff00)
    }

    /**
     * Converts a big endian float to a little endian float
     *
     * @param f
     * @return the little endian float of a big endian float
     */
    fun swapFloat(f: Float): Float {
        var intValue = java.lang.Float.floatToIntBits(f)
        intValue = swapInt(intValue)
        return java.lang.Float.intBitsToFloat(intValue)
    }

    fun readVector3D(): Vector3D {
        return Vector3D(readFloat(), readFloat(), readFloat())
    }

    fun readVector4D(): Vector4D {
        return Vector4D(readFloat(), readFloat(), readFloat(), readFloat())
    }

    fun moreToRead(): Int {
        return try {
            (dataIn.length() - dataIn.filePointer).toInt()
        } catch (ex: IOException) {
            Logger.getLogger(ReadFunctions::class.java.name).log(Level.SEVERE, null, ex)
            0
        }
    }

    val byteReader: ByteReader?
        get() = try {
            val stream = ByteArray(dataIn.length().toInt())
            dataIn.read(stream, 0, dataIn.length().toInt())
            ByteReader(stream, 0)
        } catch (ex: IOException) {
            null
        }

    fun getByteReader(size: Int): ByteReader {
        val stream = ByteArray(size)
        dataIn.read(stream, 0, size)
        return ByteReader(stream, 0)
    }

    fun seek(offset: Int) {
        try {
            dataIn.seek(offset.toLong())
        } catch (ex: IOException) {
            Logger.getLogger(ReadFunctions::class.java.name).log(Level.SEVERE, null, ex)
        }
    }

    fun seek(pOffset: Long) {
        try {
            dataIn.seek(pOffset)
        } catch (ex: IOException) {
            Logger.getLogger(ReadFunctions::class.java.name).log(Level.SEVERE, null, ex)
        }
    }

    fun readUnsignedInt(): Long {
        val i = readInt()
        return i.toLong() and 0xffffffffL
    }

    fun readArray(size: Int): ByteArray {
        val array = ByteArray(size)
        try {
            dataIn.readFully(array)
        } catch (ex: IOException) {
            Logger.getLogger(ReadFunctions::class.java.name).log(Level.SEVERE, null, ex)
        }
        return array
    }
}