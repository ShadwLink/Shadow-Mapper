package nl.shadowlink.tools.shadowmapper.preview;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.shadowlib.model.model.Model;
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic;
import nl.shadowlink.tools.shadowmapper.FileManager;
import nl.shadowlink.tools.shadowmapper.render.Camera;
import nl.shadowlink.tools.shadowmapper.utils.TextureLoaderKt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Shadow-Link
 */
public class GlListener implements GLEventListener {
    public FileManager fm;

    public Camera camera;
    private float camSpeed = 0.5f;

    private JLabel labelFPS;

    private boolean wireFrame = false;

    private Point mousePos = new Point(0, 0);
    private Point canvasPos = new Point(0, 0);

    // used for FPS
    private float fps = 0.0f;
    private long previousTime;

    private float modelZoom = -20.0f;
    private float rotationX = 0.0f;
    private float rotationY = 0.0f;

    private int dragX = -1;
    private int dragY = -1;

    public Model mdl = new Model();
    public TextureDic txd;
    // public WBDFile wbd;
    // public WBNFile wbn;
    public int type = -1;
    public int size = -1;
    public ByteReader br;
    public boolean load = false;
    private int selected = 0;

    private int selPoly = 0;

    public void mouseWheelMoved(MouseWheelEvent evt) {
        if (evt.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            modelZoom -= (evt.getWheelRotation() / 1.5);
        }
    }

    public void mousePressed(MouseEvent evt) {
        dragX = evt.getX();
        dragY = evt.getY();
    }

