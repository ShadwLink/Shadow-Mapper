package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ByteReader
import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils
import nl.shadowlink.tools.shadowlib.utils.encryption.Decrypter
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class ImgV3(
    private val encryptionKey: ByteArray
) : ImgLoader {
    private val decrypter = Decrypter(encryptionKey)

    private val identifier = ByteArray(4)

    override fun load(file: File): Img {
        val rf = ReadFunctions(file.absolutePath)

        identifier[0] = rf.readByte()
        identifier[1] = rf.readByte()
        identifier[2] = rf.readByte()
        identifier[3] = rf.readByte()
        return if (identifier[0].toInt() == 82 && identifier[1].toInt() == 42 && identifier[2].toInt() == 78 && identifier[3].toInt() == -87) {
            readUnEncryptedImg(file, rf)
        } else {
            readEncryptedImg(file, rf)
        }.also {
            rf.closeFile()
        }
    }

    private fun readUnEncryptedImg(file: File, rf: ReadFunctions): Img {
        val items = ArrayList<ImgItem>()
        // Message.displayMsgHigh("Version 3: " + rf.readInt());
        val itemCount = rf.readInt()
        val tableSize = rf.readInt();
        val sizeOfTableItems = rf.readShort();
        val unknown = rf.readShort();

        // read table
        for (curItem in 0 until itemCount) {
            var itemSize = rf.readInt() // or flags
            val itemType = rf.readInt()
            val itemOffset = rf.readInt() * 0x800
            val usedBlocks = rf.readShort()
            val padding = rf.readShort() and 0x7FF
            val item = ImgItem("")
            if (itemType <= 0x6E) {
                item.flags = itemSize
                itemSize = Utils.getTotalMemSize(itemSize)
            }
            item.offset = itemOffset
            item.size = usedBlocks * 0x800 - padding
            item.type = itemType
            items.add(item)
        }

        // read names
        for (curName in 0 until itemCount) {
            items[curName].name = rf.readNullTerminatedString()
        }

        return Img(file = file, items = items, isEncrypted = false)
    }

    private fun readEncryptedImg(file: File, rf: ReadFunctions): Img {
        val items = ArrayList<ImgItem>()

        var data = withIdentifier(rf, encryptionKey)

        var br = ByteReader(data, 0)
        val id = br.readUInt32()
        val version = br.readUInt32()
        val itemCount = br.readUInt32()
        val tableSize = br.readUInt32()

        var itemSize = rf.readShort()
        val unknown = rf.readShort()

        val namesSize = tableSize - itemCount * itemSize

        for (i in 0 until itemCount) {
            data = decrypter.decrypt16byteBlock(rf) // decrypt all table items
            br = ByteReader(data, 0)
            val item = ImgItem("")
            itemSize = br.readUInt32() // or flags
            val itemType = br.readUInt32()
            val itemOffset = br.readUInt32() * 2048
            val usedBlocks = br.readUInt16()
            val padding = br.readUInt16() and 0x7FF
            if (itemType <= 0x6E) {
                item.flags = itemSize
                itemSize = Utils.getTotalMemSize(itemSize)
            }
            item.offset = itemOffset
            item.size = usedBlocks * 0x800 - padding
            item.type = itemType
            items.add(item)
        }

        var i = 0
        val names = ByteArray(namesSize)
        while (i < namesSize) {
            data = decrypter.decrypt16byteBlock(rf) // decrypt all table names
            for (j in 0..15) {
                names[i + j] = data[j]
            }
            i += 16
            if (i + 16 > namesSize) {
                val lastName = rf.readNullTerminatedString()
                val lastBytes = lastName.toByteArray()
                for (j in lastBytes.indices) {
                    names[i + j] = lastBytes[j]
                }
                i += 16
            }
        }

        br = ByteReader(names, 0)

        i = 0
        while (i < itemCount) {
            val name = br.readNullTerminatedString()
            items[i].name = name
            br.readByte()
            i++
        }

        rf.closeFile()

        return Img(file = file, items = items, isEncrypted = true)
    }

    private fun withIdentifier(rf: ReadFunctions, key: ByteArray): ByteArray {
        var data = ByteArray(16)

        data[0] = identifier[0]
        data[1] = identifier[1]
        data[2] = identifier[2]
        data[3] = identifier[3]

        for (j in 4..15) {
            data[j] = rf.readByte()
        }
        for (j in 1..16) { // 16 (pointless) repetitions
            try {
                data = decrypter.decrypt(data)
            } catch (ex: Exception) {
                Logger.getLogger(ImgV3::class.java.name).log(Level.SEVERE, null, ex)
            }

        }
        return data
    }

    fun saveImg(img: Img) {
        TODO("Saving is not implemented")
//        val wf = WriteFunctions("${img.fileName}.temp")
//        val rf = ReadFunctions(img.fileName)
//        wf.writeByte(82)// write R*N start bytes
//        wf.writeByte(42)
//        wf.writeByte(78)
//        wf.writeByte(-87)
//        wf.write(3) // write version number
//        wf.write(img.items.size) // write item count
//        var tableSize = 0
//        for (i in 0 until img.items.size) {// calculate table size
//            tableSize += img.items[i].name!!.length + 17
//        }
//        wf.write(tableSize)
//        wf.writeShort(0x0010)// table item size (0x10)
//        wf.writeShort(0x00E9) // unknown yet
//
//        var offset = 0x800
//        while (tableSize + 20 > offset) {
//            offset += 0x800
//        }
//        val padding = offset - tableSize - 20
//
//        for (i in 0 until img.items.size) {
//            if (img.items[i].isResource) {
//                wf.write(img.items[i].flags) // write the flags
//            } else {
//                wf.write(img.items[i].size) // size
//            }
//            wf.write(img.items[i].type) // type
//            wf.write(offset / 0x800) // offset
//            offset += img.items[i].size // next offset
//            var pad = 0
//            while (offset % 0x800 != 0) {
//                offset += 1
//                pad += 1
//            }
//            wf.writeShort((img.items[i].size + pad) / 0x800) // blocks
//            if (img.items[i].isResource) {
//                wf.writeShort(pad + 8192) // padding
//            } else {
//                wf.writeShort(pad)
//            }
//        }
//        for (i in 0 until img.items.size) {
//            wf.writeNullTerminatedString(img.items[i].name!!)
//        }
//        for (i in 0 until padding) {
//            wf.writeByte(0)
//        }
//        var cOffset = 0x800
//        for (i in 0 until img.items.size) {
//            rf.seek(img.items[i].offset)
//            val array = rf.readArray(img.items[i].size)
//            wf.write(array)
//            cOffset += img.items[i].size
//            while (cOffset % 0x800 != 0) {
//                cOffset += 1
//                wf.writeByte(0)
//            }
//        }
//        rf.closeFile()
//        wf.closeFile()
//        var origFile: File? = File(img.fileName)
//        try {
//            if (origFile!!.canWrite()) {
//                println("Can write")
//            }
//            if (origFile.canExecute()) {
//                println("Can execute")
//            }
//            if (origFile.delete()) {
//                println("Deleted " + img.fileName)
//            } else {
//                println("Not deleted")
//            }
//        } catch (ex: Exception) {
//            println("Unable to delete " + img.fileName + " " + ex)
//        }
//
//        var newFile: File? = File(img.fileName + ".temp")
//        if (newFile!!.renameTo(File(img.fileName))) {
//            println("rename file")
//        } else {
//            println("Unable to rename file")
//        }
//        newFile.delete()
    }
}
