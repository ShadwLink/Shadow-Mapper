package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class VertexDeclaration {
    public int UsageFlags;
    public int Stride;
    public byte AlterateDecoder;
    public byte Type;
    public long DeclarationTypes;

    public VertexDeclaration(ByteReader br) {
        Read(br);
    }

    /* private static VertexElementType GetType(long typeDecl, int index) { return (VertexElementType)((typeDecl >> (4 *
     * index)) & 0xF); } private static int GetSize(VertexElementType type) { int[] sizeMapping = {2, 4, 6, 8, 4, 8, 12,
     * 16, 4, 4, 4, 0, 0, 0, 0, 0}; return sizeMapping[(int) type]; } private void
     * DecodeSingleElement(ICollection<VertexElement> list, int index, int streamIndex, VertexElementUsage usage, int
     * usageIndex){ DecodeSingleElement(list, index, streamIndex, usage, ref usageIndex); } private void
     * DecodeSingleElement(ICollection<VertexElement> list, int index, int streamIndex, VertexElementUsage usage, ref
     * int usageIndex) { int declTypes = DeclarationTypes; int usageFlags = UsageFlags; int usageFlagMask = (uint)(1 <<
     * index); int type = GetType(declTypes, index); int size = GetSize(type); if ((usageFlags & usageFlagMask) != 0) {
     * var element = new VertexElement() { UsageIndex = usageIndex++, StreamIndex = streamIndex, Usage = usage, Type =
     * type, Size = size, }; list.add(element); } } public VertexElement[] DecodeAsVertexElements(){ var elements = new
     * List<VertexElement>(); int streamIndex = 0; int usageIndexPosition = 0; int usageIndexBlendWeight = 0; int
     * usageIndexBlendIndices = 0; int usageIndexNormal = 0; int usageIndexTexture = 0; int usageIndexTangent = 0; int
     * usageIndexBinormal = 0; DecodeSingleElement(elements, 0, streamIndex, VertexElementUsage.Position, ref
     * usageIndexPosition); DecodeSingleElement(elements, 1, streamIndex, VertexElementUsage.BlendWeight, ref
     * usageIndexBlendWeight); DecodeSingleElement(elements, 2, streamIndex, VertexElementUsage.BlendIndices, ref
     * usageIndexBlendIndices); DecodeSingleElement(elements, 3, streamIndex, VertexElementUsage.Normal, ref
     * usageIndexNormal); DecodeSingleElement(elements, 4, streamIndex, VertexElementUsage.Color, 0); // Diffuse?
     * DecodeSingleElement(elements, 5, streamIndex, VertexElementUsage.Color, 1); // Specular? for(int i = 6; i<14;
     * i++) // 8 { DecodeSingleElement(elements, i, streamIndex, VertexElementUsage.TextureCoordinate, ref
     * usageIndexTexture); } DecodeSingleElement(elements, 14, streamIndex, VertexElementUsage.Tangent, ref
     * usageIndexTangent); DecodeSingleElement(elements, 15, streamIndex, VertexElementUsage.Binormal, ref
     * usageIndexBinormal); return elements.ToArray(); } */

    public void Read(ByteReader br) {
        UsageFlags = br.readUInt32();
        Stride = br.readUInt16();
        AlterateDecoder = br.readByte();
        Type = br.readByte();

        br.readUInt32();
        br.readUInt32();
        // DeclarationTypes = br.readUInt64();
    }

    public String[] getDataNames() {
        ArrayList<String> nameList = new ArrayList();

        nameList.add("UsageFlags");
        nameList.add("Stride");
        nameList.add("AlterateDecoder");
        nameList.add("Type");

        String[] names = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            names[i] = nameList.get(i);
        }

        return names;
    }

    public String[] getDataValues() {
        ArrayList<String> nameList = new ArrayList();

        nameList.add("" + UsageFlags);
        nameList.add("" + Stride);
        nameList.add("" + AlterateDecoder);
        nameList.add("" + Type);

        String[] names = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            names[i] = nameList.get(i);
        }

        return names;
    }

}
