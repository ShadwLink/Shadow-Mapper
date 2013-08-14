/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Render;

import javax.media.opengl.GL2;

import shadowmapper.FileManager;
import shadowmapper.Finals;
import Texturedic.TextureDic;
import Water.WaterPlane;
import file_io.Vector3D;

/**
 * 
 * @author Kilian Steenman (Shadow-Link)
 *
 */
public class RenderWater {
    public FileManager fm;
    private int[] waterTex = null;

    public void init(GL2 gl, FileManager fm){
        this.fm = fm;
        TextureDic WTD = new TextureDic(fm.getGameDir() + "/pc/textures/water.wtd", null, Finals.gIV, gl, 23655);
        waterTex = WTD.textureId;
    }

    public void display(GL2 gl){
        if(fm != null && waterTex != null){
			gl.glBindTexture(GL2.GL_TEXTURE_2D, waterTex[0]);
            drawWater(gl);
        }
    }

    public void drawWater(GL2 gl){
        gl.glPushName(Finals.pickWater);
        gl.glBegin(GL2.GL_QUADS);

        for(int i = 0; i < fm.waters[0].planes.size(); i++){
            WaterPlane plane = fm.waters[0].planes.get(i);
            if(plane.selected) gl.glColor3f(0.9f,0,0);
            else gl.glColor4f(0,0.4f,1.0f, 0.5f);

            Vector3D p0 = plane.points[0].coord;
            Vector3D p1 = plane.points[1].coord;
            Vector3D p2 = plane.points[2].coord;
            Vector3D p3 = plane.points[3].coord;

            gl.glPushName(i);
            gl.glTexCoord2f(plane.u, plane.u); gl.glVertex3f(p0.x, p0.y, p0.z);
            gl.glTexCoord2f(plane.u, plane.v); gl.glVertex3f(p2.x, p2.y, p2.z);
            gl.glTexCoord2f(plane.v, plane.v); gl.glVertex3f(p3.x, p3.y, p3.z);
            gl.glTexCoord2f(plane.v, plane.u); gl.glVertex3f(p1.x, p1.y, p1.z);
            gl.glPopName();
        }

        gl.glEnd();
        gl.glColor3f(1.0f,1.0f,1.0f);
        gl.glPopName();
    }
}
