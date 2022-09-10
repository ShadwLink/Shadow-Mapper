package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.utils.Utils;

/**
 * @author Shadow-Link
 */
public class VertexBuffer {
    public int startOffset;

    public int VTable;
    public int VertexCount;
    public int Unknown1;              // byte bLocked, byte align
    public int DataOffset;       // pLockedData
    public int StrideSize;
    public int vertexDeclOffset;
    public int Unknown2;
    public int DataOffset2;             // piVertexBuffer
    public int p2Offset;

    public byte[] RawData;

    public VertexDeclaration VertexDeclaration;

    public VertexBuffer() {
    }

    public VertexBuffer(ByteReader br) {
        Read(br);
    }

    public void ReadData(ByteReader br) {
        br.setSystemMemory(false);
        br.setCurrentOffset(DataOffset);
        RawData = br.toArray(VertexCount * StrideSize);
        br.setSystemMemory(true);
    }

    public void Read(ByteReader br) {
        startOffset = br.getCurrentOffset();
        VTable = br.readUInt32();

        VertexCount = br.readUInt16();
        Unknown1 = br.readUInt16();

        DataOffset = br.readDataOffset();
        // Message.displayMsgHigh("DataOffset: " + DataOffset);

        StrideSize = br.readUInt32();

        vertexDeclOffset = br.readOffset();

        Unknown2 = br.readUInt32();

        DataOffset2 = br.readDataOffset();
        // Message.displayMsgHigh("DataOffset2: " + DataOffset2);

        p2Offset = br.readOffset(); // null

        ReadData(br);

        //

        br.setCurrentOffset(vertexDeclOffset);
        VertexDeclaration = new VertexDeclaration(br);
    }

    public String[] getDataNames() {
        String[] names = new String[10];
        names[0] = "VTable";
        names[1] = "VertexCount";
        names[2] = "Unknown1";              // byte bLocked, byte align
        names[3] = "DataOffset";       // pLockedData
        names[4] = "StrideSize";
        names[5] = "vertexDeclOffset";
        names[6] = "Unknown2";
        names[7] = "DataOffset2";             // piVertexBuffer
        names[8] = "p2Offset";

        names[9] = "RawData(Length)";

        return names;
    }

    public String[] getDataValues() {
        String[] values = new String[10];
        values[0] = "" + VTable;
        values[1] = "" + VertexCount;
        values[2] = "" + Unknown1;              // byte bLocked, byte align
        values[3] = Utils.getHexString(DataOffset);       // pLockedData
        values[4] = "" + StrideSize;
        values[5] = Utils.getHexString(vertexDeclOffset);
        values[6] = "" + Unknown2;
        values[7] = Utils.getHexString(DataOffset2);             // piVertexBuffer
        values[8] = Utils.getHexString(p2Offset);

        values[9] = "" + RawData.length;

        return values;
    }
}
