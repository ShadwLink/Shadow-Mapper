package nl.shadowlink.tools.shadowmapper.utils

import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import nl.shadowlink.tools.shadowlib.texturedic.Texture
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic
import java.nio.ByteBuffer

fun TextureDic.toGl(gl: GL2): IntArray {
    val textureIds = Array(textures.size) { 0 }.toIntArray()
    gl.glGenTextures(textures.size, textureIds, 0)

    this.textures.forEachIndexed { index, texture -> texture.toGlTexture(gl, textureIds[index]) }
    return textureIds
}

private fun Texture.toGlTexture(gl: GL2, textureId: Int) {
    gl.glBindTexture(GL.GL_TEXTURE_2D, textureId)

    val compressionType = when (dxtCompressionType) {
        "DXT1" -> GL.GL_COMPRESSED_RGB_S3TC_DXT1_EXT
        "DXT3" -> GL.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT
        "DXT5" -> GL.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT
        else -> throw IllegalStateException("Unknown compression type")
    }

    gl.glCompressedTexImage2D(
        GL.GL_TEXTURE_2D, 0, compressionType, width, height, 0, data.size, ByteBuffer.wrap(data)
    )

    // gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, 3, width, height, 0,
    // gl.GL_RGBA, gl.GL_UNSIGNED_BYTE,
    // br.getByteBuffer(dataSize));

//        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
    gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
}