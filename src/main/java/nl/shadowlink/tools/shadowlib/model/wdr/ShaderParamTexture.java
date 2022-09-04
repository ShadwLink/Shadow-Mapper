package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.utils.Utils;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class ShaderParamTexture {
    public int startOffset;
    private int VTable;
    private int Unknown1;
    private int Unknown2;
    private int Unknown3;
    private int Unknown4;
    private int Unknown5;
    private int TextureNameOffset;
    private int Unknown7;

    public String TextureName = "";

    public void read(ByteReader br) {
        startOffset = br.getCurrentOffset();
        VTable = br.readUInt32();

        Unknown1 = br.readUInt32();
        Unknown2 = br.readUInt16();
        Unknown3 = br.readUInt16();
        Unknown4 = br.readUInt32();
        Unknown5 = br.readUInt32();
        TextureNameOffset = br.readOffset();
        Unknown7 = br.readUInt32();

        br.setCurrentOffset(TextureNameOffset);
        TextureName = br.readNullTerminatedString();
        // Message.displayMsgHigh("Texture name: " + TextureName);
    }

    public String[] getDataNames() {
        ArrayList<String> nameList = new ArrayList();

        nameList.add("VTable");
        nameList.add("Unknown1");
        nameList.add("Unknown2");
        nameList.add("Unknown3");
        nameList.add("Unknown4");
        nameList.add("Unknown5");
        nameList.add("TextureNameOffset");
        nameList.add("Unknown7");

        nameList.add("TextureName");

        String[] names = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            names[i] = nameList.get(i);
        }

        return names;
    }

    public String[] getDataValues() {
        ArrayList<String> valueList = new ArrayList();

        valueList.add("" + VTable);
        valueList.add("" + Unknown1);
        valueList.add("" + Unknown2);
        valueList.add("" + Unknown3);
        valueList.add("" + Unknown4);
        valueList.add("" + Unknown5);
        valueList.add("" + Utils.getHexString(TextureNameOffset));
        valueList.add("" + Unknown7);

        valueList.add("" + TextureName);

        String[] values = new String[valueList.size()];
        for (int i = 0; i < valueList.size(); i++) {
            values[i] = valueList.get(i);
        }

        return values;
    }

    public String getStartOffset() {
        return Utils.getStartOffset(startOffset);
    }

}
