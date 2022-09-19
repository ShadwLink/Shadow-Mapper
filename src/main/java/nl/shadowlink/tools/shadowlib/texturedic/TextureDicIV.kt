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
        // Message.displayMsgHigh("VTable: " + VTable);
        val blockMapOffset = br.readOffset()
        val parentDictionary = br.readUInt32()
        val usageCount = br.readUInt32()

        // SimpleCollection
        val hashTableOffset = br.readOffset()
        val texCount = br.readUInt16()
        val texSize = br.readUInt16()
        println("texcount: $texCount")
        if (texCount > 0) {

            // TODO: We need this somewhere
//         if (gl != null) {
//         gl.glGenTextures(texCount, textureId, 0);
//         }
            var save = br.getCurrentOffset()
            br.setCurrentOffset(hashTableOffset)
            for (i in 0 until texCount) {
                // Message.displayMsgHigh("Hash: " + br.readUInt32());
            }
            br.setCurrentOffset(save)

            // PointerCollection
            val textureListOffset = br.readOffset()
            val pTexCount = br.readUInt16()
            val pTexSize = br.readUInt16()
            // Message.displayMsgHigh("TextureListOffset: " + textureListOffset);
            save = br.getCurrentOffset()
            br.setCurrentOffset(textureListOffset)
            for (i in 0 until pTexCount) {
                val texOffset = br.readOffset()
                save = br.getCurrentOffset()
                br.setCurrentOffset(texOffset)
                val TexVTable = br.readUInt32()
                // Message.displayMsgHigh("VTable: " + TexVTable);
                val unknown1 = br.readUInt32()
                // Message.displayMsgHigh("Unknown1: " + unknown1);
                val unknown2 = br.readUInt32()
                // Message.displayMsgHigh("Unknown2: " + unknown2);
                val unknown3 = br.readUInt32()
                // Message.displayMsgHigh("Unknown3: " + unknown3);
                val unknown4 = br.readUInt32()
                // Message.displayMsgHigh("Unknown4: " + unknown4);
                val texNameOffset = br.readOffset()
                // //Message.displayMsgHigh("TexNameOffset: " +
                // Utils.getHexString(texNameOffset));
                val save2 = br.getCurrentOffset()
                br.setCurrentOffset(texNameOffset)
                val packName = br.readNullTerminatedString()
                // Message.displayMsgHigh("PackName: " + packName);
                val name = packName.replace("pack:/", "").replace(".dds", "")
                // Message.displayMsgHigh("Name: " + name);
                val texture = Texture(name)
                br.setCurrentOffset(save2)
                val unknown5 = br.readUInt32()
                // Message.displayMsgHigh("Unknown5: " + unknown5);
                val width = br.readUInt16()
                val height = br.readUInt16()
                texture.width = width
                texture.height = height
                // Message.displayMsgHigh("Width x Height: " + width + "x" + height);
                val compression = br.readString(4)
                // Message.displayMsgHigh("Compression: " + compression);
                val strideSize = br.readUInt16()
                val type = br.readByte()
                val levels = br.readByte()
                // Message.displayMsgHigh("StideSize: " + strideSize);
                // Message.displayMsgHigh("Type: " + type);
                // Message.displayMsgHigh("Levels: " + levels);
                var unk1 = br.readFloat()
                var unk2 = br.readFloat()
                var unk3 = br.readFloat()
                // Message.displayMsgHigh("Floats: " + unk1 + ", " + unk2 + ", " + unk3);
                unk1 = br.readFloat()
                unk2 = br.readFloat()
                unk3 = br.readFloat()
                // Message.displayMsgHigh("Floats: " + unk1 + ", " + unk2 + ", " + unk3);
                val nextTexOffset = br.readOffset()
                // //Message.displayMsgHigh("Next texture is at: " +
                // Utils.getHexString(nextTexOffset));
                val unknown6 = br.readUInt32()
                // //Message.displayMsgHigh("Unknown6: " + unknown6);
                val dataOffset = br.readDataOffset()
                // //Message.displayMsgHigh("Data at: " +
                // Utils.getHexString(dataOffset+sysSize));
                val unknown7 = br.readUInt32()
                // Message.displayMsgHigh("Unknown7: " + unknown7);

                // load the texture
                br.setCurrentOffset(dataOffset + sysSize)

//         if (gl != null) {
//         gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[i]);
//         // //Message.displayMsgHigh("Current offset: " +
//         // Utils.getHexString(dataOffset+sysSize));
//
//         if (compression.equals("DXT1")) {
//         int dataSize = (int) (width * height / 2);
//         System.out.println("DXT1 Datasize: " + dataSize);
//         gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGB_S3TC_DXT1_EXT, width, height, 0,
//         dataSize,
//         br.getByteBuffer(dataSize));
//         } else if (compression.equals("DXT3")) {
//         int dataSize = (int) (width * height);
//         System.out.println("DXT3 Datasize: " + dataSize);
//         gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT, width, height, 0,
//         dataSize,
//         br.getByteBuffer(dataSize));
//         } else if (compression.equals("DXT5")) {
//         int dataSize = (int) (width * height);
//         System.out.println("DXT5 Datasize: " + dataSize);
//         gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT, width, height, 0,
//         dataSize,
//         br.getByteBuffer(dataSize));
//         }
//
//         // gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, 3, width, height, 0,
//         // gl.GL_RGBA, gl.GL_UNSIGNED_BYTE,
//         // br.getByteBuffer(dataSize));
//
//         gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
//         gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
//         gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
//         gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
//         gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
//         }
                textures.add(texture)
                br.setCurrentOffset(save)
            }
        }
    }
}