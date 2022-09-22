package nl.shadowlink.tools.shadowlib.texturedic

import nl.shadowlink.tools.io.ByteReader
import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.model.wdr.ResourceFile

/**
 * @author Shadow-Link
 */
class TextureDicIV {
    private val textures = ArrayList<Texture>()
    private var sysSize = 0

    fun loadTextureDic(wtd: TextureDic, compressed: Boolean, sysSize: Int) {
        this.sysSize = sysSize
        if (compressed) {
            val br: ByteReader? = if (wtd.br == null) {
                val rf = ReadFunctions(wtd.fileName)
                println("WTD Opened")
                rf.byteReader
            } else {
                wtd.br
            }
            val stream: ByteArray
            val res = ResourceFile()
            stream = res.read(br, wtd.fileSize)
            this.sysSize = res.systemSize
            val br2 = ByteReader(stream, 0)
            read(br2)
        } else {
            read(wtd.br)
        }
        wtd.textures = textures
    }

    fun read(br: ByteReader) {
        // TODO: Move GL stuff to Shadow-Mapper
        val VTable = br.readUInt32()
        val blockMapOffset = br.readOffset()
        val parentDictionary = br.readUInt32()
        val usageCount = br.readUInt32()

        // SimpleCollection
        val hashTableOffset = br.readOffset()
        val texCount = br.readUInt16()
        val texSize = br.readUInt16()

        if (texCount > 0) {

            var save = br.getCurrentOffset()
            br.setCurrentOffset(hashTableOffset)
            for (i in 0 until texCount) {
                // Message.displayMsgHigh("Hash: " + br.readUInt32());
            }
            br.setCurrentOffset(save)

            // PointerCollection
            val textureListOffset = br.readOffset()
            val textureCount = br.readUInt16()
            val pTexSize = br.readUInt16()
            save = br.getCurrentOffset()
            br.setCurrentOffset(textureListOffset)
            for (i in 0 until textureCount) {
                val texOffset = br.readOffset()
                save = br.getCurrentOffset()
                br.setCurrentOffset(texOffset)
                val TexVTable = br.readUInt32()
                val unknown1 = br.readUInt32()
                val unknown2 = br.readUInt32()
                val unknown3 = br.readUInt32()
                val unknown4 = br.readUInt32()
                val texNameOffset = br.readOffset()
                val save2 = br.getCurrentOffset()
                br.setCurrentOffset(texNameOffset)
                val packName = br.readNullTerminatedString()
                // Message.displayMsgHigh("PackName: " + packName);
                val name = packName.replace("pack:/", "").replace(".dds", "")
                br.setCurrentOffset(save2)
                val unknown5 = br.readUInt32()
                val width = br.readUInt16()
                val height = br.readUInt16()
                val compression = br.readString(4)
                val strideSize = br.readUInt16()
                val type = br.readByte()
                val levels = br.readByte()
                var unk1 = br.readFloat()
                var unk2 = br.readFloat()
                var unk3 = br.readFloat()
                unk1 = br.readFloat()
                unk2 = br.readFloat()
                unk3 = br.readFloat()
                val nextTexOffset = br.readOffset()
                val unknown6 = br.readUInt32()
                val dataOffset = br.readDataOffset()
                val unknown7 = br.readUInt32()


                // load the texture
                br.setCurrentOffset(dataOffset + sysSize)

                val dataSize = when (compression) {
                    "DXT1" -> (width * height / 2)
                    "DXT3" -> width * height
                    "DXT5" -> width * height
                    else -> throw IllegalStateException("Unknown compression type")
                }

                val texture = Texture(
                    diffuseTexName = name,
                    dxtCompressionType = compression,
                    width = width,
                    height = height,
                    data = br.readBytes(dataSize),
                )

                textures.add(texture)
                br.setCurrentOffset(save)
            }
        }
    }
}