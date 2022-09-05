package nl.shadowlink.tools.io;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all read function needed for loading
 * the 3D model formats
 *
 * @author Itopia Team08
 * @version 1.0
 */
public class ReadFunctions {
    private RandomAccessFile data_in;

    public boolean openFile(String name) {
        boolean ret = true;
        try {
            data_in = new RandomAccessFile(name, "r");
        } catch (FileNotFoundException ex) {
            ret = false;
        }
        return ret;
    }

    public boolean closeFile() {
        boolean ret = true;
        try {
            data_in.close();
        } catch (IOException ex) {
            System.out.println("Unable to close file");
            ret = false;
        }
        return ret;
    }

    public byte readByte() {
        byte waarde = -1;
        try {
            waarde = data_in.readByte();
        } catch (IOException ex) {
            waarde = -1;
        }
        return waarde;
    }

    public void readBytes(byte[] bytes) {
        try {
            data_in.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readInt() {
        int waarde = -1;
        try {
            waarde = swapInt(data_in.readInt());
        } catch (IOException ex) {
            waarde = -1;
        }
        return waarde;
    }

    public int readShort() {
        int waarde = -1;
        try {
            waarde = swapShort(data_in.readShort());
        } catch (IOException ex) {
            waarde = -1;
        }
        return waarde;
    }

    public float readFloat() {
        float waarde = -1;
        byte[] bytes = new byte[4];
        for (int i = 3; i >= 0; i--) {
            bytes[i] = readByte();
        }
        ByteBuffer data = ByteBuffer.wrap(bytes);
        waarde = data.getFloat();

        return waarde;
    }

    public String readString(int size) {
        char letter = 'n';
        String woord = "";
        //letter = readChar(data_in);
        for (int i = 0; i < size; i++) {
            letter = readChar();
            woord += letter;
        }
        return woord;
    }

    public String readNullTerminatedString(int size) {
        String woord = "";
        boolean gotNull = false;
        for (int i = 0; i < size; i++) {
            byte b = readByte();
            if (!gotNull) {
                if (b != 0) woord += (char) b;
                else gotNull = true;
            }
        }
        return woord;
    }

    public String readNullTerminatedString() {
        String woord = "";
        byte b = readByte();
        while (b != 0) {
            woord += (char) b;
            b = readByte();
        }
        return woord;
    }


    public char readChar() {
        char letter = '\0';
        try {
            letter = (char) data_in.readByte();
        } catch (IOException ex) {
            //Logger.getLogger(loadSAFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return letter;
    }

    /**
     * Converts a big endian int to a little endian int
     *
     * @param v
     * @return the little endian int of a big endian int
     */
    public int swapInt(int v) {
        return (v >>> 24) | (v << 24) | ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
    }

    /**
     * Converts a big endian short to a little endian short
     *
     * @param i
     * @return the little endian short of a big endian short
     */
    public int swapShort(short i) {
        return ((i >> 8) & 0xff) + ((i << 8) & 0xff00);
    }

    /**
     * Converts a big endian float to a little endian float
     *
     * @param f
     * @return the little endian float of a big endian float
     */
    public float swapFloat(float f) {
        int intValue = Float.floatToIntBits(f);
        intValue = swapInt(intValue);
        return Float.intBitsToFloat(intValue);
    }

    public Vector3D readVector3D() {
        return new Vector3D(readFloat(), readFloat(), readFloat());
    }

    public Vector4D readVector4D() {
        return new Vector4D(readFloat(), readFloat(), readFloat(), readFloat());
    }

    public int moreToRead() {
        try {
            return (int) (data_in.length() - data_in.getFilePointer());
        } catch (IOException ex) {
            Logger.getLogger(ReadFunctions.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public ByteReader getByteReader() {
        try {
            byte[] stream = new byte[(int) data_in.length()];
            data_in.read(stream, 0, (int) data_in.length());
            return new ByteReader(stream, 0);
        } catch (IOException ex) {
            return null;
        }
    }

    public ByteReader getByteReader(int size) {
        try {
            byte[] stream = new byte[size];
            data_in.read(stream, 0, size);
            return new ByteReader(stream, 0);
        } catch (IOException ex) {
            System.out.println("Error in getByteReader");
            return null;
        }
    }

    public void seek(int offset) {
        try {
            data_in.seek(offset);
        } catch (IOException ex) {
            Logger.getLogger(ReadFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void seek(final long pOffset) {
        try {
            data_in.seek(pOffset);
        } catch (IOException ex) {
            Logger.getLogger(ReadFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public long readUnsignedInt() {
        int i = readInt();
        return i & 0xffffffffL;
    }

    public byte[] readArray(int size) {
        byte[] array = new byte[size];
        try {
            data_in.readFully(array);
        } catch (IOException ex) {
            Logger.getLogger(ReadFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return array;
    }

}
