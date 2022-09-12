package nl.shadowlink.tools.shadowmapper.utils

import com.jogamp.opengl.GL2

object GlUtil {

    @JvmStatic
    fun drawCube(gl: GL2, size: Float, red: Float, green: Float, blue: Float) {
        gl.glColor3f(red, green, blue) // Set The Color To Green
        gl.glBegin(GL2.GL_QUADS) // Start Drawing The Cube
        gl.glVertex3f(size, size, -size) // Top Right Of The Quad (Top)
        gl.glVertex3f(-size, size, -size) // Top Left Of The Quad (Top)
        gl.glVertex3f(-size, size, size) // Bottom Left Of The Quad (Top)
        gl.glVertex3f(size, size, size) // Bottom Right Of The Quad (Top)
        gl.glVertex3f(size, -size, size) // Top Right Of The Quad (Bottom)
        gl.glVertex3f(-size, -size, size) // Top Left Of The Quad (Bottom)
        gl.glVertex3f(-size, -size, -size) // Bottom Left Of The Quad (Bottom)
        gl.glVertex3f(size, -size, -size) // Bottom Right Of The Quad (Bottom)
        gl.glVertex3f(size, size, size) // Top Right Of The Quad (Front)
        gl.glVertex3f(-size, size, size) // Top Left Of The Quad (Front)
        gl.glVertex3f(-size, -size, size) // Bottom Left Of The Quad (Front)
        gl.glVertex3f(size, -size, size) // Bottom Right Of The Quad (Front)
        gl.glVertex3f(size, -size, -size) // Bottom Left Of The Quad (Back)
        gl.glVertex3f(-size, -size, -size) // Bottom Right Of The Quad (Back)
        gl.glVertex3f(-size, size, -size) // Top Right Of The Quad (Back)
        gl.glVertex3f(size, size, -size) // Top Left Of The Quad (Back)
        gl.glVertex3f(-size, size, size) // Top Right Of The Quad (Left)
        gl.glVertex3f(-size, size, -size) // Top Left Of The Quad (Left)
        gl.glVertex3f(-size, -size, -size) // Bottom Left Of The Quad (Left)
        gl.glVertex3f(-size, -size, size) // Bottom Right Of The Quad (Left)
        gl.glVertex3f(size, size, -size) // Top Right Of The Quad (Right)
        gl.glVertex3f(size, size, size) // Top Left Of The Quad (Right)
        gl.glVertex3f(size, -size, size) // Bottom Left Of The Quad (Right)
        gl.glVertex3f(size, -size, -size) // Bottom Right Of The Quad (Right)
        gl.glEnd()
        gl.glColor3f(1.0f, 1.0f, 1.0f)
    }
}