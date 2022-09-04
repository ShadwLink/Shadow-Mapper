package nl.shadowlink.tools.shadowlib.texturedic;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.io.WriteBuffer;
import nl.shadowlink.tools.io.WriteFunctions;
import nl.shadowlink.tools.shadowlib.utils.HashUtils;
import nl.shadowlink.tools.shadowlib.utils.Utils;

import java.util.ArrayList;
import java.util.zip.Deflater;

/**
 * @author Shadow-Link
 */
public class TextureDic {
    private String fileName;
    private int gameType;

    public int[] textureId;
    public String[] texName;
    public int textureCount = 0;
    public int flags = 0;
    public int size = 0;
    public ArrayList<Texture> textures = new ArrayList<Texture>();

    public ByteReader br = null;

    public int fileSize = -1;
    private boolean compressed = true;

    public TextureDic(String fileName, ByteReader br, int gameType, int fileSize) {
        this.fileName = fileName;
        this.gameType = gameType;
        this.br = br;
        this.fileSize = fileSize;
        loadTextureDic();
    }

    public TextureDic(String fileName) {
        this.fileName = fileName;
        gameType = 2;
        loadTextureDic();
    }

    public TextureDic(String fileName, ByteReader br, int gameType, boolean compressed, int sysSize) {
        this.fileName = fileName;
        this.gameType = gameType;
        this.br = br;
        this.fileSize = sysSize;
        this.compressed = compressed;
        loadTextureDic();
    }

