package nl.shadowlink.tools.shadowlib.texturedic

import nl.shadowlink.tools.io.WriteBuffer
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils
import nl.shadowlink.tools.shadowlib.utils.hashing.OneAtATimeHasher.Companion.getHashKey
import java.util.zip.Deflater

class WtdExporter {

    fun convertToWTD(txd: TextureDic, wf: WriteFunctions) {
        makeLayout(txd)
        val wf = WriteBuffer()
        var offset = 0
        var sysSize = 0
        var graphicSize = 0
        offset += wf.writeInt(6902660) // vtable
        offset += wf.writeOffset(0x20) // blockmapoffset
        offset += wf.writeInt(0) // parent
        offset += wf.writeInt(1) // usagecount
        offset += wf.writeOffset(0x0220) // hash offset currently unknown
        offset += wf.writeShort(txd.textureCount) // hash count
        offset += wf.writeShort(txd.textureCount) // hash size

        // calc texturelist offset
        var texOffset: Int = 0x0220 + txd.textureCount * 4
        while (texOffset % 16 != 0) {
            texOffset++
        }
        offset += wf.writeOffset(texOffset) // texturelist offset
        offset += wf.writeShort(txd.textureCount)
        offset += wf.writeShort(txd.textureCount)
        offset += wf.writeInt(0) // blockmap
        offset = addPaddingToRow(wf, offset)
        offset = addPaddingTillOffset(wf, offset, 0x0220)

        // writeHashTable
        for (i in txd.textures.indices) {
            offset += wf.writeInt(getHashKey(txd.textures[i].diffuseTexName).toInt())
        }
        offset = addPaddingToRow(wf, offset)

        // write texturelist Offsets
        // calculate the first offset first
        texOffset += txd.textureCount * 4
        while (texOffset % 16 != 0) {
            texOffset++
        }
        for (i in 0 until txd.textureCount) {
            offset += wf.writeOffset(texOffset) // temp
            texOffset += 0x50
        }
        offset = addPaddingToRow(wf, offset)
        for (i in 0 until txd.textureCount) {
            var tempTex: Texture? = txd.textures.get(i)
            offset += wf.writeInt(7019924) // vtable
            offset += wf.writeInt(0) // blockmapadress
            offset += wf.writeInt(65536) // unknown
            offset += wf.writeInt(0) // unknown
            offset += wf.writeInt(0) // unknown
            offset += wf.writeOffset(texOffset) // texNameOffset
            offset += wf.writeInt(0) // unknown
            offset += wf.writeShort(tempTex!!.width) // tex width
            offset += wf.writeShort(tempTex.height) // tex height
            offset += wf.writeInt(tempTex.compression) // tex compression
            offset += wf.writeShort(tempTex.width / 2) // stridesize
            offset += wf.writeByte(0) // type
            offset += wf.writeByte(1) // levels (might be mipmapcount)
            // unknown floats
            offset += wf.writeFloat(1.0f)
            offset += wf.writeFloat(1.0f)
            offset += wf.writeFloat(1.0f)
            offset += wf.writeFloat(0f)
            offset += wf.writeFloat(0f)
            offset += wf.writeFloat(0f)
            if (i < txd.textureCount) offset += wf.writeOffset(offset + 0xF) // offset to next texturelist
            offset += wf.writeInt(0) // unknown
            offset += wf.writeDataOffset(tempTex.conversionOffset) // dataOffset
            offset += wf.writeInt(0) // unknown
            texOffset += 11 + txd.textures.get(i).diffuseTexName.length
            while (texOffset % 16 != 0) {
                texOffset++
            }
            tempTex = null
        }
        for (i in 0 until txd.textureCount) {
            println("Offset " + Utils.getHexString(offset))
            offset += wf.writeString("pack:/" + txd.textures.get(i).diffuseTexName + ".dds")
            offset = addPaddingToRow(wf, offset)
        }
        while (offset % 4096 != 0) {
            if (offset % 2 != 0) {
                offset += wf.writeByte(0xCD)
            }
            offset += wf.writeShort(52685)
        }
        sysSize = offset
        for (i in 0 until txd.textureCount) {
            val tempTex: Texture = txd.textures[i]
            while (offset != tempTex.conversionOffset + sysSize) {
                offset += wf.writeByte(0xCD)
            }
            offset += wf.writeArray(tempTex.data!!)
        }
        graphicSize = offset - sysSize
        TODO("implement this")
//        wf.write(0x05435352)
//        wf.write(0x00000008)
//        flags = getFlags(sysSize, graphicSize)
//        wf.write(flags)
//        wf.writeShort(0xDA78)
//        println("Starting compression")
//        wf.write(compress(wf.array))
    }

