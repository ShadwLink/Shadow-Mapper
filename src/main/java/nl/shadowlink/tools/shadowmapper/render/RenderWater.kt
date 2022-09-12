package nl.shadowlink.tools.shadowmapper.render

import com.jogamp.opengl.GL2
import nl.shadowlink.tools.shadowmapper.gui.FileManager
import nl.shadowlink.tools.shadowmapper.gui.PickingType

/**
 * @author Shadow-Link
 */
class RenderWater(
    private val fm: FileManager
) {
    private val waterTex: IntArray? = null

    fun init() {
        // TODO: Fix loading water texture
//        TextureDic WTD = fm.loadWaterTexture();
//        waterTex = WTD.textureId;
    }

    fun display(gl: GL2) {
        if (waterTex != null) {
            //gl.glBindTexture(GL2.GL_TEXTURE_2D, waterTex[0]);
            drawWater(gl)
        }
    }

    private fun drawWater(gl: GL2) {
        gl.glPushName(PickingType.water)
        gl.glBegin(GL2.GL_QUADS)
        fm.waters[0].planes.forEachIndexed { index, plane ->
            if (plane.selected) gl.glColor3f(0.9f, 0f, 0f) else gl.glColor4f(0f, 0.4f, 1.0f, 0.5f)
            val p0 = plane.points[0]!!.coord
            val p1 = plane.points[1]!!.coord
            val p2 = plane.points[2]!!.coord
            val p3 = plane.points[3]!!.coord
            gl.glPushName(index)
            gl.glTexCoord2f(plane.u, plane.u)
            gl.glVertex3f(p0.x, p0.y, p0.z)
            gl.glTexCoord2f(plane.u, plane.v)
            gl.glVertex3f(p2.x, p2.y, p2.z)
            gl.glTexCoord2f(plane.v, plane.v)
            gl.glVertex3f(p3.x, p3.y, p3.z)
            gl.glTexCoord2f(plane.v, plane.u)
            gl.glVertex3f(p1.x, p1.y, p1.z)
            gl.glPopName()
        }
        gl.glEnd()
        gl.glColor3f(1.0f, 1.0f, 1.0f)
        gl.glPopName()
    }
}