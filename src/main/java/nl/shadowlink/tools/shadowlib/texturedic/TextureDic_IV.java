package nl.shadowlink.tools.shadowlib.texturedic;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.shadowlib.model.wdr.ResourceFile;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class TextureDic_IV {
    private int[] textureId;
    private String[] texName;
    private ArrayList<Texture> textures = new ArrayList<Texture>();
    private int sysSize = 0;

    public int[] loadTextureDic(TextureDic wtd, boolean compressed, int sysSize) {
        this.sysSize = sysSize;
        if (compressed) {
            ByteReader br;
            if (wtd.br == null) {
                ReadFunctions rf = new ReadFunctions(wtd.getFileName());
                System.out.println("WTD Opened");
                br = rf.getByteReader();
            } else {
                br = wtd.br;
            }

            byte stream[];

            ResourceFile res = new ResourceFile();
            stream = res.read(br, wtd.fileSize);

            this.sysSize = res.getSystemSize();

            ByteReader br2 = new ByteReader(stream, 0);
            read(br2);
        } else {
            read(wtd.br);
        }

        wtd.texName = texName;
        wtd.textures = this.textures;
        return textureId;
    }

    public void read(ByteReader br) {
        // TODO: Move GL stuff to Shadow-Mapper
        int VTable = br.readUInt32();
        // Message.displayMsgHigh("VTable: " + VTable);

        int blockMapOffset = br.readOffset();

        int parentDictionary = br.readUInt32();
        int usageCount = br.readUInt32();

        // SimpleCollection
        int hashTableOffset = br.readOffset();
        int texCount = br.readUInt16();
        int texSize = br.readUInt16();
        // Message.displayMsgHigh("HashTableOffset: " + hashTableOffset + " size: " + texCount);

        textureId = new int[texCount];
        texName = new String[texCount];
        System.out.println("texcount: " + texCount);

        if (texCount > 0) {

            // TODO: We need this somewhere
//         if (gl != null) {
//         gl.glGenTextures(texCount, textureId, 0);
//         }

            int save = br.getCurrentOffset();
            br.setCurrentOffset(hashTableOffset);
            for (int i = 0; i < texCount; i++) {
                // Message.displayMsgHigh("Hash: " + br.readUInt32());
            }

            br.setCurrentOffset(save);

            // PointerCollection
            int textureListOffset = br.readOffset();
            int pTexCount = br.readUInt16();
            int pTexSize = br.readUInt16();
            // Message.displayMsgHigh("TextureListOffset: " + textureListOffset);

            save = br.getCurrentOffset();
            br.setCurrentOffset(textureListOffset);

            for (int i = 0; i < pTexCount; i++) {
                Texture texture = new Texture();
                int texOffset = br.readOffset();

                save = br.getCurrentOffset();
                br.setCurrentOffset(texOffset);

                int TexVTable = br.readUInt32();
                // Message.displayMsgHigh("VTable: " + TexVTable);
                int unknown1 = br.readUInt32();
                // Message.displayMsgHigh("Unknown1: " + unknown1);
                int unknown2 = br.readUInt32();
                // Message.displayMsgHigh("Unknown2: " + unknown2);
                int unknown3 = br.readUInt32();
                // Message.displayMsgHigh("Unknown3: " + unknown3);
                int unknown4 = br.readUInt32();
                // Message.displayMsgHigh("Unknown4: " + unknown4);
                int texNameOffset = br.readOffset();
                // //Message.displayMsgHigh("TexNameOffset: " +
                // Utils.getHexString(texNameOffset));
                int save2 = br.getCurrentOffset();
                br.setCurrentOffset(texNameOffset);
                String packName = br.readNullTerminatedString();
                // Message.displayMsgHigh("PackName: " + packName);
                String name = packName.replace("pack:/", "").replace(".dds", "");
                // Message.displayMsgHigh("Name: " + name);
                texName[i] = name;
                br.setCurrentOffset(save2);
                int unknown5 = br.readUInt32();
                // Message.displayMsgHigh("Unknown5: " + unknown5);
                int width = br.readUInt16();
                int height = br.readUInt16();
                texture.width = width;
                texture.height = height;
                // Message.displayMsgHigh("Width x Height: " + width + "x" + height);
                String compression = br.readString(4);
                // Message.displayMsgHigh("Compression: " + compression);
                int strideSize = br.readUInt16();
                byte type = br.readByte();
                byte levels = br.readByte();
                // Message.displayMsgHigh("StideSize: " + strideSize);
                // Message.displayMsgHigh("Type: " + type);
                // Message.displayMsgHigh("Levels: " + levels);
                float unk1 = br.readFloat();
                float unk2 = br.readFloat();
                float unk3 = br.readFloat();
                // Message.displayMsgHigh("Floats: " + unk1 + ", " + unk2 + ", " + unk3);
                unk1 = br.readFloat();
                unk2 = br.readFloat();
                unk3 = br.readFloat();
                // Message.displayMsgHigh("Floats: " + unk1 + ", " + unk2 + ", " + unk3);
                int nextTexOffset = br.readOffset();
                // //Message.displayMsgHigh("Next texture is at: " +
                // Utils.getHexString(nextTexOffset));
                int unknown6 = br.readUInt32();
                // //Message.displayMsgHigh("Unknown6: " + unknown6);
                int dataOffset = br.readDataOffset();
                // //Message.displayMsgHigh("Data at: " +
                // Utils.getHexString(dataOffset+sysSize));
                int unknown7 = br.readUInt32();
                // Message.displayMsgHigh("Unknown7: " + unknown7);

                // load the texture
                br.setCurrentOffset(dataOffset + sysSize);

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

                textures.add(texture);

                br.setCurrentOffset(save);
            }
        }
    }

}