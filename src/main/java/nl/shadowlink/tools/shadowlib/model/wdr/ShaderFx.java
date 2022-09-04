package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.utils.Utils;

/**
 * @author Shadow-Link
 */
public class ShaderFx {
    private int startOffset;

    public Shader shader;

    private int shaderNamePtr;
    private int shaderSpsPtr;

    private int Unknown14;
    private int Unknown15;
    private int Unknown16;
    private int Unknown17;

    public String ShaderName;
    public String ShaderSPS;

    public void read(ByteReader br) {
        startOffset = br.getCurrentOffset();

        shader = new Shader(br);

        shaderNamePtr = br.readOffset();
        shaderSpsPtr = br.readOffset();

        // System.out.println("ShaderNamePtr: " + shaderNamePtr);
        // System.out.println("ShaderSpsPtr: " + shaderSpsPtr);

        Unknown14 = br.readUInt32();
        Unknown15 = br.readUInt32();
        Unknown16 = br.readUInt32();
        Unknown17 = br.readUInt32();

        // Data:

        if (shaderNamePtr != -1) {
            br.setCurrentOffset(shaderNamePtr);
            ShaderName = br.readNullTerminatedString();
            System.out.println("ShaderName: " + ShaderName);
        }

        if (shaderSpsPtr != -1) {
            br.setCurrentOffset(shaderSpsPtr);
            ShaderSPS = br.readNullTerminatedString();
            System.out.println("ShaderSPS: " + ShaderSPS);
        }

    }

    public String[] getDataNames() {
        String[] names = new String[8];
        names[0] = "shaderNamePtr";
        names[1] = "shaderSpsPtr";
        names[2] = "Unknown14";              // byte bLocked, byte align
        names[3] = "Unknown15";       // pLockedData
        names[4] = "Unknown16";
        names[5] = "Unknown17";
        names[6] = "ShaderName";
        names[7] = "ShaderSPS";             // piVertexBuffer

        return names;
    }

    public String[] getDataValues() {
        String[] values = new String[10];
        values[0] = Utils.getHexString(shaderNamePtr);
        values[1] = Utils.getHexString(shaderSpsPtr);
        values[2] = "" + Unknown14;              // byte bLocked, byte align
        values[3] = "" + Unknown15;       // pLockedData
        values[4] = "" + Unknown16;
        values[5] = "" + Unknown17;
        values[6] = "" + ShaderName;
        values[7] = "" + ShaderSPS;             // piVertexBuffer

        return values;
    }

    public String getStartOffset() {
        return Utils.getStartOffset(startOffset);
    }

}
