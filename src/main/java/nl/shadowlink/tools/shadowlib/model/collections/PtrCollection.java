package nl.shadowlink.tools.shadowlib.model.collections;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.model.wdr.Geometry;
import nl.shadowlink.tools.shadowlib.model.wdr.Model2;
import nl.shadowlink.tools.shadowlib.model.wdr.ShaderFx;
import nl.shadowlink.tools.shadowlib.utils.Utils;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class PtrCollection<T> {

    private final static int modelCollection = 1;
    private final static int geometry = 2;
    private final static int shaderfx = 3;

    private int[] _itemOffsets;
    public ArrayList<T> _items;

    private int startOffset;

    public int ptrListOffset;

    public int Count;
    public int Size;

    private int type;
    private ByteReader br;

    public PtrCollection() {
    }

    /**
     * Created a new pointer collection of the specified type
     *
     * @param br   the bytereader to read from
     * @param type the type of object the pointers point to
     */
    public PtrCollection(ByteReader br, int type) {
        this.type = type;
        this.br = br;
        Read();
    }

    /**
     * Read the pointer collection and read the objects
     */
    private void Read() {
        startOffset = br.getCurrentOffset();
        ptrListOffset = br.readOffset();

        Count = br.readUInt16();
        Size = br.readUInt16();

        _itemOffsets = new int[Count];
        _items = new ArrayList<T>();

        int save = br.getCurrentOffset();

        br.setCurrentOffset(ptrListOffset);

        for (int i = 0; i < Count; i++) {
            _itemOffsets[i] = br.readOffset();
            // Message.displayMsgLow("Item offset " + i + ": " + Utils.getHexString(_itemOffsets[i]));
        }

        for (int i = 0; i < Count; i++) {
            br.setCurrentOffset(_itemOffsets[i]);

            T item = getType();

            _items.add(item);
        }

        br.setCurrentOffset(save);
    }

    /**
     * Get the class of the object type
     *
     * @return the objects class
     */
    private T getType() {
        switch (type) {
            case modelCollection:
                Model2 model = new Model2();
                model.read(br);
                return (T) model;
            case geometry:
                Geometry geo = new Geometry();
                geo.read(br);
                return (T) geo;
            case shaderfx:
                ShaderFx sf = new ShaderFx();
                sf.read(br);
                return (T) sf;
            default:
                Model2 model2 = new Model2();
                return (T) model2;
        }
    }

    /**
     * Returns all data names in a String array
     *
     * @return String array with data Names
     */
    public String[] getDataNames() {
        String[] names = new String[4 + Count];
        int i = 0;
        names[i] = "ptrListOffset";
        i++;
        names[i] = "Count";
        i++;
        names[i] = "Size";
        i++;

        names[i] = "[Start PtrList]";
        i++;
        for (int i2 = 0; i2 < Count; i2++) {
            names[i] = "  Pointer " + (i2 + 1) + _items;
            i++;
        }

        return names;
    }

    /**
     * Returns all data values in a String array
     *
     * @return String array with data values
     */
    public String[] getDataValues() {
        String[] values = new String[4 + Count];
        int i = 0;
        values[i] = Utils.getHexString(ptrListOffset);
        i++;
        values[i] = "" + Count;
        i++;
        values[i] = "" + Size;
        i++;

        values[i] = "";
        i++;
        for (int i2 = 0; i2 < Count; i2++) {
            values[i] = Utils.getHexString(_itemOffsets[i2]);
            i++;
        }

        return values;
    }

    /**
     * Returns the start offset of this object in the file
     *
     * @return the start offset of this object in the file
     */
    public String getStartOffset() {
        return Utils.getStartOffset(startOffset);
    }

}
