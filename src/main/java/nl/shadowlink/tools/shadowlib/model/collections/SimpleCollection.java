package nl.shadowlink.tools.shadowlib.model.collections;

import nl.shadowlink.tools.io.ByteReader;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class SimpleCollection<T> {
    public ArrayList<T> Values;

    public int Count;
    public int Size;
    public int type;

    public SimpleCollection(ByteReader br, int type) {
        this.type = type;
        Read(br);
    }

    public void Read(ByteReader br) {
        int offset = br.readOffset();
        // Message.displayMsgLow("Offset: " + offset);

        Count = br.readUInt16();
        Size = br.readUInt16();

        Values = new ArrayList<T>(Count);

        int save = br.getCurrentOffset();

        br.setCurrentOffset(offset);

        for (int i = 0; i < Count; i++) {
            Values.add(ReadData(br));
        }

        br.setCurrentOffset(save);
    }

    public T ReadData(ByteReader br) {

        switch (type) {
            case 0:
                Object data = br.readUInt32();
                // Message.displayMsgLow("Data: " + data);
                return (T) data;
            default:
                Object data2 = br.readUInt32();
                return (T) data2;
        }
    }
}
