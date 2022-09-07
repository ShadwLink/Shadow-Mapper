package nl.shadowlink.tools.shadowlib.model.dff;

import nl.shadowlink.tools.io.WriteBuffer;
import nl.shadowlink.tools.io.WriteFunctions;
import nl.shadowlink.tools.shadowlib.model.model.Model;
import nl.shadowlink.tools.shadowlib.model.model.Polygon;
import nl.shadowlink.tools.shadowlib.model.model.Strip;
import nl.shadowlink.tools.shadowlib.model.model.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.Deflater;

/**
 * @author Shadow-Link
 */
public class NewConvert {
    private Model mdl;

    private int graphicSize = 0;
    private int sysSize = 0;

    // private WriteFunctions wf;
    private WriteBuffer wf;
    private WriteFunctions wrf = null;

    // Using next vars to temporarly store the offsets for offsets that are not know yet
    private ArrayList<Integer> shaderOffsets = new ArrayList();

    private int unknownVectorOffset = 0;

    private ArrayList<Integer> geometryOffset = new ArrayList();
    private ArrayList<Integer> modelOffset = new ArrayList();

    private int shaderGroupInsertOffset = 0;
    private int shaderGroupOffset = 0;

    private int modelCollectionInsertOffset = 0;
    private int modelCollectionOffset = 0;

    private int shaderCollectionInsertOffset = 0;
    private int shaderCollectionOffset = 0;

    private int vertexDeclFlagsInsertOffset = 0;
    private int vertexDeclFlagsOffset = 0;

    private int data3InsertOffset = 0;
    private int data3Offset = 0;

    private int geometryColInsertOffset = 0;
    private int geometryColOffset = 0;

    private int materialMappingInsertOffset = 0;
    private int materialMappingOffset = 0;

    private int modelColInsertOffset = 0;
    private int modelColOffset = 0;

    private int[] textureInsertOffsets;
    private int[] textureOffsets;

    private int[] vertexDeclInsertOffsets;
    private int[] vertexDeclOffsets;

    private int[] shaderParamOffsetInsertOffsets;
    private int[] shaderParamOffsetOffsets;

    private int[] shaderParamTypeInsertOffsets;
    private int[] shaderParamTypeOffsets;

    private int[] shaderParamNameInsertOffsets;
    private int[] shaderParamNameOffsets;

    private int[] shaderNameInsertOffsets;
    private int[] shaderNameOffsets;

    private int[] shaderSPSInsertOffsets;
    private int[] shaderSPSOffsets;

    private int[][] vertexInsertOffsets;
    private int[] vertexOffsets;

    private int[] indexInsertOffsets;
    private int[] indexOffsets;

    private int[] vertexBufferInsertOffsets;
    private int[] vertexBufferOffsets;

    private int[] indexBufferInsertOffsets;
    private int[] indexBufferOffsets;

    private int[] texNameInsertOffsets;
    private int[] texNameOffsets;

    public NewConvert(Model mdl, WriteFunctions wrf) {
        this.wrf = wrf;
        this.mdl = mdl;
        wf = new WriteBuffer();
        System.out.println("Shader count: " + mdl.getElement(0).getShaderCount());
        System.out.println("Geometry count: " + mdl.getElement(0).getStripCount());
        createSystemBlock(); // start writing the system block
        createGraphicsBlock(); // start writing the graphic block
    }

