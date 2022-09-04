

package nl.shadowlink.tools.shadowlib.model.wdr;

import nl.shadowlink.tools.shadowlib.model.model.Element;

/**
 * @author Shadow-Link
 */
public class ModelFile {

	public void addVertices(Element mdl, byte[] stream, int vertOffset, int polyOffset, int vertSize, int polySize, int StrideSize, int matIndex) {
		for (int i = 0; i < vertSize; i++) {
			byte[] miniArr = new byte[4];
			miniArr[0] = stream[vertOffset + (i * StrideSize)];
			miniArr[1] = stream[vertOffset + (i * StrideSize) + 1];
			miniArr[2] = stream[vertOffset + (i * StrideSize) + 2];
			miniArr[3] = stream[vertOffset + (i * StrideSize) + 3];
			float x = arr2float(miniArr);
			miniArr[0] = stream[vertOffset + (i * StrideSize) + 4];
			miniArr[1] = stream[vertOffset + (i * StrideSize) + 5];
			miniArr[2] = stream[vertOffset + (i * StrideSize) + 6];
			miniArr[3] = stream[vertOffset + (i * StrideSize) + 7];
			float y = arr2float(miniArr);
			miniArr[0] = stream[vertOffset + (i * StrideSize) + 8];
			miniArr[1] = stream[vertOffset + (i * StrideSize) + 9];
			miniArr[2] = stream[vertOffset + (i * StrideSize) + 10];
			miniArr[3] = stream[vertOffset + (i * StrideSize) + 11];
			float z = arr2float(miniArr);
			miniArr[0] = stream[vertOffset + (i * StrideSize) + 12];
			miniArr[1] = stream[vertOffset + (i * StrideSize) + 13];
			miniArr[2] = stream[vertOffset + (i * StrideSize) + 14];
			miniArr[3] = stream[vertOffset + (i * StrideSize) + 15];
			float normx = arr2float(miniArr);
			miniArr[0] = stream[vertOffset + (i * StrideSize) + 16];
			miniArr[1] = stream[vertOffset + (i * StrideSize) + 17];
			miniArr[2] = stream[vertOffset + (i * StrideSize) + 18];
			miniArr[3] = stream[vertOffset + (i * StrideSize) + 19];
			float normy = arr2float(miniArr);
			miniArr[0] = stream[vertOffset + (i * StrideSize) + 20];
			miniArr[1] = stream[vertOffset + (i * StrideSize) + 21];
			miniArr[2] = stream[vertOffset + (i * StrideSize) + 22];
			miniArr[3] = stream[vertOffset + (i * StrideSize) + 23];
			float normz = arr2float(miniArr);
			byte unk1 = stream[vertOffset + (i * StrideSize) + 24];
			byte unk2 = stream[vertOffset + (i * StrideSize) + 25];
			byte unk3 = stream[vertOffset + (i * StrideSize) + 26];
			byte unk4 = stream[vertOffset + (i * StrideSize) + 27];
			miniArr[0] = stream[vertOffset + (i * StrideSize) + 28];
			miniArr[1] = stream[vertOffset + (i * StrideSize) + 29];
			miniArr[2] = stream[vertOffset + (i * StrideSize) + 30];
			miniArr[3] = stream[vertOffset + (i * StrideSize) + 31];
			float u = arr2float(miniArr);
			miniArr[0] = stream[vertOffset + (i * StrideSize) + 32];
			miniArr[1] = stream[vertOffset + (i * StrideSize) + 33];
			miniArr[2] = stream[vertOffset + (i * StrideSize) + 34];
			miniArr[3] = stream[vertOffset + (i * StrideSize) + 35];
			float v = arr2float(miniArr);

			// System.out.println("XYZ " + i + ":" + x + ", " + y + ", " + z);
			// Message.displayMsgLow("Normal:" + normx + ", " + normy + ", " + normz);
			// Message.displayMsgLow(unk1 + ", " + unk2 + ", " + unk3 + ", " + unk4);
			// Message.displayMsgLow("UV: " + u + ", " + v);
			mdl.addVertexToStrip(x, y, z, u, v, matIndex);
			mdl.createModelVertex(i, x, y, z, u, v);
			// mdl.getStrip(matIndex).getVertex(mdl.getStrip(matIndex).getVertexCount()-1);
		}
	}

	public void addPolygons(Element mdl, byte[] stream, int vertOffset, int polyOffset, int vertSize, int polySize, int StrideSize, int matIndex) {
		for (int i = 0; i < polySize; i++) { // 240
			byte[] miniArr = new byte[2];
			miniArr[0] = stream[polyOffset + (i * 6)];
			miniArr[1] = stream[polyOffset + (i * 6) + 1];
			int a = arr2int(miniArr);
			miniArr[0] = stream[polyOffset + (i * 6) + 2];
			miniArr[1] = stream[polyOffset + (i * 6) + 3];
			int b = arr2int(miniArr);
			miniArr[0] = stream[polyOffset + (i * 6) + 4];
			miniArr[1] = stream[polyOffset + (i * 6) + 5];
			int c = arr2int(miniArr);
			// System.out.println("Poly "+i+": " + a + ", " + b + ", " + c);

			mdl.createModelPoly(a, b, c, matIndex, false);
		}
	}

	public static float arr2float(byte[] arr) {
		int i = 0;
		int len = 4;
		int cnt = 0;
		int accum = 0;
		i = 0;
		for (int shiftBy = 0; shiftBy < 32; shiftBy += 8) {
			accum |= ((long) (arr[i] & 0xff)) << shiftBy;
			i++;
		}
		return Float.intBitsToFloat(accum);
	}

	public static int arr2int(byte[] arr) {
		int low = arr[0] & 0xff;
		int high = arr[1] & 0xff;
		return (int) (high << 8 | low);
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

}
