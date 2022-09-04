package nl.shadowlink.tools.shadowlib.model.wft;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.io.Vector4D;

/**
 * @author Shadow-Link
 */
public class FragTypeChild {
    public int drwblOffset;

    public FragTypeChild(ByteReader br) {
        int VTable = br.readUInt32();
        // System.out.println("VTable: " + VTable);
        float unkFloat1 = br.readFloat();
        float unkFloat2 = br.readFloat();
        int unkZero = br.readUInt32();
        // System.out.println("UnkFloat1: " + unkFloat1);
        // System.out.println("UnkFloat2: " + unkFloat2);
        // System.out.println("UnkZero: " + unkZero);
        for (int i = 0; i < 8; i++) {
            Vector4D unkVec = br.readVector4D();
            unkVec.print("UnkVec" + i);
        }
        drwblOffset = br.readOffset();
        // System.out.println("DrawableOffset: " + Utils.getHexString(drwblOffset));
        int unkZero2 = br.readUInt32();
        // System.out.println("UnkZero2: " + unkZero2);
        int unkOffset1 = br.readOffset();
        // System.out.println("UnkOffset1: " + Utils.getHexString(unkOffset1));
        int unkOffset2 = br.readOffset();
        // System.out.println("UnkOffset2: " + Utils.getHexString(unkOffset2));
        int unkOffset3 = br.readOffset();
        // System.out.println("UnkOffset3: " + Utils.getHexString(unkOffset3));
        int unkOffset4 = br.readOffset();
        // System.out.println("UnkOffset4: " + Utils.getHexString(unkOffset4));

        int unkZero3 = br.readUInt32();
        // System.out.println("UnkZero3: " + unkZero3);
        int unkZero4 = br.readUInt32();
        // System.out.println("UnkZero4: " + unkZero4);
        int unkZero5 = br.readUInt32();
        // System.out.println("UnkZero5: " + unkZero5);
        int unkZero6 = br.readUInt32();
        // System.out.println("UnkZero6: " + unkZero6);

        int unkOffset5 = br.readOffset();
        // System.out.println("UnkOffset5: " + Utils.getHexString(unkOffset5));

    }

}
