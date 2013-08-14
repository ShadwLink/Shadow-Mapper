/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Render;

import javax.media.opengl.GL;
import shadowmapper.FileManager;

/**
 *
 * @author Kilian
 */
public class RenderCollision {
    private Camera camera;
    private FileManager fm;

    public RenderCollision(){
    }

    public void init(GL gl, Camera camera, FileManager fm){
        this.fm = fm;
        this.camera = camera;
    }

    public void display(GL gl){

        gl.glFlush();
    }

}