    public void keyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            selPoly += 1;
        }
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            selPoly -= 1;
        }
        if (evt.getKeyCode() == KeyEvent.VK_P) {
            System.out.println("Sel poly: " + selPoly);
        }
    }

    public void mouseMoved(MouseEvent evt) {
        if (evt.getModifiers() == 4) {
            int newX = dragX - evt.getX();
            int newY = dragY - evt.getY();
            dragX = evt.getX();
            dragY = evt.getY();
            rotationX += newX;
            rotationY += newY;
        }
    }

    private int[] txdArray = null;

    private void loadModel(GL2 gl) {
        mdl = null;
        mdl = new Model();
        switch (type) {
            case 0:
                mdl.loadWDR(br, size);
                break;
            case 1:
                mdl.loadWFT(br, size);
                break;
            case 2:
                mdl.loadWDD(br, size, null);
                break;
            case 3:
                txdArray = TextureLoaderKt.toGl(txd, gl);
                //txd = new TextureDic("", br, GameType.GTA_IV, size);
                break;
            case 4:
                // wbd = new WBDFile(br);
                break;
            case 5:
                // wbn = new WBNFile(br);
                break;
        }
        load = false;
    }

    public void loadTxdIntoGl(TextureDic txd) {
        this.txd = txd;
        type = 3;
        load = true;
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();

        if (load) {
            loadModel(gl);
        }

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        glu.gluLookAt(camera.getPosX(), camera.getPosY(), camera.getPosZ(), camera.getViewX(), camera.getViewY(),
                camera.getViewZ(), camera.getUpX(), camera.getUpY(), camera.getUpZ());

        if (wireFrame) {
            gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
        } else {
            gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        }

        if (type == 3 && txdArray != null) {
            int height = 512;
            int width = 512;
            gl.glMatrixMode(gl.GL_PROJECTION);
            gl.glPushMatrix();
            gl.glLoadIdentity();

            gl.glOrtho(0, height, 0, width, -1, 1);
            gl.glMatrixMode(gl.GL_MODELVIEW);
            gl.glPushMatrix();
            gl.glLoadIdentity();

            gl.glTranslatef(0, 512, 0);
            gl.glRotatef(-90, 0.0f, 0.0f, 1.0f);
            // TODO: Fix this
            gl.glBindTexture(gl.GL_TEXTURE_2D, txdArray[selected]);
            gl.glBegin(gl.GL_QUADS);
            gl.glTexCoord2d(0.0, 0.0);
            gl.glVertex2f(0, 0);
            gl.glTexCoord2d(0.0, 1.0);
            gl.glVertex2f(txd.textures.get(selected).getHeight(), 0);
            gl.glTexCoord2d(1.0, 1.0);
            gl.glVertex2f(txd.textures.get(selected).getHeight(), txd.textures.get(selected).getWidth());
            gl.glTexCoord2d(1.0, 0.0);
            gl.glVertex2f(0, txd.textures.get(selected).getWidth());
            gl.glEnd();

            gl.glMatrixMode(gl.GL_PROJECTION); // Select Projection
            gl.glPopMatrix(); // Pop The Matrix
            gl.glMatrixMode(gl.GL_MODELVIEW); // Select Modelview
            gl.glPopMatrix();

        } else if (type == 4 || type == 5) {
            // gl.glPushMatrix();
            // gl.glTranslatef(0, 2, modelZoom);
            // gl.glRotatef(270, 1.0f, 0.0f, 0.0f);
            // gl.glRotatef(rotationX, 0.0f, 0.0f, 1.0f);
            // gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f);
            //
            // gl.glColor3f(1.0f, 1.0f, 1.0f);
            // phBounds phTemp = null;
            // if (type == 4) {
            // phTemp = wbd.bounds.phBounds._items.get(selected);
            // } else if (type == 5) {
            // phTemp = wbn.phBound;
            // }
            // phTemp.renderBounds(gl);
            // switch (phTemp.type) {
            // case wbdFinals.phSphere:
            // // System.out.println("Sphere");
            // break;
            // case wbdFinals.phCapsule:
            // // System.out.println("Capsule");
            // break;
            // case wbdFinals.phBox:
            // // System.out.println("Box");
            // break;
            // case wbdFinals.phGeometry:
            // gl.glPushMatrix();
            // gl.glTranslatef(phTemp.phgeometry.transform.x,
            // phTemp.phgeometry.transform.y,
            // phTemp.phgeometry.transform.z);
            // /*
            // * for(int i = 0; i <
            // * wbd.bounds.phBounds._items.get(selected).phgeometry
            // * .verts.length; i++){ Vector3D vert =
            // * wbd.bounds.phBounds._items.get(selected).phgeometry.verts[i];
            // * gl.glPushMatrix(); gl.glTranslatef(vert.x, vert.y, vert.z);
            // * drawCube(gl, 0.01f, 0.5f, 0.5f, 0.5f); gl.glPopMatrix(); }
            // */
            // // phTemp.phgeometry.mdl.render(gl);
            //
            // float t = 0.0f;
            // float h = 0.0f;
            // float n = 0.0f;
            //
            // gl.glBegin(GL.GL_TRIANGLES); // begin triangle object
            // for (int i = 0; i < phTemp.phgeometry.polys.length; i++) { //
            // zolang
            // // we
            // // polygons
            // // hebben
            // if (selPoly == i) {
            // t = 0.00f;
            // h = 1.00f;
            // n = 0.00f;
            // }
            //
            // gl.glColor3f(t, h, n);
            // Polygon pol = phTemp.phgeometry.polys[i];
            // Vector3D verta = phTemp.phgeometry.verts[pol.a];
            // Vector3D vertb = phTemp.phgeometry.verts[pol.b];
            // Vector3D vertc = phTemp.phgeometry.verts[pol.c];
            // Vector3D vertd =
            // phTemp.phgeometry.verts[phTemp.phgeometry.polyd[i]];
            //
            // gl.glVertex3f(verta.x, verta.y, verta.z); // eerste vertex
            // // van polygon
            // gl.glVertex3f(vertb.x, vertb.y, vertb.z); // tweede vertex
            // // van polygon
            // gl.glVertex3f(vertc.x, vertc.y, vertc.z); // derde vertex
            // // van polygon
            // t = 1.0f;
            // h = 1.0f;
            // n = 1.0f;
            //
            // if (selPoly == i) {
            // t = 1.00f;
            // h = 0.00f;
            // n = 0.00f;
            // }
            //
            // if (phTemp.phgeometry.polyd2[i] != 65535) {
            // gl.glColor3f(t, h, n);
            // gl.glVertex3f(vertc.x, vertc.y, vertc.z); // eerste
            // // vertex
            // // van
            // // polygon
            // gl.glVertex3f(vertd.x, vertd.y, vertd.z); // tweede
            // // vertex
            // // van
            // // polygon
            // gl.glVertex3f(verta.x, verta.y, verta.z); // derde
            // // vertex
            // // van
            // // polygon
            // }
            //
            // t = 0.0f;
            // h = 0.0f;
            // n = 0.0f;
            // }
            // gl.glEnd();
            //
            // gl.glPopMatrix();
            // break;
            // case wbdFinals.phCurvedGeometry:
            // // System.out.println("Curved Geometry");
            // break;
            // case wbdFinals.phOctree:
            // // System.out.println("Octree");
            // break;
            // case wbdFinals.phQuadtree:
            // // System.out.println("Quadtree");
            // break;
            // case wbdFinals.phGrid:
            // // System.out.println("Grid");
            // break;
            // case wbdFinals.phRibbon:
            // // System.out.println("Ribbon");
            // break;
            // case wbdFinals.phBVH:
            // if (type == 4) {
            // gl.glPushMatrix();
            // gl.glTranslatef(phTemp.phBvh.transform.x,
            // phTemp.phBvh.transform.y, phTemp.phBvh.transform.z);
            // }
            // float r = 0.0f;
            // float g = 0.0f;
            // float b = 0.0f;
            // // for(int i = 0; i <
            // // wbd.bounds.phBounds._items.get(selected).phBvh.verts.length;
            // // i++){
            // // Vector3D vert =
            // // wbd.bounds.phBounds._items.get(selected).phBvh.verts[i];
            // // gl.glPushMatrix();
            // // gl.glTranslatef(vert.x, vert.y, vert.z);
            // // drawCube(gl, 0.01f, r, g, b);
            // // r += 0.15f;
            // // g += 0.15f;
            // // b += 0.15f;
            // // gl.glPopMatrix();
            // // }
            //
            // /*
            // * gl.glBegin(GL.GL_QUADS); //begin triangle object for (int i =
            // * 0; i < phTemp.phBvh.polys.length; i++) { //zolang we polygons
            // * hebben
            // *
            // * Polygon pol = phTemp.phBvh.polys[i]; Vector3D verta =
            // * phTemp.phBvh.verts[pol.a]; Vector3D vertb =
            // * phTemp.phBvh.verts[pol.b]; Vector3D vertc =
            // * phTemp.phBvh.verts[pol.c]; Vector3D vertd =
            // * phTemp.phBvh.verts[phTemp.phBvh.polyd[i]];
            // *
            // * gl.glVertex3f(verta.x, verta.y, verta.z); // Bottom Left
            // * gl.glVertex3f(vertb.x, vertb.y, vertb.z); // Bottom Right
            // * gl.glVertex3f(vertc.x, vertc.y, vertc.z); // Top Right
            // * gl.glVertex3f(vertd.x, vertd.y, vertd.z); // Top Left
            // *
            // *
            // * } gl.glEnd();
            // */
            //
            // // phTemp.phBvh.mdl.render(gl);
            //
            // r = 0.0f;
            // g = 0.0f;
            // b = 0.0f;
            //
            // gl.glBegin(GL.GL_TRIANGLES); // begin triangle object
            // for (int i = 0; i < phTemp.phBvh.polys.length; i++) { // zolang
            // // we
            // // polygons
            // // hebben
            // if (selPoly == i) {
            // r = 1.00f;
            // g = 0.00f;
            // b = 0.00f;
            // }
            // gl.glColor3f(r, g, b);
            // Polygon pol = phTemp.phBvh.polys[i];
            // Vector3D verta = phTemp.phBvh.verts[pol.a];
            // Vector3D vertb = phTemp.phBvh.verts[pol.b];
            // Vector3D vertc = phTemp.phBvh.verts[pol.c];
            // Vector3D vertd = phTemp.phBvh.verts[phTemp.phBvh.polyd[i]];
            //
            // gl.glVertex3f(verta.x, verta.y, verta.z); // eerste vertex
            // // van polygon
            // gl.glVertex3f(vertb.x, vertb.y, vertb.z); // tweede vertex
            // // van polygon
            // gl.glVertex3f(vertc.x, vertc.y, vertc.z); // derde vertex
            // // van polygon
            // r = 1.0f;
            // g = 1.0f;
            // b = 1.0f;
            //
            // if (selPoly == i) {
            // r = 0.00f;
            // g = 1.00f;
            // b = 0.00f;
            // }
            //
            // if (phTemp.phBvh.polyd2[i] == 65535) {
            // r = 0;
            // g = 0;
            // b = 1;
            // }
            // gl.glColor3f(r, g, b);
            // gl.glVertex3f(vertc.x, vertc.y, vertc.z); // eerste vertex
            // // van polygon
            // gl.glVertex3f(vertd.x, vertd.y, vertd.z); // tweede vertex
            // // van polygon
            // gl.glVertex3f(verta.x, verta.y, verta.z); // derde vertex
            // // van polygon
            //
            // r = 0.0f;
            // g = 0.0f;
            // b = 0.0f;
            // }
            // gl.glEnd();
            // if (type == 4) {
            // gl.glPopMatrix();
            // }
            // break;
            // case wbdFinals.phSurface:
            // // System.out.println("Surface");
            // break;
            // case wbdFinals.phComposite:
            // // System.out.println("Composite");
            // break;
            // }
            // gl.glPopMatrix();
        } else {
            gl.glPushMatrix();
            gl.glTranslatef(0, 2, modelZoom);
            gl.glRotatef(270, 1.0f, 0.0f, 0.0f);
            gl.glRotatef(rotationX, 0.0f, 0.0f, 1.0f);
            gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f);

            gl.glColor3f(1.0f, 1.0f, 1.0f);
            if (mdl.isLoaded()) {
                mdl.render(gl);
            }

            gl.glPopMatrix();
        }

        updateFPS();

        gl.glFlush();
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 0.1, 1000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        System.out.println("Not supported yet.");
    }

    public void init(GLAutoDrawable drawable) {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glProfile);
        caps.setHardwareAccelerated(true);
        caps.setDoubleBuffered(true);

        GL2 gl = drawable.getGL().getGL2();

        System.err.println("INIT GL IS: " + gl.getClass().getName());

        gl.glClearColor(0.250f, 0.250f, 0.250f, 0.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);

        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);

        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_BLEND);
        // gl.glDisable(gl.GL_COLOR_MATERIAL);

        camera = new Camera(0, 2, 5, 0, 2.5f, 0, 0, 1, 0);
    }

    private void updateFPS() {
        long currentTime = (System.currentTimeMillis());

        ++fps;

        if (currentTime - previousTime >= 1000) {
            previousTime = currentTime;
            if (labelFPS != null)
                labelFPS.setText("FPS: " + fps);
            fps = 0.0f;
        }
    }

    public void setFPSLabel(JLabel labelFPS) {
        this.labelFPS = labelFPS;
    }

    public void setWireFrame(boolean wireFrame) {
        this.wireFrame = wireFrame;
    }

    public void setCurrentMousePos(Point pos) {
        this.mousePos = pos;
    }

    public void setCanvasPosition(Point canvasPos) {
        this.canvasPos = canvasPos;
    }

    public void setFileManager(FileManager fm) {
        this.fm = fm;
    }

    public void setSelected(int sel) {
        this.selected = sel;
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

}
