package nl.shadowlink.tools.shadowlib.model.wft;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.model.wdr.DrawableModel;
import nl.shadowlink.tools.shadowlib.utils.Utils;

import java.util.LinkedList;

/**
 * @author Shadow-Link
 */
public class FragTypeModel {
    public int VTable;
    public int blockMapAdress;
    public int offset1;
    public int offset2;

    public LinkedList<DrawableModel> drawables = new LinkedList();

    public void read(ByteReader br) {
        System.out.println("--------------------\nHeader\n--------------------");
        VTable = br.readUInt32();
        System.out.println("VTable: " + VTable);
        blockMapAdress = br.readOffset();
        System.out.println("Block: " + Utils.getHexString(blockMapAdress));

        float unkFloat1 = br.readFloat();
        float unkFloat2 = br.readFloat();
        System.out.println("UNKFloat1: " + unkFloat1);
        System.out.println("UNKFloat2: " + unkFloat2);

        for (int i = 0; i < 10; i++) {
            br.readVector4D().print("UNK Vec" + i);
        }

        offset1 = br.readOffset();
        offset2 = br.readOffset();

        System.out.println("PackString Offset: " + Utils.getHexString(offset1));
        int save = br.getCurrentOffset();
        br.setCurrentOffset(offset1);
        System.out.println("PackString: " + br.readNullTerminatedString());
        br.setCurrentOffset(save);

        System.out.println("Drawable: " + Utils.getHexString(offset2));
        save = br.getCurrentOffset();
        br.setCurrentOffset(offset2);

        DrawableModel drwbl = new DrawableModel();
        drwbl.readSystemMemory(br);
        drawables.add(drwbl);

        br.setCurrentOffset(save);

        int zero1 = br.readUInt32();
        int zero2 = br.readUInt32();
        int zero3 = br.readUInt32();
        int max1 = br.readUInt32();
        int zero4 = br.readUInt32();
        System.out.println("Zero1: " + zero1);
        System.out.println("Zero2: " + zero2);
        System.out.println("Zero3: " + zero3);
        System.out.println("Max1: " + max1);
        System.out.println("Zero4: " + zero4);

        int offset3 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset3));
        save = br.getCurrentOffset();
        br.setCurrentOffset(offset3);
        int off = br.readOffset();
        System.out.println("Off = " + Utils.getHexString(off));
        while (off != -1) {
            int save2 = br.getCurrentOffset();
            br.setCurrentOffset(off);
            String name = br.readNullTerminatedString();
            System.out.println("Name: " + name);
            br.setCurrentOffset(save2);
            System.out.println(Utils.getHexString(br.getCurrentOffset()));
            off = br.readOffset();
        }
        br.setCurrentOffset(save);

        int offset4 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset4));
        int childListOffset = br.readOffset();
        System.out.println("ChildListOffset: " + Utils.getHexString(childListOffset));

        int zero5 = br.readUInt32();
        int zero6 = br.readUInt32();
        int zero7 = br.readUInt32();

        int offset6 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset6));

        int zero8 = br.readUInt32();

        int offset7 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset7));
        int offset8 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset8));
        int offset9 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset9));
        int offset10 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset10));

        int zero9 = br.readUInt32();
        int zero10 = br.readUInt32();
        int zero11 = br.readUInt32();

        int offset11 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset11));
        int zero12 = br.readUInt32();
        int zero13 = br.readUInt32();
        int zero14 = br.readUInt32();
        int zero15 = br.readUInt32();

        int offset12 = br.readOffset();
        System.out.println("Unk offset: " + Utils.getHexString(offset12));

        System.out.println("--------------------\nChildList\n--------------------");
        save = br.getCurrentOffset();
        br.setCurrentOffset(childListOffset);
        int childOffset = br.readOffset();
        while (childOffset != -1) {
            int save2 = br.getCurrentOffset();
            System.out.println("ChildOffset: " + Utils.getHexString(childOffset));
            if (childOffset < 0x0F0000) {
                br.setCurrentOffset(childOffset);
                FragTypeChild ftc = new FragTypeChild(br);
                if (ftc.drwblOffset != -1) {
                    br.setCurrentOffset(ftc.drwblOffset);
                    drwbl = new DrawableModel();
                    drwbl.readSystemMemory(br);
                    drawables.add(drwbl);
                }
                br.setCurrentOffset(save2);
            }
            childOffset = br.readOffset();
        }
        br.setCurrentOffset(save);
    }

}
