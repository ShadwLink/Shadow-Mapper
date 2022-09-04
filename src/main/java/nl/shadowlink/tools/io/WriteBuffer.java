

package nl.shadowlink.tools.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class WriteBuffer {
	private ArrayList<Byte> byteBuffer = new ArrayList();

	/**
	 * Returns a byte from the DataInputStream
	 *
	 * @param data_in
	 * @return a byte from the DataInputStream
	 */
	public int writeByte(int waarde) {
		byteBuffer.add((byte) waarde);
		return 1;
	}

	/**
	 * Returns an int from the DataInputStream
	 *
	 * @param data_in
	 * @return an int from the DataInputStream
	 */
	public int writeInt(int waarde) {
		// ByteBuffer bbuf = ByteBuffer.allocate(4);
		// bbuf.order(ByteOrder.BIG_ENDIAN);
		// bbuf.putInt(waarde);
		// bbuf.order(ByteOrder.LITTLE_ENDIAN);
		// waarde = bbuf.getInt(0);

		byte[] bytes = int2arr(waarde);
		for (int i = 0; i < bytes.length; i++) {
			writeByte(bytes[i]);
		}
		return 4;
	}

	public int writeVector(float x, float y, float z, float w) {
		writeFloat(x);
		writeFloat(y);
		writeFloat(z);
		if (w != -1) {
			writeFloat(w);
		} else {
			writeInt(2139095041);
		}
		return 16;
	}

	public int writeVector(Vector3D vec) {
		writeFloat(vec.x);
		writeFloat(vec.y);
		writeFloat(vec.z);
		writeInt(2139095041);
		return 16;
	}

	public int writeOffset(int waarde) {
		Short sWaarde = (short) waarde;
		ByteBuffer bbuf = ByteBuffer.allocate(2);
		bbuf.order(ByteOrder.BIG_ENDIAN);
		bbuf.putShort(sWaarde);
		bbuf.order(ByteOrder.LITTLE_ENDIAN);
		sWaarde = bbuf.getShort(0);
		writeShort(waarde);
		writeShort(20480);
		return 4;
	}

	public int writeDataOffset(int waarde) {
		ByteBuffer bbuf = ByteBuffer.allocate(4);
		bbuf.order(ByteOrder.BIG_ENDIAN);
		bbuf.putInt(waarde);
		bbuf.order(ByteOrder.LITTLE_ENDIAN);
		byte test1 = bbuf.get(0);
		byte test2 = bbuf.get(1);
		byte test3 = bbuf.get(2);
		writeByte(test1);
		writeByte(test3);
		writeByte(test2);
		writeByte(0x60);
		return 4;
	}

	/**
	 * Returns a short as an int from the DataInputStream
	 *
	 * @param data_in
	 * @return a short as an int from the DataInputStream
	 */
	public int writeShort(int waarde) {
		Short sWaarde = (short) waarde;
		ByteBuffer bbuf = ByteBuffer.allocate(2);
		bbuf.order(ByteOrder.BIG_ENDIAN);
		bbuf.putShort(sWaarde);
		bbuf.order(ByteOrder.LITTLE_ENDIAN);
		sWaarde = bbuf.getShort(0);

		byte[] bytes = short2arr(waarde);
		for (int i = 0; i < bytes.length; i++) {
			writeByte(bytes[i]);
		}

		return 2;
	}

	/**
	 * Returns a float from the DataInputStream
	 *
	 * @param data_in
	 * @return a float from the DataInputStream
	 */
	public int writeFloat(float waarde) {
		// ByteBuffer bbuf = ByteBuffer.allocate(4);
		// bbuf.order(ByteOrder.BIG_ENDIAN);
		// bbuf.putFloat(waarde);
		// bbuf.order(ByteOrder.LITTLE_ENDIAN);
		// waarde = bbuf.getFloat(0);

		byte[] bytes = float2arr(waarde);
		for (int i = 0; i < bytes.length; i++) {
			writeByte(bytes[i]);
		}
		return 4;
	}

	/**
	 * Returns a Char from the DataInputStream
	 *
	 * @param data_in
	 * @return a char from the DatainputStream
	 */
	public int writeChar(char waarde) {
		writeByte(waarde);
		return 1;
	}

	/**
	 * writes a String from the DataInputStream till a \0 char
	 *
	 * @param data_in
	 * @return a String
	 */
	public int writeString(String waarde) {
		int length = 0;
		for (int i = 0; i < waarde.length(); i++) {
			length += writeChar(waarde.charAt(i));
		}
		length += writeByte(0);
		return length;
	}

	public int writeArray(byte[] array) {
		for (int i = 0; i < array.length; i++) {
			writeByte(array[i]);
		}
		return array.length;
	}

	public static byte[] float2arr(float f) {
		int n = Float.floatToIntBits((float) f);
		byte[] bytes = new byte[4];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (n >> (i * 8) & 0xFF);   // 8!!!!
		}

		return bytes;
	}

	public static byte[] short2arr(int i) {
		byte[] bytes = new byte[2];
		short s = (short) i;
		bytes[1] = (byte) ((s >> 8) & 0xff);
		bytes[0] = (byte) (s & 0xff);
		return bytes;
	}

	public static byte[] int2arr(int value) {
		byte[] bytes = new byte[4];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (value >> (i * 8) & 0xFF);   // 8!!!!
		}

		return bytes;
	}

	public void writeBufferToFile(DataOutputStream data_out) {
		for (int i = 0; i < byteBuffer.size(); i++) {
			try {
				data_out.writeByte(byteBuffer.get(i));
			} catch (IOException ex) {
				Logger.getLogger(WriteBuffer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void displayByteBuffer() {
		for (int i = 0; i < byteBuffer.size(); i++) {
			if ((i % 16) == 0) {
				System.out.println("");
			}
			System.out.print(Integer.toString(byteBuffer.get(i), 16) + " ");
		}
	}

	public void replaceOffset(int offsetOffset, int newOffset) {
		ByteBuffer bbuf = ByteBuffer.allocate(4);
		bbuf.order(ByteOrder.BIG_ENDIAN);
		bbuf.putInt(newOffset);

		byteBuffer.set(offsetOffset, bbuf.get(3));
		byteBuffer.set(offsetOffset + 1, bbuf.get(2));
		byteBuffer.set(offsetOffset + 2, bbuf.get(1));
	}

	public void replaceDataOffset(int offsetOffset, int newOffset) {
		System.out.println("Newoffset: 0x" + Integer.toString(newOffset, 16) + " " + newOffset);
		ByteBuffer bbuf = ByteBuffer.allocate(4);
		bbuf.order(ByteOrder.BIG_ENDIAN);
		bbuf.putInt(newOffset);

		byteBuffer.set(offsetOffset, bbuf.get(3));
		byteBuffer.set(offsetOffset + 1, bbuf.get(2));
		byteBuffer.set(offsetOffset + 2, bbuf.get(1));
	}

	public byte[] getArray() {
		byte[] dataBuffer = new byte[byteBuffer.size()];
		for (int i = 0; i < dataBuffer.length; i++) {
			dataBuffer[i] = byteBuffer.get(i);
		}
		return dataBuffer;
	}
}