    public void createSystemBlock() {
        // Setup the tempoffset sizes (depends on number of geometries/shaders)
        textureInsertOffsets = new int[mdl.getElement(0).getShaderCount()];
        textureOffsets = new int[mdl.getElement(0).getShaderCount()];

        // vertexDeclInsertOffsets = new int[mdl.getElement(0).getStripCount()];
        // vertexDeclOffsets = new int[mdl.getElement(0).getStripCount()];
        vertexDeclInsertOffsets = new int[mdl.getElement(0).getShaderCount()];
        vertexDeclOffsets = new int[mdl.getElement(0).getShaderCount()];

        shaderParamOffsetInsertOffsets = new int[mdl.getElement(0).getShaderCount()];
        shaderParamOffsetOffsets = new int[mdl.getElement(0).getShaderCount()];

        shaderParamTypeInsertOffsets = new int[mdl.getElement(0).getShaderCount()];
        shaderParamTypeOffsets = new int[mdl.getElement(0).getShaderCount()];

        shaderParamNameInsertOffsets = new int[mdl.getElement(0).getShaderCount()];
        shaderParamNameOffsets = new int[mdl.getElement(0).getShaderCount()];

        shaderNameInsertOffsets = new int[mdl.getElement(0).getShaderCount()];
        shaderNameOffsets = new int[mdl.getElement(0).getShaderCount()];

        shaderSPSInsertOffsets = new int[mdl.getElement(0).getShaderCount()];
        shaderSPSOffsets = new int[mdl.getElement(0).getShaderCount()];

        vertexInsertOffsets = new int[mdl.getElement(0).getStripCount()][2];
        vertexOffsets = new int[mdl.getElement(0).getStripCount()];

        indexInsertOffsets = new int[mdl.getElement(0).getStripCount()];
        indexOffsets = new int[mdl.getElement(0).getStripCount()];

        vertexBufferInsertOffsets = new int[mdl.getElement(0).getStripCount()];
        vertexBufferOffsets = new int[mdl.getElement(0).getStripCount()];

        indexBufferInsertOffsets = new int[mdl.getElement(0).getStripCount()];
        indexBufferOffsets = new int[mdl.getElement(0).getStripCount()];

        texNameInsertOffsets = new int[mdl.getElement(0).getShaderCount()];
        texNameOffsets = new int[mdl.getElement(0).getShaderCount()];

        int offset = 0; // using this far to keep track of the current offset

        offset = writeHeader(offset); // write the header of the wdr

        while (offset != 0x02A0) { // not sure what this is for but almost every wdr pads till 0x02A0
            offset += wf.writeShort(52685); // pading
        }

        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            shaderParamOffsetOffsets[i] = offset;
            shaderParamTypeOffsets[i] = offset + 32;
            shaderParamNameOffsets[i] = offset + 48;
            offset = writeShaderFX(i, offset);
        }

        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            shaderOffsets.add(offset);
            offset = writeShader(i, offset);
        }

        offset = writeShaderGroup(offset);

        for (int i = 0; i < mdl.getElement(0).getStripCount(); i++) {
            offset = writeGeometry(i, offset);
        }

        for (int i = 0; i < mdl.getElement(0).getStripCount(); i++) {
            offset = writeVertexBuffer(i, offset);
        }

        for (int i = 0; i < mdl.getElement(0).getStripCount(); i++) {
            offset = writeIndexBuffer(i, offset);
        }

        unknownVectorOffset = offset;
        for (int i = 0; i < mdl.getElement(0).getStripCount(); i++) {
            offset += wf.writeVector(mdl.getElement(0).getStrip(i).sphere.x, mdl.getElement(0).getStrip(i).sphere.y,
                    mdl.getElement(0).getStrip(i).sphere.z, mdl.getElement(0).getStrip(i).sphere.w);
        }

        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            textureOffsets[i] = offset;
            offset = writeTexture(i, offset);
        }

        offset = writeModel(offset);

        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            texNameOffsets[i] = offset;
            offset += wf.writeString(mdl.getElement(0).getShader(i).getTextureName());
            offset = addPaddingToRow(offset);
        }

        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            vertexDeclOffsets[i] = offset;
            offset = writeShaderStuff(offset);
        }

        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            shaderSPSOffsets[i] = offset;
            offset += wf.writeString("gta_default.sps");
            offset = addPaddingToRow(offset);
        }

        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            shaderNameOffsets[i] = offset;
            offset += wf.writeString("gta_default");
            offset = addPaddingToRow(offset);
        }

        geometryColOffset = offset;
        for (int i = 0; i < mdl.getElement(0).getStripCount(); i++) {
            offset += wf.writeOffset(geometryOffset.get(i));
        }
        offset = addPaddingToRow(offset);

        // modelcollectionpointer
        System.out.println("ModelCollectionOffset " + offset);
        modelCollectionOffset = offset;
        modelColInsertOffset = offset;
        offset += wf.writeOffset(0); // ModelPointers offset
        offset += wf.writeShort(1);
        offset += wf.writeShort(1);
        offset = addPaddingToRow(offset);

        // data3
        data3Offset = offset;
        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            offset += wf.writeInt(1);
        }
        offset = addPaddingToRow(offset);

        // vertexdeclarationusageflags
        vertexDeclFlagsOffset = offset;
        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            offset += wf.writeInt(89);
        }
        offset = addPaddingToRow(offset);

        // shaderoffsets
        shaderCollectionOffset = offset;
        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            offset += wf.writeOffset(shaderOffsets.get(i));
        }
        offset = addPaddingToRow(offset);

        // shadermapping
        materialMappingOffset = offset;
        for (int i = 0; i < mdl.getElement(0).getStripCount(); i++) {
            offset += wf.writeShort(mdl.getElement(0).getStrip(i).getShaderNumber());
        }
        offset = addPaddingToRow(offset);

        // modelpointers
        modelColOffset = offset;
        for (int i = 0; i < 1; i++) { // modelcount
            offset += wf.writeOffset(modelOffset.get(i));
        }
        offset = addPaddingToRow(offset);

        while ((offset % 4096) != 0) {
            if ((offset % 2) != 0) {
                offset += wf.writeByte(0xCD);
            }
            offset += wf.writeShort(52685);
        }

        sysSize = offset;
    }

    public int writeShaderStuff(int offset) {
        offset += wf.writeInt(89);
        offset += wf.writeShort(36); // stridesize
        offset += wf.writeByte(0);
        offset += wf.writeByte(4);

        offset += wf.writeInt(0x55996996);
        offset += wf.writeInt(0x67555555);

        return offset;
    }

    public int writeModel(int offset) {
        modelOffset.add(offset);
        offset += wf.writeInt(7012916); // VTable

        geometryColInsertOffset = offset;
        offset += wf.writeOffset(0); // TEMP offset model collection
        offset += wf.writeShort(mdl.getElement(0).getStripCount());
        offset += wf.writeShort(mdl.getElement(0).getStripCount());

        offset += wf.writeOffset(unknownVectorOffset);

        materialMappingInsertOffset = offset;
        offset += wf.writeOffset(0); // MaterialMappingOffset

        offset += wf.writeShort(0);
        offset += wf.writeShort(205);

        offset += wf.writeShort(0);
        offset += wf.writeShort(mdl.getElement(0).getStripCount());

        offset = addPaddingToRow(offset);

        return offset;
    }

    public int writeTexture(int shader, int offset) {
        offset += wf.writeInt(7038812); // VTable

        offset += wf.writeInt(0);
        offset += wf.writeShort(2);
        offset += wf.writeShort(1);
        offset += wf.writeInt(0);
        offset += wf.writeInt(0);

        texNameInsertOffsets[shader] = offset;
        offset += wf.writeOffset(0); // offset to texturename

        offset += wf.writeInt(0);

        offset = addPaddingToRow(offset);

        return offset;
    }

    public int writeIndexBuffer(int geo, int offset) {
        indexBufferOffsets[geo] = offset;
        offset += wf.writeInt(7059568); // VTable

        offset += wf.writeInt(mdl.getElement(0).getStrip(geo).getPolyCount() * 3); // indexcount

        indexInsertOffsets[geo] = offset;
        offset += wf.writeDataOffset(0x0000); // write dataoffset to index

        offset += wf.writeInt(0); // unknown 0

        // padding
        for (int i = 0; i < 32; i++) {
            offset += wf.writeByte(0xCD);
        }

        return offset;
    }

    public int writeVertexBuffer(int geo, int offset) {
        vertexBufferOffsets[geo] = offset;
        offset += wf.writeInt(7060184); // VTable

        offset += wf.writeShort(mdl.getElement(0).getStrip(geo).getVertexCount()); // vertex count

        offset += wf.writeShort(0);

        vertexInsertOffsets[geo][0] = offset;
        offset += wf.writeDataOffset(0x0000); // write offset to vertex data
        offset += wf.writeInt(36); // vertex stride

        vertexDeclInsertOffsets[geo] = offset;
        offset += wf.writeOffset(0); // vertex decl offset TEMP

        offset += wf.writeInt(0); // unknown

        vertexInsertOffsets[geo][1] = offset;
        offset += wf.writeDataOffset(0x0000); // write offset to vertex data again
        offset += wf.writeInt(0);

        // padding
        for (int i = 0; i < 32; i++) {
            offset += wf.writeByte(0xCD);
        }

        return offset;
    }

    public int writeGeometry(int geo, int offset) {
        geometryOffset.add(offset);

        offset += wf.writeInt(7031028); // VTable

        offset += wf.writeInt(0);
        offset += wf.writeInt(0);

        vertexBufferInsertOffsets[geo] = offset;
        offset += wf.writeOffset(0); // vertexbuffer offset

        offset += wf.writeInt(0);
        offset += wf.writeInt(0);
        offset += wf.writeInt(0);

        indexBufferInsertOffsets[geo] = offset;
        offset += wf.writeOffset(0); // temp indexbuffer offset

        offset += wf.writeInt(0);
        offset += wf.writeInt(0);
        offset += wf.writeInt(0);

        offset += wf.writeInt(mdl.getElement(0).getStrip(geo).getPolyCount() * 3); // indexcount

        offset += wf.writeInt(mdl.getElement(0).getStrip(geo).getPolyCount()); // faceCount

        offset += wf.writeShort(mdl.getElement(0).getStrip(geo).getVertexCount()); // needs to be strip

        offset += wf.writeShort(3); // vertex type

        offset += wf.writeInt(0);

        offset += wf.writeInt(36); // vertex stride

        offset += wf.writeInt(0);
        offset += wf.writeInt(0);
        offset += wf.writeInt(0);

        offset = addPaddingToRow(offset);

        return offset;
    }

    public int writeShaderGroup(int offset) {
        shaderGroupOffset = offset;

        offset += wf.writeInt(7018052); // VTable
        offset += wf.writeInt(0); // TexDic offset

        shaderCollectionInsertOffset = offset;
        offset += wf.writeOffset(0); // temp offset
        offset += wf.writeShort(mdl.getElement(0).getShaderCount());
        offset += wf.writeShort(mdl.getElement(0).getShaderCount());

        for (int i = 0; i < 12; i++) { // 12x 0
            offset += wf.writeInt(0);
        }

        vertexDeclFlagsInsertOffset = offset;
        offset += wf.writeOffset(0); // temp offset
        offset += wf.writeShort(mdl.getElement(0).getShaderCount());
        offset += wf.writeShort(mdl.getElement(0).getShaderCount());

        data3InsertOffset = offset;
        offset += wf.writeOffset(0); // temp offset
        offset += wf.writeShort(mdl.getElement(0).getShaderCount());
        offset += wf.writeShort(mdl.getElement(0).getShaderCount());

        return offset;
    }

    public int writeShader(int shader, int offset) {
        offset += wf.writeInt(7021116); // VTable
        offset += wf.writeInt(0); // BlockMapAdress

        offset += wf.writeShort(2);
        offset += wf.writeByte(1);
        offset += wf.writeByte(1);
        offset += wf.writeByte(0);
        offset += wf.writeByte(0);
        offset += wf.writeShort(1);
        offset += wf.writeInt(0);

        shaderParamOffsetInsertOffsets[shader] = offset;
        offset += wf.writeOffset(0); // offset to paramoffsets
        offset += wf.writeInt(0);
        offset += wf.writeInt(6);

        offset += wf.writeInt(208);

        shaderParamTypeInsertOffsets[shader] = offset;
        offset += wf.writeOffset(0); // offset to paramTypes
        offset += wf.writeInt(2066433082); // hash
        offset += wf.writeInt(0);

        offset += wf.writeInt(0);
        shaderParamNameInsertOffsets[shader] = offset;
        offset += wf.writeOffset(0); // offset to paramTypes
        offset += wf.writeInt(0);
        offset += wf.writeInt(0);
        offset += wf.writeInt(0);

        shaderNameInsertOffsets[shader] = offset;
        offset += wf.writeOffset(0); // temp offset
        shaderSPSInsertOffsets[shader] = offset;
        offset += wf.writeOffset(0); // temp offset

        offset += wf.writeInt(0);
        offset += wf.writeInt(0);
        offset += wf.writeInt(113);
        offset += wf.writeInt(0);

        offset = addPaddingToRow(offset);

        return offset;
    }

    public int writeShaderFX(int shader, int offset) {
        // write the paramoffset
        int nextOffset = offset + 80;
        offset += wf.writeOffset(nextOffset); // vector 1
        nextOffset += 16;
        offset += wf.writeOffset(nextOffset); // vector 2
        nextOffset += 0;
        textureInsertOffsets[shader] = offset;
        offset += wf.writeOffset(0); // texture not sure how to calculate this
        nextOffset += 16;
        offset += wf.writeOffset(nextOffset); // vector 3
        nextOffset += 16;
        offset += wf.writeOffset(nextOffset); // Matrix
        nextOffset += 64;
        offset += wf.writeOffset(nextOffset); // Vector

        offset = addZeroPaddingToRow(offset);

        // write the param types

        offset += wf.writeByte(1);
        offset += wf.writeByte(1);
        offset += wf.writeByte(0);
        offset += wf.writeByte(1);
        offset += wf.writeByte(4);
        offset += wf.writeByte(1);

        offset = addZeroPaddingToRow(offset);

        // write shader names

        offset += wf.writeInt(-677643234);
        offset += wf.writeInt(-1168850544);
        offset += wf.writeInt(726757629);
        offset += wf.writeInt(-64241494);
        offset += wf.writeInt(-260861532);
        offset += wf.writeInt(-2078982697);

        offset = addZeroPaddingToRow(offset);

        // write shaderParams

        offset += wf.writeVector(1.0f, 0.0f, 0.0f, 0.0f);
        offset += wf.writeVector(0.0f, 1.0f, 0.0f, 0.0f);
        offset += wf.writeVector(1280.0f, 0.0f, 0.0f, 0.0f);

        // write matrix
        offset += wf.writeVector(-1.0f, 0.0f, 0.0f, 0.0f);
        offset += wf.writeVector(1.0f, 0.0f, 0.0f, 0.0f);
        offset += wf.writeVector(0.0f, -1.0f, 0.0f, 0.0f);
        offset += wf.writeVector(0.0f, 1.0f, 0.0f, 0.0f);

        offset += wf.writeVector(0.2125f, 0.7154f, 0.0721f, 0.0f);

        return offset;
    }

    public int writeHeader(int offset) {
        int blockMapAdress = 0x0090;
        offset += wf.writeInt(6902356); // vtable
        offset += wf.writeOffset(blockMapAdress); // blockmapadress
        shaderGroupInsertOffset = offset;
        offset += wf.writeOffset(shaderGroupOffset); // shaderlistoffset TEMP
        offset += wf.writeInt(0); // skeletonoffset

        Vertex center = mdl.getElement(0).getCenter();
        Vertex max = mdl.getElement(0).getMax();
        Vertex min = mdl.getElement(0).getMin();
        offset += wf.writeVector(center.getVertexX(), center.getVertexY(), center.getVertexZ(), -1); // center
        offset += wf.writeVector(min.getVertexX(), min.getVertexY(), min.getVertexZ(), -1); // boundsmin
        offset += wf.writeVector(max.getVertexX(), max.getVertexY(), max.getVertexZ(), -1); // boundsmax

        // write the detailOffset, we keep it 1 for now.. (max 4)
        modelCollectionInsertOffset = offset;
        offset += wf.writeOffset(modelCollectionOffset);  // this pointer points to a pointer collection TEMP
        for (int i = 0; i < 3; i++) {
            offset += wf.writeInt(0);
        }

        offset += wf.writeVector(9999.0f, 9999.0f, 9999.0f, 9999.0f); // absolute max

        offset += wf.writeInt(1); // unkown
        offset += wf.writeInt(-1); // unkown
        offset += wf.writeInt(-1); // unkown
        offset += wf.writeInt(-1); // unkown

        offset += wf.writeFloat(8.013029f); // unkown

        for (int i = 0; i < 5; i++) {
            offset += wf.writeInt(0); // write the row of zeros
        }

        offset = addPaddingToRow(offset); // add padding

        offset += wf.writeInt(0); // write to block map adress

        offset = addPaddingToRow(offset); // add padding
        return offset;
    }

    public int addPaddingToRow(int size) {
        if ((size % 2) != 0) {
            size += wf.writeByte(0xCD);
        }
        while ((size % 16) != 0) {
            size += wf.writeShort(52685); // pading
        }
        return size;
    }

    public int addZeroPaddingToRow(int size) {
        while ((size % 16) != 0) {
            size += wf.writeByte(0); // pading
        }
        return size;
    }

    public int makeLayout() {
        int offset = 0;

        LinkedList<BlockInfo> blocks = createBlocks();
        int blocksLeft = blocks.size() - 1;
        while (blocksLeft >= 0) {
            System.out.println("Blocks left " + blocksLeft);
            if (blocks.get(blocksLeft).size > 0x00010000) {
                offset = writeBlock(blocks.get(blocksLeft), offset);
                offset = writeGraphicsPadding(offset);
                blocks.remove(blocksLeft);
                blocksLeft = blocks.size() - 1;
            } else {
                if (blocksLeft != 0) {
                    int check = 0;
                    if (blocks.get(blocksLeft).size + blocks.get(check).size > 0x00010000) {
                        offset = writeBlock(blocks.get(blocksLeft), offset);
                        offset = writeGraphicsPadding(offset);
                        blocks.remove(blocksLeft);
                        blocksLeft = blocks.size() - 1;
                    } else {
                        int currentWin = check;
                        check++;
                        while (check < blocksLeft) {
                            if (blocks.get(blocksLeft).size + blocks.get(check).size > 0x00010000) {
                                break;
                            } else {
                                currentWin = check;
                                check++;
                            }
                        }
                        offset = writeBlock(blocks.get(blocksLeft), offset);
                        offset = writeBlock(blocks.get(currentWin), offset);
                        offset = writeGraphicsPadding(offset);
                        blocks.remove(blocksLeft);
                        blocks.remove(currentWin);
                        blocksLeft = blocks.size() - 1;
                    }
                } else {
                    offset = writeBlock(blocks.get(blocksLeft), offset);
                    offset = writeGraphicsPadding(offset);
                    blocks.remove(blocksLeft);
                    blocksLeft = blocks.size() - 1;
                }
            }
        }
        return offset;
    }

    public int writeGraphicsPadding(int retValue) {
        System.out.println("Writing padding 0x00010000");
        if ((retValue % 0x00010000) != 0) {
            System.out.println("We need padding " + retValue);
            while ((retValue % 0x00010000) != 0) {
                wf.writeShort(52685);
                retValue += 2;
            }
        }
        return retValue;
    }

    public int writeBlock(BlockInfo block, int offset) {
        Strip strip = mdl.getElement(0).getStrip(block.getStrip());
        if (block.getType() == 1) {
            vertexOffsets[block.getStrip()] = offset;
            // write vertices
            for (int i = 0; i < strip.getVertexCount(); i++) {
                Vertex vert = strip.getVertex(i);
                wf.writeFloat(vert.getVertexX());
                wf.writeFloat(vert.getVertexY());
                wf.writeFloat(vert.getVertexZ());

                if (mdl.getElement(0).hasNormals()) {
                    wf.writeFloat(vert.getNormX());
                    wf.writeFloat(vert.getNormY());
                    wf.writeFloat(vert.getNormZ());
                } else {
                    wf.writeFloat(0.0f); // normal x
                    wf.writeFloat(0.0f); // normal y
                    wf.writeFloat(0.0f); // normal z
                }

                if (!vert.hasVertexColors()) {
                    for (int i2 = 0; i2 < 4; i2++) {
                        wf.writeByte(0xFF);
                    }
                } else {
                    // System.out.println("Writing vert unk");
                    wf.writeByte(vert.getRed());
                    wf.writeByte(vert.getGreen());
                    wf.writeByte(vert.getBlue());
                    wf.writeByte(vert.getAlpha());
                }

                wf.writeFloat(vert.getVertexU());
                wf.writeFloat(vert.getVertexV());

                offset += 36;
            }
        } else {
            indexOffsets[block.getStrip()] = offset;
            for (int i = 0; i < strip.getPolyCount(); i++) {
                Polygon poly = strip.getPoly(i);
                wf.writeShort(poly.a);
                wf.writeShort(poly.b);
                wf.writeShort(poly.c);
                offset += 6;
            }
        }
        if ((offset % 16) != 0) {
            System.out.println("We need padding " + offset);
            while ((offset % 16) != 0) {
                wf.writeShort(52685);
                offset += 2;
            }
        }
        return offset;
    }

    public LinkedList<BlockInfo> createBlocks() {
        System.out.println("Creating blocks " + mdl.getElement(0).getStripCount());
        LinkedList<BlockInfo> blocks = new LinkedList();
        System.out.println("Blocks length " + blocks.size());
        for (int currentStrip = 0; currentStrip < mdl.getElement(0).getStripCount(); currentStrip++) {
            BlockInfo block = new BlockInfo(currentStrip);
            block.setStrip(currentStrip);
            block.setType(1);
            block.setSize(mdl.getElement(0).getStrip(currentStrip).getVertexCount() * 36);
            blocks.add(block);

            block = new BlockInfo((currentStrip + 1) * 2 - 1);
            block.setStrip(currentStrip);
            block.setType(2);
            block.setSize(mdl.getElement(0).getStrip(currentStrip).getPolyCount() * 6);
            blocks.add(block);
        }
        for (int i = 0; i < blocks.size(); i++) {
            System.out.println("Block " + i + " size " + Integer.toString(blocks.get(i).size, 16) + " type " + blocks.get(i).type);
        }
        blocks = sortBlocks(blocks);
        for (int i = 0; i < blocks.size(); i++) {
            System.out.println("Block " + i + " size " + Integer.toString(blocks.get(i).size, 16) + " type " + blocks.get(i).type);
        }
        return blocks;
    }

    public LinkedList<BlockInfo> sortBlocks(LinkedList<BlockInfo> blocks) {
        BlockInfo temp = new BlockInfo(-1);

        for (int position = blocks.size() - 1; position >= 0; position--) {
            for (int scan = 0; scan <= position - 1; scan++) {
                if (blocks.get(scan).size > blocks.get(scan + 1).size) {
                    temp = blocks.get(scan);
                    blocks.set(scan, blocks.get(scan + 1));
                    blocks.set(scan + 1, temp);
                }
            }
        }
        return blocks;
    }

    public void createGraphicsBlock() {
        int size = makeLayout(); // using this var to keep track of the current offset

        while ((size % 4096) != 0) {
            wf.writeShort(52685);
            size += 2;
        }
        graphicSize = size;

        System.out.println("Finished writing graphics block");

        // replace all temp offsets with good offsets
        wf.replaceOffset(shaderGroupInsertOffset, shaderGroupOffset);
        wf.replaceOffset(modelCollectionInsertOffset, modelCollectionOffset);
        wf.replaceOffset(shaderCollectionInsertOffset, shaderCollectionOffset);
        wf.replaceOffset(vertexDeclFlagsInsertOffset, vertexDeclFlagsOffset);
        wf.replaceOffset(data3InsertOffset, data3Offset);
        wf.replaceOffset(geometryColInsertOffset, geometryColOffset);
        wf.replaceOffset(materialMappingInsertOffset, materialMappingOffset);
        wf.replaceOffset(modelColInsertOffset, modelColOffset);

        for (int i = 0; i < mdl.getElement(0).getShaderCount(); i++) {
            wf.replaceOffset(shaderNameInsertOffsets[i], shaderNameOffsets[i]);
            wf.replaceOffset(shaderSPSInsertOffsets[i], shaderSPSOffsets[i]);
            wf.replaceOffset(shaderParamOffsetInsertOffsets[i], shaderParamOffsetOffsets[i]);
            wf.replaceOffset(shaderParamTypeInsertOffsets[i], shaderParamTypeOffsets[i]);
            wf.replaceOffset(shaderParamNameInsertOffsets[i], shaderParamNameOffsets[i]);
            wf.replaceOffset(textureInsertOffsets[i], textureOffsets[i]);
            wf.replaceOffset(texNameInsertOffsets[i], texNameOffsets[i]);
        }

        for (int i = 0; i < mdl.getElement(0).getStripCount(); i++) {
            wf.replaceOffset(vertexBufferInsertOffsets[i], vertexBufferOffsets[i]);
            wf.replaceOffset(indexBufferInsertOffsets[i], indexBufferOffsets[i]);
            wf.replaceOffset(vertexDeclInsertOffsets[i], vertexDeclOffsets[i]);
            wf.replaceDataOffset(vertexInsertOffsets[i][0], vertexOffsets[i]);
            wf.replaceDataOffset(vertexInsertOffsets[i][1], vertexOffsets[i]);
            wf.replaceDataOffset(indexInsertOffsets[i], indexOffsets[i]);
        }

        compressAndSave(wf.getArray()); // compress and save the file
    }

    private void compressAndSave(byte[] dataBuffer) {
        wrf.write(0x05435352);
        wrf.write(0x0000006E);

        mdl.flags = getFlags(sysSize, graphicSize);
        wrf.write(getFlags(sysSize, graphicSize));
        wrf.writeShort(0xDA78);

        System.out.println("Starting compression");
        wrf.write(compress(dataBuffer));
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

    public int getFlags(int sysSegSize, int gpuSegSize) {
        int result = (getCompactSize(sysSegSize) & 0x7FFF) | (getCompactSize(gpuSegSize) & 0x7FFF) << 15 | 3 << 30;
        return result;
    }

    public byte[] compress(byte[] source) {
        byte[] dest = new byte[source.length + 50];
        Deflater deflater = new Deflater();
        deflater.setInput(source);
        deflater.finish();
        deflater.deflate(dest);

        int size = deflater.getTotalOut();
        mdl.size = size + 12;
        deflater.end();

        byte[] ret = new byte[size];
        for (int i = 2; i < size; i++) {
            ret[i - 2] = dest[i];
        }
        return ret;
    }

}
