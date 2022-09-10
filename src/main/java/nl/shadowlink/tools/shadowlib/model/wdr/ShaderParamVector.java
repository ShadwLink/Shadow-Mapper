package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.io.Vector4D;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class ShaderParamVector {
    public int startOffset;
    public Vector4D Data;

    public void read(ByteReader br) {
        startOffset = br.getCurrentOffset();
        Data = br.readVector4D();
        Data.print("Shader Vector: ");
    }

    public String[] getDataNames() {
        ArrayList<String> nameList = new ArrayList();

        nameList.add("Unkown Vector");

        String[] names = new String[nameList.size()];
        for (int i = 0; i < nameList.size(); i++) {
            names[i] = nameList.get(i);
        }

        return names;
    }

    public String[] getDataValues() {
        ArrayList<String> valueList = new ArrayList();

        valueList.add(Data.x + ", " + Data.y + ", " + Data.z + ", " + Data.w);

        String[] values = new String[valueList.size()];
        for (int i = 0; i < valueList.size(); i++) {
            values[i] = valueList.get(i);
        }

        return values;
    }
}
