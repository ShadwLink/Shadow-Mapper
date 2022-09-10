package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.utils.Utils;

/**
 * @author Shadow-Link
 */
public class Geometry {
    private int startOffset;

    private int VTable;

    private int Unknown1;
    private int Unknown2;

    public int vertexBuffersOffset;

    private int Unknown3;
    private int Unknown4;
    private int Unknown5;

    public int indexBuffersOffset;

    private int Unknown6;
    private int Unknown7;
    private int Unknown8;
    public int IndexCount;
    public int FaceCount;
    public int VertexCount;
    public int PrimitiveType;    // RAGE_PRIMITIVE_TYPE
    private int Unknown9;
    public int VertexStride;
    private int Unknown10;
    private int Unknown11;
    private int Unknown12;
    private int Unknown13;

    public VertexBuffer vertexBuffer;
    public IndexBuffer indexBuffer;

    public void read(ByteReader br) {
        // Message.displayMsgHigh("Geometry");

        startOffset = br.getCurrentOffset();

        VTable = br.readUInt32();

        // Message.displayMsgHigh("VTable: " + VTable);

        Unknown1 = br.readUInt32();
        Unknown2 = br.readUInt32();

        vertexBuffersOffset = br.readOffset();
        // Message.displayMsgHigh("VertexBufferOffset: " + vertexBuffersOffset);
        Unknown3 = br.readUInt32();
        Unknown4 = br.readUInt32();
        Unknown5 = br.readUInt32();

        indexBuffersOffset = br.readOffset();
        // Message.displayMsgHigh("IndexBufferOffset: " + indexBuffersOffset);
        Unknown6 = br.readUInt32();
        Unknown7 = br.readUInt32();
        Unknown8 = br.readUInt32();

        IndexCount = br.readUInt32();
        FaceCount = br.readUInt32();
        VertexCount = br.readUInt16();
        PrimitiveType = br.readUInt16();

        // Message.displayMsgHigh("IC: " + IndexCount);
        // Message.displayMsgHigh("FC: " + FaceCount);
        // Message.displayMsgHigh("VC: " + VertexCount);
        // Message.displayMsgHigh("PT: " + PrimitiveType);

        Unknown9 = br.readUInt32();

        VertexStride = br.readUInt16();
        Unknown10 = br.readUInt16();

        Unknown11 = br.readUInt32();
        Unknown12 = br.readUInt32();
        Unknown13 = br.readUInt32();

        // Data

        br.setCurrentOffset(vertexBuffersOffset);
        vertexBuffer = new VertexBuffer(br);

        br.setCurrentOffset(indexBuffersOffset);
        indexBuffer = new IndexBuffer(br);
    }

    public String[] getDataNames() {
        String[] names = new String[20];

        names[0] = "Unknown1";
        names[1] = "Unknown2";

        names[2] = "vertexBuffersOffset";

        names[3] = "Unknown3";
        names[4] = "Unknown4";
        names[5] = "Unknown5";

        names[6] = "indexBuffersOffset";

        names[7] = "Unknown6";
        names[8] = "Unknown7";
        names[9] = "Unknown8";
        names[10] = "IndexCount";
        names[11] = "FaceCount";
        names[12] = "VertexCount";
        names[13] = "PrimitiveType";    // RAGE_PRIMITIVE_TYPE
        names[14] = "Unknown9";
        names[15] = "VertexStride";
        names[16] = "Unknown10";
        names[17] = "Unknown11";
        names[18] = "Unknown12";
        names[19] = "Unknown13";

        return names;
    }

    public String[] getDataValues() {
        String[] values = new String[20];

        values[0] = "" + Unknown1;
        values[1] = "" + Unknown2;

        values[2] = Utils.getHexString(vertexBuffersOffset);

        values[3] = "" + Unknown3;
        values[4] = "" + Unknown4;
        values[5] = "" + Unknown5;

        values[6] = Utils.getHexString(indexBuffersOffset);

        values[7] = "" + Unknown6;
        values[8] = "" + Unknown7;
        values[9] = "" + Unknown8;
        values[10] = "" + IndexCount;
        values[11] = "" + FaceCount;
        values[12] = "" + VertexCount;
        values[13] = "" + PrimitiveType;    // RAGE_PRIMITIVE_TYPE
        values[14] = "" + Unknown9;
        values[15] = "" + VertexStride;
        values[16] = "" + Unknown10;
        values[17] = "" + Unknown11;
        values[18] = "" + Unknown12;
        values[19] = "" + Unknown13;

        return values;
    }

    public String toString() {
        return "Geometry";
    }
}
