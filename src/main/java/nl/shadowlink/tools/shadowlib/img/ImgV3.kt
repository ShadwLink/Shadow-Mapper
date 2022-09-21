package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.readNullTerminatedString
import nl.shadowlink.tools.shadowlib.utils.Utils
import nl.shadowlink.tools.shadowlib.utils.encryption.Decrypter
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.buffer
import okio.source
import java.io.ByteArrayInputStream
import java.nio.file.Path
import kotlin.experimental.and

/**
 * @author Shadow-Link
 */
class ImgV3(
    private val encryptionKey: ByteArray
) : ImgLoader {
    private val decrypter = Decrypter(encryptionKey)

    override fun load(path: Path): Img {
        FileSystem.SYSTEM.source(path.toOkioPath()).buffer().use { fs ->
            val identifier = fs.peek().readByteArray(4)

            return if (identifier[0].toInt() == 82 && identifier[1].toInt() == 42 && identifier[2].toInt() == 78 && identifier[3].toInt() == -87) {
                readUnencryptedImg(path, fs)
            } else {
                readEncryptedImg(path, fs)
            }
        }
    }

    private fun readUnencryptedImg(path: Path, rf: BufferedSource): Img {
        val itemCount = rf.readInt()
        val tableSize = rf.readInt();
        val sizeOfTableItems = rf.readShort();
        val unknown = rf.readShort();

        val items = readTable(itemCount, rf)

        return Img(path = path, items = items, isEncrypted = false)
    }

    private fun readTable(itemCount: Int, bs: BufferedSource): MutableList<ImgItem> {
        val items = ArrayList<ImgItem>()

        for (curItem in 0 until itemCount) {
            var itemSize = bs.readIntLe() // or flags
            val itemType = bs.readIntLe()
            val itemOffset = bs.readIntLe() * BLOCK_SIZE
            val usedBlocks = bs.readShortLe()
            val padding = bs.readShortLe() and 0x7FF
            val item = ImgItem("")
            if (itemType <= 0x6E) {
                item.flags = itemSize
                itemSize = Utils.getTotalMemSize(itemSize)
            }
            item.offset = itemOffset
            item.size = usedBlocks * BLOCK_SIZE - padding
            item.type = itemType
            items.add(item)
        }

        // read names
        for (curName in 0 until itemCount) {
            items[curName].name = bs.readNullTerminatedString()
        }
        return items
    }

    private fun readEncryptedImg(path: Path, rf: BufferedSource): Img {
        var data = decrypter.decrypt16byteBlock(rf)

        var itemCount = 0
        var tableSize = 0
        ByteArrayInputStream(data).source().buffer().use { br ->
            // TODO: Implement readUInt32
            val id = br.readIntLe()
            val version = br.readIntLe()
            itemCount = br.readIntLe()
            tableSize = br.readIntLe()
        }

        val itemSize = rf.readShortLe()
        val unknown = rf.readShortLe()

        val namesSize = tableSize - itemCount * itemSize

        // Decrypt the table
        var bytesRead = 0
        val decryptedTableData = ByteArray(tableSize)
        while (bytesRead + 16 < tableSize) {
            data = decrypter.decrypt16byteBlock(rf)
            data.copyInto(destination = decryptedTableData, destinationOffset = bytesRead)
            bytesRead += 16
        }
        // Copy left over bytes which are unencrypted
        rf.readByteArray((tableSize - bytesRead).toLong()).copyInto(decryptedTableData, bytesRead)

        val items = ByteArrayInputStream(decryptedTableData).source().buffer().use { br ->
            readTable(itemCount, br)
        }

        return Img(path = path, items = items, isEncrypted = true)
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

    companion object {
        private const val BLOCK_SIZE = 0x800
    }
}
