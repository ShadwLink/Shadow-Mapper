package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.io.ReadFunctions;

/**
 * @author Shadow-Link
 */
public class ResourceHeader {

    private int MagicBigEndian = 0x52534305;
    public static int MagicValue = 0x05435352;

    public int Magic;// { get; set; }
    public ResourceType Type;// { get; set; }
    public int Flags;// { get; set; }
    public CompressionType CompressCodec;// { get; set; }

    public int getSystemMemSize() {
        return (int) (Flags & 0x7FF) << (int) (((Flags >> 11) & 0xF) + 8);
    }

    public int getGraphicsMemSize() {
        return (int) ((Flags >> 15) & 0x7FF) << (int) (((Flags >> 26) & 0xF) + 8);
    }

    public void setMemSizes(int systemMemSize, int graphicsMemSize) {
        // gfx = a << (b + 8)
        // minimum representable is block of 0x100 bytes

        int maxA = 0x3F;

        int sysA = systemMemSize >> 8;
        int sysB = 0;

        while (sysA > maxA) {
            if ((sysA & 1) != 0) {
                sysA += 2;
            }
            sysA >>= 1;
            sysB++;
        }

        int gfxA = graphicsMemSize >> 8;
        int gfxB = 0;

        while (gfxA > maxA) {
            if ((gfxA & 1) != 0) {
                gfxA += 2;
            }
            gfxA >>= 1;
            gfxB++;
        }

        Flags = (Flags & 0xC0000000) | (int) (sysA | (sysB << 11) | (gfxA << 15) | (gfxB << 26));
    }

    public void read(ByteReader br) {
        Magic = br.readUInt32();
        int type = br.readUInt32();
        Type = ResourceType.get(type);
        Flags = br.readUInt32();
        CompressCodec = CompressionType.get(br.readUInt16());

        /* if (Magic == MagicBigEndian) { //Message.displayMsgHigh("SwapEndian"); Magic = rf.swapInt(Magic); Type =
         * ResourceType.get(rf.swapInt(type)); Flags = rf.swapInt(Flags); } */

        // Message.displayMsgHigh("Magic: " + Magic);
        switch (Type) {
            case Model:
                // Message.displayMsgHigh("Model: " + type);
                break;
            default:
                // Message.displayMsgHigh("Dunnow: " + type);
        }
    }

    public void read(ReadFunctions rf) {
        Magic = rf.readInt();
        int type = rf.readInt();
        Type = ResourceType.get(type);
        Flags = rf.readInt();
        CompressCodec = CompressionType.get(rf.readShort());

        /* if (Magic == MagicBigEndian) { //Message.displayMsgHigh("SwapEndian"); Magic = rf.swapInt(Magic); Type =
         * ResourceType.get(rf.swapInt(type)); Flags = rf.swapInt(Flags); } */

        // Message.displayMsgHigh("Magic: " + Magic);
        switch (Type) {
            case Model:
                // Message.displayMsgHigh("Model: " + type);
                break;
            default:
                // Message.displayMsgHigh("Dunnow: " + type);
        }
    }

    /* public void Write(DataOutputStream bw) { try { WriteFunctions wf = new WriteFunctions(); ReadFunctions rf = new
     * ReadFunctions(); wf.writeInt(bw, rf.swapInt(MagicValue)); bw.writeInt(0x6E000000); wf.writeInt(bw,
     * rf.swapInt(Flags)); bw.writeShort(0x78DA); } catch (IOException ex) { //Message.displayMsgHigh("Error"); } } */

}
