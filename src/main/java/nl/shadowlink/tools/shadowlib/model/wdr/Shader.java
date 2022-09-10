package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.model.collections.SimpleArray;
import nl.shadowlink.tools.shadowlib.utils.Utils;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class Shader {
    private int startOffset;

    private int VTable;
    private int BlockMapAdress;

    private int Unknown1;
    private byte Unknown2;
    private byte Unknown3;
    private int Unknown4;
    private int Unknown4_1;
    private int Unknown5;
    private int Unknown6;

    public int shaderParamOffsetsOffset;

    public int ShaderParamCount;
    private int Unknown8;

    public int shaderParamTypesOffset;

    public int shaderParamNameOffset;

    public int Hash;
    private int Unknown9;
    private int Unknown10;
    private int Unknown11;
    private int Unknown12;
    private int Unknown13;

    public SimpleArray<Integer> ShaderParamOffsets;
    public SimpleArray<Byte> ShaderParamTypes;
    public SimpleArray<Integer> ShaderParamNames;

    public ArrayList ShaderParams = new ArrayList();

    /* public Dictionary<ParamNameHash, IShaderParam> ShaderParams { get; private set; } public T
     * GetInfoData<T>(ParamNameHash hash) where T : class, IShaderParam { IShaderParam value;
     * ShaderParams.TryGetValue(hash, out value); return value as T; } */

    public Shader(ByteReader br) {
        startOffset = br.getCurrentOffset();

        VTable = br.readUInt32();
        BlockMapAdress = br.readOffset();

        Unknown1 = br.readUInt16();
        Unknown2 = br.readByte();
        Unknown3 = br.readByte();

        Unknown4 = br.readUInt16();
        Unknown4_1 = br.readUInt16();

        Unknown5 = br.readUInt32();

        shaderParamOffsetsOffset = br.readOffset();

        Unknown6 = br.readUInt32();
        ShaderParamCount = br.readUInt32();
        Unknown8 = br.readUInt32();

        shaderParamTypesOffset = br.readOffset();

        Hash = br.readUInt32();
        Unknown9 = br.readUInt32();
        Unknown10 = br.readUInt32();

        shaderParamNameOffset = br.readOffset();

        Unknown11 = br.readUInt32();
        Unknown12 = br.readUInt32();
        Unknown13 = br.readUInt32();

        int save = br.getCurrentOffset();

        br.setCurrentOffset(shaderParamOffsetsOffset);
        ShaderParamOffsets = new SimpleArray<Integer>(br, ShaderParamCount, 1);

        br.setCurrentOffset(shaderParamTypesOffset);
        ShaderParamTypes = new SimpleArray<Byte>(br, ShaderParamCount, 2);

        br.setCurrentOffset(shaderParamNameOffset);
        ShaderParamNames = new SimpleArray<Integer>(br, ShaderParamCount, 0);

        for (int i = 0; i < ShaderParamCount; i++) {
            if (ShaderParamOffsets.Values.get(i) != -1) {
                br.setCurrentOffset(ShaderParamOffsets.Values.get(i));
                switch (ShaderParamTypes.Values.get(i)) {
                    case 0:
                        ShaderParamTexture test = new ShaderParamTexture();
                        //test.read(br);
                        ShaderParams.add(test);
                        break;
                    case 1:
                        ShaderParamVector test1 = new ShaderParamVector();
                        //test1.read(br);
                        ShaderParams.add(test1);
                        break;
                    case 4:
                        ShaderParamMatrix test2 = new ShaderParamMatrix();
                        // test2.read(br);
                        ShaderParams.add(test2);
                        break;
                }
            } else {
                // Message.displayMsgLow("WTF Der zit een shader @ -1 " + Utils.getHexString(startOffset));
            }
        }

        // not sure what to do here

        /* ShaderParams = new Dictionary<ParamNameHash, IShaderParam>(ShaderParamCount); for (int i = 0; i <
         * ShaderParamCount; i++) { try { var obj = ParamObjectFactory.Create((ParamType) ShaderParamTypes[i]);
         * br.BaseStream.Seek(ShaderParamOffsets[i], SeekOrigin.Begin); obj.read(br); ShaderParams.Add((ParamNameHash)
         * ShaderParamNames[i], obj); } catch { ShaderParams.Add((ParamNameHash) ShaderParamNames[i], null); } } */
        br.setCurrentOffset(save);
    }

    public String[] getDataNames() {
        ArrayList<String> nameList = new ArrayList();

        nameList.add("VTable");
        nameList.add("BlockMapAdress");
        nameList.add("Unknown1");
        nameList.add("Unknown2");
        nameList.add("Unknown3");
        nameList.add("Unknown4");
        nameList.add("Unknown4_1");
        nameList.add("Unknown5");
        nameList.add("shaderParamOffsetsOffset");
        nameList.add("Unknown6");
        nameList.add("ShaderParamCount");
        nameList.add("Unknown8");
        nameList.add("shaderParamTypesOffset");
        nameList.add("Hash");
        nameList.add("Unknown9");
        nameList.add("Unknown10");
        nameList.add("shaderParamNameOffset");
        nameList.add("Unknown11");
        nameList.add("Unknown12");
        nameList.add("Unknown13");

        nameList.add("[ShaderParamOffsets]");
        for (int i = 0; i < ShaderParamOffsets.Count; i++) {
            nameList.add("  ShaderParamOffset " + i);
        }

        nameList.add("[ShaderParamTypes]");
        for (int i = 0; i < ShaderParamTypes.Count; i++) {
            nameList.add("  ShaderParamType " + i);
        }

        nameList.add("[ShaderParamNames]");
        for (int i = 0; i < ShaderParamNames.Count; i++) {
            nameList.add("  ShaderParamName " + i);
        }

        String[] names = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            names[i] = nameList.get(i);
        }

        return names;
    }

    public String[] getDataValues() {
        ArrayList valueList = new ArrayList();

        valueList.add("" + VTable);
        valueList.add("" + BlockMapAdress);
        valueList.add("" + Unknown1);
        valueList.add("" + Unknown2);
        valueList.add("" + Unknown3);
        valueList.add("" + Unknown4);
        valueList.add("" + Unknown4_1);
        valueList.add("" + Unknown5);
        valueList.add("" + Utils.getHexString(shaderParamOffsetsOffset));
        valueList.add("" + Unknown6);
        valueList.add("" + ShaderParamCount);
        valueList.add("" + Unknown8);
        valueList.add("" + Utils.getHexString(shaderParamTypesOffset));
        valueList.add("" + Hash);
        valueList.add("" + Unknown9);
        valueList.add("" + Unknown10);
        valueList.add("" + Utils.getHexString(shaderParamNameOffset));
        valueList.add("" + Unknown11);
        valueList.add("" + Unknown12);
        valueList.add("" + Unknown13);

        valueList.add("" + ShaderParamOffsets.Count);
        for (int i = 0; i < ShaderParamOffsets.Count; i++) {
            valueList.add("" + Utils.getHexString(ShaderParamOffsets.Values.get(i)));
        }

        valueList.add("" + ShaderParamTypes.Count);
        for (int i = 0; i < ShaderParamTypes.Count; i++) {
            valueList.add(Utils.getShaderType(ShaderParamTypes.Values.get(i)));
        }

        valueList.add("" + ShaderParamNames.Count);
        for (int i = 0; i < ShaderParamNames.Count; i++) {
            valueList.add(Utils.getShaderName(ShaderParamNames.Values.get(i)));
        }

        String[] values = new String[valueList.size()];

        for (int i = 0; i < valueList.size(); i++) {
            values[i] = (String) valueList.get(i);
        }

        return values;
    }
}
