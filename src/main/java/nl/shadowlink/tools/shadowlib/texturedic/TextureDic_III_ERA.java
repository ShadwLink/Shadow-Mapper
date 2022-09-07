package nl.shadowlink.tools.shadowlib.texturedic;

/**
 * @author Shadow-Link
 */
public class TextureDic_III_ERA {
    private int[] textureId;
    private String[] texName;

    public int[] loadTextureDic(TextureDic txd) {
        // TODO: Move GL to Shadow-Mapper
        // ByteReader br;
        // if (txd.br == null) {
        // ReadFunctions rf = new ReadFunctions();
        // if (rf.openFile(txd.getFileName())) {
        // System.out.println("TXD Opened");
        // }
        // br = rf.getByteReader();
        // } else {
        // br = txd.br;
        // }
        //
        // int secID = 0;
        // int secSize = 0;
        // int secVersion = 0;
        // int lastID = 0;
        //
        // secID = br.readUInt32();
        // secSize = br.readUInt32();
        // secVersion = br.readUInt32();
        //
        // // for textures
        // int texID = 0;
        //
        // while (secID != 0) {
        // // Message.displayMsgLow("secID: " + secID);
        // // Message.displayMsgLow("secSize: " + secSize);
        // // Message.displayMsgLow("secVersion: " + secVersion);
        // switch (secID) {
        // case ConstantsDFF.rwDATA:
        // switch (lastID) {
        // case ConstantsDFF.rwTEXDIC:
        // // Message.displayMsgHigh("RW Texdic");
        // int texCount = br.readUInt16();
        // txd.textureCount = texCount;
        // int unknown = br.readUInt16();
        // // Message.displayMsgHigh("Texture Count: " + texCount);
        // // Message.displayMsgLow("Unknown: " + unknown);
        //
        // textureId = new int[texCount];
        // texName = new String[texCount];
        // if (texCount != 0 && gl != null) {
        // gl.glGenTextures(texCount, textureId, 0);
        // }
        //
        // break;
        // case ConstantsDFF.rwTEXNAT:
        // Texture tex = new Texture();
        // int platformID = br.readUInt32();
        // int filterFlags = br.readUInt16();
        // byte texU = br.readByte();
        // byte texV = br.readByte();
        // String difTexName = br.readNullTerminatedString(32);
        // String alphaTexName = br.readNullTerminatedString(32);
        // int rasterFormat = br.readUInt32();
        // int fourcc = br.readUInt32();
        // int imgWidth = br.readUInt16();
        // int imgHeight = br.readUInt16();
        // byte bbp = br.readByte();
        // byte mipmapCount = br.readByte();
        // byte rasterType = br.readByte();
        // byte dxtCompression = br.readByte();
        // //
        // tex.height = imgHeight;
        // tex.width = imgWidth;
        // tex.compression = fourcc;
        // tex.difTexName = difTexName;
        // tex.alphaTexName = alphaTexName;
        // txd.addTexture(tex);
        // // Message.displayMsgLow("platformID: " + platformID);
        // // Message.displayMsgLow("Filter flags: " + filterFlags);
        // // Message.displayMsgLow("texU: " + texU);
        // // Message.displayMsgLow("texV: " + texV);
        // // Message.displayMsgLow("difTexName: " + difTexName);
        // // Message.displayMsgLow("alphaTexName: " + alphaTexName);
        // // Message.displayMsgLow("rasterFormat: " + rasterFormat);
        // // Message.displayMsgLow("fourcc: " + fourcc);
        // System.out.println("ImgWidth: " + imgWidth);
        // System.out.println("ImgHeight: " + imgHeight);
        // System.out.println("bbp: " + bbp);
        // System.out.println("MipMapCount: " + mipmapCount);
        // System.out.println("rasterType: " + rasterType);
        // System.out.println("dxtCompression: " + dxtCompression);
        //
        // if (mipmapCount > 0) {
        // for (int j = 0; j < mipmapCount; j++) {
        // if (bbp == 8) {
        // br.skipBytes(1024);
        // System.out.println("Skipping pallete");
        // }
        //
        // int dataSize = br.readUInt32();
        // System.out.println("DataSize: " + dataSize);
        // System.out.println("offset: " + br.getCurrentOffset());
        // tex.dataSize = dataSize;
        // tex.data = br.toArray(dataSize);
        // if (gl != null)
        // br.setCurrentOffset(br.getCurrentOffset() - dataSize);
        //
        // imgWidth = imgWidth / (1 << j);
        // imgHeight = imgHeight / (1 << j);
        // if (imgWidth == 0) {
        // imgWidth = 1;
        // }
        // if (imgHeight == 0) {
        // imgHeight = 1;
        // }
        //
        // texName[texID] = difTexName;
        // // Message.displayMsgHigh("Binding texture: " + texID);
        // if (gl != null) {
        // gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[texID]);
        //
        // // 827611204 - dxt1
        // // 861165636 - dxt3
        //
        // if (fourcc == 827611204)
        // gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGB_S3TC_DXT1_EXT, imgWidth, imgHeight,
        // 0, dataSize, br.getByteBuffer(dataSize));
        // else if (fourcc == 861165636)
        // gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT, imgWidth, imgHeight,
        // 0, dataSize, br.getByteBuffer(dataSize));
        // else {
        //
        // switch (bbp) {
        // case 8:
        // // make 32 from 8
        // // if (fourcc == 0)
        // // gl.glTexImage2D(gl.GL_TEXTURE_2D, 0,
        // // gl.GL_RGBA4, imgWidth, imgHeight, 0,
        // // gl.GL_RGBA, gl.GL_UNSIGNED_BYTE,
        // // br.getByteBuffer(dataSize));
        // break;
        // case 16:
        // switch (dxtCompression) {
        // case 0:
        // gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGB_S3TC_DXT1_EXT,
        // imgWidth, imgHeight, 0, dataSize, br.getByteBuffer(dataSize));
        // break;
        // case 3:
        // gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT,
        // imgWidth, imgHeight, 0, dataSize, br.getByteBuffer(dataSize));
        // break;
        // case 5:
        // gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT,
        // imgWidth, imgHeight, 0, dataSize, br.getByteBuffer(dataSize));
        // break;
        // default:
        // gl.glCompressedTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_COMPRESSED_RGB_S3TC_DXT1_EXT,
        // imgWidth, imgHeight, 0, dataSize, br.getByteBuffer(dataSize));
        // }
        // break;
        // case 32:
        // if (platformID == 9)
        // fourcc = dxtCompression;
        //
        // // swap rgba
        //
        // if (fourcc == 0) {
        // gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, imgWidth, imgHeight, 0, GL.GL_RGBA,
        // GL.GL_UNSIGNED_BYTE, br.getByteBuffer(dataSize));
        // } else {
        // gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 4, imgWidth, imgHeight, 0, GL.GL_RGBA,
        // GL.GL_UNSIGNED_BYTE, br.getByteBuffer(dataSize));
        // }
        // }
        // }
        // gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        // gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        // gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        // gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        // gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        // }
        // texID++;
        // break;
        // }
        // }
        // }
        // break;
        // }
        // lastID = secID;
        // if (br.moreToRead() < 16)
        // break;
        // else {
        // secID = br.readUInt32();
        // secSize = br.readUInt32();
        // secVersion = br.readUInt32();
        // }
        // if (secID == -1)
        // break;
        // }
        //
        // txd.texName = texName;

        return textureId;
    }

}