    private boolean loadTextureDic() {
        switch (gameType) {
            case 3:
                if (compressed)
                    textureId = new TextureDic_IV().loadTextureDic(this, compressed, 0);
                else
                    textureId = new TextureDic_IV().loadTextureDic(this, compressed, fileSize);
                break;
            default:
                textureId = new TextureDic_III_ERA().loadTextureDic(this);
        }
        return true;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void convertToWTD(WriteFunctions wrf) {
        makeLayout();
        WriteBuffer wf = new WriteBuffer();
        int offset = 0;
        int sysSize = 0;
        int graphicSize = 0;

        offset += wf.writeInt(6902660); // vtable
        offset += wf.writeOffset(0x20); // blockmapoffset
        offset += wf.writeInt(0); // parent
        offset += wf.writeInt(1); // usagecount
        offset += wf.writeOffset(0x0220); // hash offset currently unknown
        offset += wf.writeShort(textureCount); // hash count
        offset += wf.writeShort(textureCount); // hash size

        // calc texturelist offset
        int texOffset = (0x0220 + (textureCount * 4));
        while ((texOffset % 16) != 0) {
            texOffset++;
        }
        offset += wf.writeOffset(texOffset); // texturelist offset
        offset += wf.writeShort(textureCount);
        offset += wf.writeShort(textureCount);

        offset += wf.writeInt(0); // blockmap
        offset = addPaddingToRow(wf, offset);
        offset = addPaddingTillOffset(wf, offset, 0x0220);

        // writeHashTable
        for (int i = 0; i < textures.size(); i++) {
            offset += wf.writeInt((int) HashUtils.genHash(textures.get(i).difTexName));
        }
        offset = addPaddingToRow(wf, offset);

        // write texturelist Offsets
        // calculate the first offset first
        texOffset += textureCount * 4;
        while ((texOffset % 16) != 0) {
            texOffset++;
        }
        for (int i = 0; i < textureCount; i++) {
            offset += wf.writeOffset(texOffset); // temp
            texOffset += 0x50;
        }
        offset = addPaddingToRow(wf, offset);

        for (int i = 0; i < textureCount; i++) {
            Texture tempTex = textures.get(i);
            offset += wf.writeInt(7019924); // vtable
            offset += wf.writeInt(0); // blockmapadress
            offset += wf.writeInt(65536); // unknown
            offset += wf.writeInt(0); // unknown
            offset += wf.writeInt(0); // unknown
            offset += wf.writeOffset(texOffset); // texNameOffset
            offset += wf.writeInt(0); // unknown
            offset += wf.writeShort(tempTex.width); // tex width
            offset += wf.writeShort(tempTex.height); // tex height
            offset += wf.writeInt(tempTex.compression); // tex compression
            offset += wf.writeShort(tempTex.width / 2); // stridesize
            offset += wf.writeByte(0); // type
            offset += wf.writeByte(1); // levels (might be mipmapcount)
            // unknown floats
            offset += wf.writeFloat(1.0f);
            offset += wf.writeFloat(1.0f);
            offset += wf.writeFloat(1.0f);
            offset += wf.writeFloat(0);
            offset += wf.writeFloat(0);
            offset += wf.writeFloat(0);
            if (i < textureCount)
                offset += wf.writeOffset(offset + 0xF); // offset to next texturelist
            offset += wf.writeInt(0); // unknown
            offset += wf.writeDataOffset(tempTex.conversionOffset); // dataOffset
            offset += wf.writeInt(0); // unknown
            texOffset += 11 + textures.get(i).difTexName.length();
            while ((texOffset % 16) != 0) {
                texOffset++;
            }
            tempTex = null;
        }

        for (int i = 0; i < textureCount; i++) {
            System.out.println("Offset " + Utils.getHexString(offset));
            offset += wf.writeString("pack:/" + textures.get(i).difTexName + ".dds");
            offset = addPaddingToRow(wf, offset);
        }

        while ((offset % 4096) != 0) {
            if ((offset % 2) != 0) {
                offset += wf.writeByte(0xCD);
            }
            offset += wf.writeShort(52685);
        }
        sysSize = offset;

        for (int i = 0; i < textureCount; i++) {
            Texture tempTex = textures.get(i);
            while (offset != (tempTex.conversionOffset + sysSize)) {
                offset += wf.writeByte(0xCD);
            }
            offset += wf.writeArray(tempTex.data);
        }
        graphicSize = offset - sysSize;

        wrf.write(0x05435352);
        wrf.write(0x00000008);

        flags = getFlags(sysSize, graphicSize);
        wrf.write(flags);
        wrf.writeShort(0xDA78);

        System.out.println("Starting compression");
        wrf.write(compress(wf.getArray()));
    }

    public int makeLayout() {
        System.out.println("Before sort");
        displayTextures();
        sortTextures();
        System.out.println("After sort");
        displayTextures();
        int offset = 0;

        ArrayList<Texture> tempTextures = textures;
        textures = new ArrayList<Texture>();
        int blocksLeft = tempTextures.size() - 1;
        while (blocksLeft >= 0) {
            System.out.println("Blocks left " + blocksLeft);
            if (tempTextures.get(blocksLeft).dataSize > 0x00020000) {
                tempTextures.get(blocksLeft).conversionOffset = offset;
                textures.add(tempTextures.get(blocksLeft));

                offset = writeBlock(tempTextures.get(blocksLeft), offset);
                offset = writeGraphicsPadding(offset);
                tempTextures.remove(blocksLeft);
                blocksLeft = tempTextures.size() - 1;
            } else {
                if (blocksLeft != 0) {
                    int check = 0;
                    if (tempTextures.get(blocksLeft).dataSize + tempTextures.get(check).dataSize > 0x00010000) {
                        tempTextures.get(blocksLeft).conversionOffset = offset;
                        textures.add(tempTextures.get(blocksLeft));

                        offset = writeBlock(tempTextures.get(blocksLeft), offset);
                        offset = writeGraphicsPadding(offset);
                        tempTextures.remove(blocksLeft);
                        blocksLeft = tempTextures.size() - 1;
                    } else {
                        int currentWin = check;
                        check++;
                        while (check < blocksLeft) {
                            if (tempTextures.get(blocksLeft).dataSize + tempTextures.get(check).dataSize > 0x00010000) {
                                break;
                            } else {
                                currentWin = check;
                                check++;
                            }
                        }
                        tempTextures.get(blocksLeft).conversionOffset = offset;
                        textures.add(tempTextures.get(blocksLeft));
                        offset = writeBlock(tempTextures.get(blocksLeft), offset);
                        tempTextures.get(currentWin).conversionOffset = offset;
                        textures.add(tempTextures.get(currentWin));
                        offset = writeBlock(tempTextures.get(currentWin), offset);
                        offset = writeGraphicsPadding(offset);
                        tempTextures.remove(blocksLeft);
                        tempTextures.remove(currentWin);
                        blocksLeft = tempTextures.size() - 1;
                    }
                } else {
                    tempTextures.get(blocksLeft).conversionOffset = offset;
                    textures.add(tempTextures.get(blocksLeft));
                    offset = writeBlock(tempTextures.get(blocksLeft), offset);
                    offset = writeGraphicsPadding(offset);
                    tempTextures.remove(blocksLeft);
                    blocksLeft = tempTextures.size() - 1;
                }
            }
        }
        System.out.println("END");
        displayTextures();
        return offset;
    }

    public int writeBlock(Texture texture, int offset) {
        offset += texture.dataSize;
        while ((offset % 16) != 0) {
            offset++;
        }
        return offset;
    }

    public int writeGraphicsPadding(int retValue) {
        System.out.println("Writing padding 0x00010000");
        if ((retValue % 0x00010000) != 0) {
            System.out.println("We need padding " + retValue);
            while ((retValue % 0x00010000) != 0) {
                retValue++;
            }
        }
        return retValue;
    }

    public void displayTextures() {
        for (int i = 0; i < textures.size(); i++) {
            System.out.println("Texture" + i + " size " + Utils.getHexString(textures.get(i).dataSize) + " offset "
                    + Utils.getHexString(textures.get(i).conversionOffset));
        }
    }

    public void sortTextures() {
        Texture temp = new Texture();

        for (int position = textures.size() - 1; position >= 0; position--) {
            for (int scan = 0; scan <= position - 1; scan++) {
                if (textures.get(scan).dataSize > textures.get(scan + 1).dataSize) {
                    temp = textures.get(scan);
                    textures.set(scan, textures.get(scan + 1));
                    textures.set(scan + 1, temp);
                }
            }
        }
    }

    public byte[] compress(byte[] source) {
        byte[] dest = new byte[source.length + 50];
        Deflater deflater = new Deflater();
        deflater.setInput(source);
        deflater.finish();
        deflater.deflate(dest);

        size = deflater.getTotalOut();
        size += 12;

        deflater.end();

        byte[] ret = new byte[size];
        for (int i = 2; i < size; i++) {
            ret[i - 2] = dest[i];
        }
        return ret;
    }

    public int getFlags(int sysSegSize, int gpuSegSize) {
        int result = (getCompactSize(sysSegSize) & 0x7FFF) | (getCompactSize(gpuSegSize) & 0x7FFF) << 15 | 3 << 30;
        return result;
    }

    public int getCompactSize(int size) {
        int i;
        // sizes must be multiples of 256!
        if ((size % 256) != 0) {
            System.out.println("Size klopt niet");
        }
        size = size >> 8;

        i = 0;
        while ((size % 2) == 0 && size >= 32 && i < 15) {
            i++;
            size = size >> 1;
        }

        return ((i & 0xF) << 11) | (size & 0x7FF);
    }

    public int addPaddingTillOffset(WriteBuffer wf, int currentOffset, int offset) {
        while (currentOffset != offset) { // not sure what this is for but almost every wtd pads till 0x0220
            currentOffset += wf.writeByte(205); // padding
        }
        return offset;
    }

    public int addPaddingToRow(WriteBuffer wf, int size) {
        if ((size % 2) != 0) {
            size += wf.writeByte(0xCD);
        }
        while ((size % 16) != 0) {
            size += wf.writeShort(52685); // padding
        }
        return size;
    }

    public void addTexture(Texture tex) {
        textures.add(tex);
    }

}