package nl.shadowlink.tools.shadowmapper.render;

import com.jogamp.opengl.GL;
import nl.shadowlink.tools.shadowmapper.gui.FileManager;

/**
 * @author Shadow-Link
 */
public class RenderCollision {
    private Camera camera;
    private FileManager fm;

    public RenderCollision() {
    }

    public void init(GL gl, Camera camera, FileManager fm) {
        this.fm = fm;
        this.camera = camera;
    }

    public void display(GL gl) {

        gl.glFlush();
    }

}
