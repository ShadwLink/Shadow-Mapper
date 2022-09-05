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

    public boolean openFile(String name) {
        try {
            data_out = new RandomAccessFile(name, "rw");
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

       public boolean closeFile() {
        try {
            data_out.close();
        } catch (IOException ex) {
            Logger.getLogger(WriteFunctions.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }


    public void writeByte(int value) {
        try {
            data_out.writeByte(value);
        } catch (IOException ex) {
            value = -1;
        }
    }


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


    public char writeChar(char value) {
        char letter = '\0';
        try {
            data_out.writeByte(value);
        } catch (IOException ex) {
            // Logger.getLogger(loadSAFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return letter;
    }


    public void writeString(String value) {
        for (int i = 0; i < value.length(); i++) {
            writeChar(value.charAt(i));
        }
    }


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
