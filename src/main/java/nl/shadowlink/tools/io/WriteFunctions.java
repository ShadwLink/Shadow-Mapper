package nl.shadowlink.tools.io;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class WriteFunctions {
    private RandomAccessFile data_out;

    /**
     * returns if a certain flag is on
     *
     * @param flags
     * @param flag
     * @return if a flag has been set
     */
    public boolean hasFlag(int flags, int flag) {
        boolean hasFlag = false;
        boolean finished = false;
        int waarde = 1024;
        int newFlag = flags;
        while (!finished) {
            newFlag -= waarde;
            if (waarde < flag) {
                finished = true;
            } else {
                if (newFlag <= 0) {
                    if (waarde == 1) {
                        finished = true;
                    }
                    newFlag = flags;
                    waarde /= 2;
                } else {
                    flags = newFlag;
                    if (waarde == flag) {
                        hasFlag = true;
                        finished = true;
                    } else {
                        waarde /= 2;
                    }
                }
            }
        }
        return hasFlag;
    }

    /**
     * Opens a file
     *
     * @param name
     * @return inputStream of the file
     */
    public boolean openFile(String name) {
        try {
            data_out = new RandomAccessFile(name, "rw");
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Closes the FileInputStream and DataInputStream
     *
     * @param file_in
     * @param data_in
     */
    public boolean closeFile() {
        try {
            data_out.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * Returns a byte from the DataInputStream
     *
     * @param data_in
     * @return a byte from the DataInputStream
     */
    public void writeByte(int value) {
        try {
            data_out.writeByte(value);
        } catch (IOException ex) {
            value = -1;
        }
    }

    /**
     * Returns an int from the DataInputStream
     *
     * @param data_in
     * @return an int from the DataInputStream
     */
    public void write(int value) {
        ByteBuffer bbuf = ByteBuffer.allocate(4);
        bbuf.order(ByteOrder.BIG_ENDIAN);
        bbuf.putInt(value);
        bbuf.order(ByteOrder.LITTLE_ENDIAN);
        value = bbuf.getInt(0);
        try {
            data_out.writeInt(value);
        } catch (IOException ex) {
            value = -1;
        }
    }

    /**
     * Returns a short as an int from the DataInputStream
     *
     * @param data_in
     * @return a short as an int from the DataInputStream
     */
    public void writeShort(int value) {
        ByteBuffer bbuf = ByteBuffer.allocate(4);
        bbuf.order(ByteOrder.BIG_ENDIAN);
        bbuf.putInt(value);
        bbuf.order(ByteOrder.LITTLE_ENDIAN);
        value = bbuf.getShort(2);
        try {
            data_out.writeShort(value);
        } catch (IOException ex) {
            value = -1;
        }
    }

    /**
     * Returns a float from the DataInputStream
     *
     * @param data_in
     * @return a float from the DataInputStream
     */
    public void write(float value) {
        ByteBuffer bbuf = ByteBuffer.allocate(4);
        bbuf.order(ByteOrder.BIG_ENDIAN);
        bbuf.putFloat(value);
        bbuf.order(ByteOrder.LITTLE_ENDIAN);
        value = bbuf.getFloat(0);
        try {
            data_out.writeFloat(value);
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a Char from the DataInputStream
     *
     * @param data_in
     * @return a char from the DatainputStream
     */
    public char writeChar(char value) {
        char letter = '\0';
        try {
            data_out.writeByte(value);
        } catch (IOException ex) {
            // Logger.getLogger(loadSAFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return letter;
    }

    /**
     * writes a String to the dataoutputstream
     *
     * @param data_in
     * @return a String
     */
    public void writeString(String value) {
        for (int i = 0; i < value.length(); i++) {
            writeChar(value.charAt(i));
        }
    }

    /**
     * writes a String from the DataInputStream till a \0 char
     *
     * @param data_in
     * @return a String
     */
    public void writeNullTerminatedString(String value) {
        for (int i = 0; i < value.length(); i++) {
            writeChar(value.charAt(i));
        }
        writeByte(0);
    }

    public void write(Vector3D vector) {
        write(vector.x);
        write(vector.y);
        write(vector.z);
    }

    public void write(Vector4D vector) {
        write(vector.x);
        write(vector.y);
        write(vector.z);
        write(vector.w);
    }

    public void write(byte[] array) {
        try {
            data_out.write(array);
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void seek(int pos) {
        try {
            data_out.seek(pos);
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoEnd() {
        try {
            data_out.seek(data_out.length());
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getFileSize() {
        try {
            return (int) data_out.length();
        } catch (IOException ex) {
            return 0;
        }
    }

}
