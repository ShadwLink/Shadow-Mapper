package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.model.collections.PtrCollection;
import nl.shadowlink.tools.shadowlib.model.collections.SimpleArray;
import nl.shadowlink.tools.shadowlib.model.collections.SimpleCollection;
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic;
import nl.shadowlink.tools.shadowlib.utils.Utils;

/**
 * @author Shadow-Link
 */
public class ShaderGroup {

    private int startOffset;

    public int VTable;
    public int TextureDictionaryOffset;
    // public TextureFile TextureDictionary;

    public TextureDic wtd;

    public PtrCollection<ShaderFx> Shaders;

    private SimpleArray<Integer> Zeros;

    private SimpleCollection<Integer> VertexDeclarationUsageFlags;

    private SimpleCollection<Integer> Data3;

    public ShaderGroup(ByteReader br) {
        Read(br);
    }

    public void Read(ByteReader br) {
        startOffset = br.getCurrentOffset();

        VTable = br.readUInt32();

        TextureDictionaryOffset = br.readOffset();

        if (TextureDictionaryOffset != -1) {
            // //Message.displayMsgHigh("There is a wtd: " + Utils.getHexString(TextureDictionaryOffset));
            // int save = br.getCurrentOffset();
            // br.setCurrentOffset(TextureDictionaryOffset);

            // wtd = new TextureDic("", br, 3, null, 0);

            // br.setCurrentOffset(save);
        }

        Shaders = new PtrCollection<ShaderFx>(br, 3);

        Zeros = new SimpleArray<Integer>(br, 12, 0);

        VertexDeclarationUsageFlags = new SimpleCollection<Integer>(br, 0);

        Data3 = new SimpleCollection<Integer>(br, 0);
    }

    public String[] getDataNames() {
        String[] names = new String[6];
        names[0] = "VTable";
        names[1] = "TextureDictionaryOffset";
        names[2] = "Shaders(Length)";
        names[3] = "Zeros(Length)";              // byte bLocked, byte align
        names[4] = "VertexDeclarationUsageFlags(Length)";       // pLockedData
        names[5] = "Data3(Length)";

        return names;
    }

    public String[] getDataValues() {
        String[] values = new String[6];
        values[0] = "" + VTable;
        values[1] = Utils.getHexString(TextureDictionaryOffset);
        values[2] = "" + Shaders.Count;              // byte bLocked, byte align
        values[3] = "" + Zeros.Count;       // pLockedData
        values[4] = "" + VertexDeclarationUsageFlags.Count;
        values[5] = "" + Data3.Count;

        return values;
    }

    public String getStartOffset() {
        return Utils.getStartOffset(startOffset);
    }

}