    private fun makeLayout(txd: TextureDic) {
        println("Before sort")
        displayTextures(txd)
        sortTextures()
        println("After sort")
        displayTextures(txd)
        var offset = 0
        val tempTextures: ArrayList<Texture> = txd.textures
        txd.textures = ArrayList<Texture>()
        var blocksLeft = tempTextures.size - 1
        while (blocksLeft >= 0) {
            println("Blocks left $blocksLeft")
            if (tempTextures[blocksLeft].dataSize > 0x00020000) {
                tempTextures[blocksLeft].conversionOffset = offset
                txd.textures.add(tempTextures[blocksLeft])
                offset = writeBlock(tempTextures[blocksLeft], offset)
                offset = writeGraphicsPadding(offset)
                tempTextures.removeAt(blocksLeft)
                blocksLeft = tempTextures.size - 1
            } else {
                if (blocksLeft != 0) {
                    var check = 0
                    if (tempTextures[blocksLeft].dataSize + tempTextures[check].dataSize > 0x00010000) {
                        tempTextures[blocksLeft].conversionOffset = offset
                        txd.textures.add(tempTextures[blocksLeft])
                        offset = writeBlock(tempTextures[blocksLeft], offset)
                        offset = writeGraphicsPadding(offset)
                        tempTextures.removeAt(blocksLeft)
                        blocksLeft = tempTextures.size - 1
                    } else {
                        var currentWin = check
                        check++
                        while (check < blocksLeft) {
                            if (tempTextures[blocksLeft].dataSize + tempTextures[check].dataSize > 0x00010000) {
                                break
                            } else {
                                currentWin = check
                                check++
                            }
                        }
                        tempTextures[blocksLeft].conversionOffset = offset
                        txd.textures.add(tempTextures[blocksLeft])
                        offset = writeBlock(tempTextures[blocksLeft], offset)
                        tempTextures[currentWin].conversionOffset = offset
                        txd.textures.add(tempTextures[currentWin])
                        offset = writeBlock(tempTextures[currentWin], offset)
                        offset = writeGraphicsPadding(offset)
                        tempTextures.removeAt(blocksLeft)
                        tempTextures.removeAt(currentWin)
                        blocksLeft = tempTextures.size - 1
                    }
                } else {
                    tempTextures[blocksLeft].conversionOffset = offset
                    txd.textures.add(tempTextures[blocksLeft])
                    offset = writeBlock(tempTextures[blocksLeft], offset)
                    offset = writeGraphicsPadding(offset)
                    tempTextures.removeAt(blocksLeft)
                    blocksLeft = tempTextures.size - 1
                }
            }
        }
        println("END")
        displayTextures(txd)
    }

    private fun writeBlock(texture: Texture, offset: Int): Int {
        var offset = offset
        offset += texture.dataSize
        while (offset % 16 != 0) {
            offset++
        }
        return offset
    }

    private fun writeGraphicsPadding(retValue: Int): Int {
        var retValue = retValue
        println("Writing padding 0x00010000")
        if (retValue % 0x00010000 != 0) {
            println("We need padding $retValue")
            while (retValue % 0x00010000 != 0) {
                retValue++
            }
        }
        return retValue
    }

    private fun displayTextures(txd: TextureDic) {
        txd.textures.forEach { texture ->
            println(
                "Texture size ${Utils.getHexString(texture.dataSize)} offset ${Utils.getHexString(texture.conversionOffset)}"
            )
        }
    }

    private fun sortTextures() {
//        var temp: Texture = Texture()
//        for (position in textures.indices.reversed()) {
//            for (scan in 0..(position - 1)) {
//                if (textures.get(scan).dataSize > textures.get(scan + 1).dataSize) {
//                    temp = textures.get(scan)
//                    textures.set(scan, textures.get(scan + 1))
//                    textures.set(scan + 1, temp)
//                }
//            }
//        }
    }

    private fun compress(source: ByteArray): ByteArray? {
        val dest = ByteArray(source.size + 50)
        val deflater = Deflater()
        deflater.setInput(source)
        deflater.finish()
        deflater.deflate(dest)
        TODO("Fix this?")
//        size = deflater.totalOut
//        size += 12
//        deflater.end()
//        val ret = ByteArray(size)
//        for (i in 2 until size) {
//            ret[i - 2] = dest[i]
//        }
//        return ret
    }

    private fun getFlags(sysSegSize: Int, gpuSegSize: Int): Int {
        return (getCompactSize(sysSegSize) and 0x7FFF) or ((getCompactSize(gpuSegSize) and 0x7FFF) shl 15) or (3 shl 30)
    }

    private fun getCompactSize(size: Int): Int {
        var size = size
        var i: Int
        // sizes must be multiples of 256!
        if ((size % 256) != 0) {
            println("Size klopt niet")
        }
        size = size shr 8
        i = 0
        while (((size % 2) == 0) && (size >= 32) && (i < 15)) {
            i++
            size = size shr 1
        }
        return ((i and 0xF) shl 11) or (size and 0x7FF)
    }

    private fun addPaddingTillOffset(wf: WriteBuffer, currentOffset: Int, offset: Int): Int {
        var currentOffset = currentOffset
        while (currentOffset != offset) { // not sure what this is for but almost every wtd pads till 0x0220
            currentOffset += wf.writeByte(205) // padding
        }
        return offset
    }

    private fun addPaddingToRow(wf: WriteBuffer, size: Int): Int {
        var size = size
        if ((size % 2) != 0) {
            size += wf.writeByte(0xCD)
        }
        while ((size % 16) != 0) {
            size += wf.writeShort(52685) // padding
        }
        return size
    }
}