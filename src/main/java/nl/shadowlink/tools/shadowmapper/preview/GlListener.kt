package nl.shadowlink.tools.shadowmapper.preview

import com.jogamp.opengl.*
import com.jogamp.opengl.fixedfunc.GLMatrixFunc
import com.jogamp.opengl.glu.GLU
import nl.shadowlink.tools.io.ByteReader
import nl.shadowlink.tools.shadowlib.model.model.Model
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic
import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.render.Camera
import nl.shadowlink.tools.shadowmapper.utils.toGl
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent

/**
 * @author Shadow-Link
 */
class GlListener : GLEventListener {
    var fm: FileManager? = null
    var camera: Camera? = null
    private var modelZoom = -20.0f
    private var rotationX = 0.0f
    private var rotationY = 0.0f
    private var dragX = -1
    private var dragY = -1
    var mdl: Model? = Model()
    var txd: TextureDic? = null

    // public WBDFile wbd;
    // public WBNFile wbn;
    var type = -1
    var size = -1
    var br: ByteReader? = null
    var load = false
    private var selected = 0
    private var selPoly = 0
    fun mouseWheelMoved(evt: MouseWheelEvent) {
        if (evt.scrollType == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            modelZoom -= (evt.wheelRotation / 1.5).toFloat()
        }
    }

    fun mousePressed(evt: MouseEvent) {
        dragX = evt.x
        dragY = evt.y
    }

    fun keyPressed(evt: KeyEvent) {
        if (evt.keyCode == KeyEvent.VK_UP) {
            selPoly += 1
        }
        if (evt.keyCode == KeyEvent.VK_DOWN) {
            selPoly -= 1
        }
        if (evt.keyCode == KeyEvent.VK_P) {
            println("Sel poly: $selPoly")
        }
    }

    fun mouseMoved(evt: MouseEvent) {
        if (evt.modifiers == 4) {
            val newX = dragX - evt.x
            val newY = dragY - evt.y
            dragX = evt.x
            dragY = evt.y
            rotationX += newX.toFloat()
            rotationY += newY.toFloat()
        }
    }

    private var txdArray: IntArray? = null
    private fun loadModel(gl: GL2) {
        mdl = null
        mdl = Model()
        when (type) {
            0 -> mdl!!.loadWDR(br, size)
            1 -> mdl!!.loadWFT(br, size)
            2 -> mdl!!.loadWDD(br, size, null)
            3 -> txdArray = txd!!.toGl(gl)
            4 -> {}
            5 -> {}
        }
        load = false
    }

    fun loadTxdIntoGl(txd: TextureDic?) {
        this.txd = txd
        type = 3
        load = true
    }

    override fun display(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        val glu = GLU()
        if (load) {
            loadModel(gl)
        }
        gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)
        gl.glLoadIdentity()
        glu.gluLookAt(
            camera!!.posX, camera!!.posY, camera!!.posZ, camera!!.viewX, camera!!.viewY,
            camera!!.viewZ, camera!!.upX, camera!!.upY, camera!!.upZ
        )
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL)
        if (type == 3 && txdArray != null) {
            val height = 512
            val width = 512
            gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION)
            gl.glPushMatrix()
            gl.glLoadIdentity()
            gl.glOrtho(0.0, height.toDouble(), 0.0, width.toDouble(), -1.0, 1.0)
            gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
            gl.glPushMatrix()
            gl.glLoadIdentity()
            gl.glTranslatef(0f, 512f, 0f)
            gl.glRotatef(-90f, 0.0f, 0.0f, 1.0f)
            // TODO: Fix this
            gl.glBindTexture(GL.GL_TEXTURE_2D, txdArray!![selected])
            gl.glBegin(GL2ES3.GL_QUADS)
            gl.glTexCoord2d(0.0, 0.0)
            gl.glVertex2f(0f, 0f)
            gl.glTexCoord2d(0.0, 1.0)
            gl.glVertex2f(txd!!.textures[selected].height.toFloat(), 0f)
            gl.glTexCoord2d(1.0, 1.0)
            gl.glVertex2f(txd!!.textures[selected].height.toFloat(), txd!!.textures[selected].width.toFloat())
            gl.glTexCoord2d(1.0, 0.0)
            gl.glVertex2f(0f, txd!!.textures[selected].width.toFloat())
            gl.glEnd()
            gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION)
            gl.glPopMatrix()
            gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
            gl.glPopMatrix()
        } else if (type == 4 || type == 5) {
            // Do nothing yet
        } else {
            gl.glPushMatrix()
            gl.glTranslatef(0f, 2f, modelZoom)
            gl.glRotatef(270f, 1.0f, 0.0f, 0.0f)
            gl.glRotatef(rotationX, 0.0f, 0.0f, 1.0f)
            gl.glRotatef(rotationY, 0.0f, 1.0f, 0.0f)
            gl.glColor3f(1.0f, 1.0f, 1.0f)
            if (mdl!!.isLoaded) {
                mdl!!.render(gl)
            }
            gl.glPopMatrix()
        }
        gl.glFlush()
    }

    override fun reshape(drawable: GLAutoDrawable, x: Int, y: Int, width: Int, height: Int) {
        var height = height
        val gl = drawable.gl.gL2
        val glu = GLU()
        if (height <= 0) { // avoid a divide by zero error!
            height = 1
        }
        val h = width.toFloat() / height.toFloat()
        gl.glViewport(0, 0, width, height)
        gl.glMatrixMode(GL2.GL_PROJECTION)
        gl.glLoadIdentity()
        glu.gluPerspective(45.0, h.toDouble(), 0.1, 1000.0)
        gl.glMatrixMode(GL2.GL_MODELVIEW)
        gl.glLoadIdentity()
    }

    override fun init(drawable: GLAutoDrawable) {
        val glProfile = GLProfile.getDefault()
        val caps = GLCapabilities(glProfile)
        caps.hardwareAccelerated = true
        caps.doubleBuffered = true
        val gl = drawable.gl.gL2
        System.err.println("INIT GL IS: " + gl.javaClass.name)
        gl.glClearColor(0.250f, 0.250f, 0.250f, 0.0f)
        gl.glEnable(GL.GL_TEXTURE_2D)
        gl.glEnable(GL2.GL_DEPTH_TEST)
        gl.glEnable(GL2.GL_CULL_FACE)
        gl.glCullFace(GL2.GL_BACK)
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL)
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA)
        gl.glEnable(GL2.GL_BLEND)
        // gl.glDisable(gl.GL_COLOR_MATERIAL);
        camera = Camera(0f, 2f, 5f, 0f, 2.5f, 0f, 0f, 1f, 0f)
    }

    fun setSelected(sel: Int) {
        selected = sel
    }

    override fun dispose(arg0: GLAutoDrawable) {
        // TODO Auto-generated method stub
    }
}