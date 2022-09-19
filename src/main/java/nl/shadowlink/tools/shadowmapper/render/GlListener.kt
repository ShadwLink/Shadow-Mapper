package nl.shadowlink.tools.shadowmapper.render

import com.jogamp.common.nio.Buffers
import com.jogamp.opengl.*
import com.jogamp.opengl.fixedfunc.GLMatrixFunc
import com.jogamp.opengl.glu.GLU
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.gui.MainForm
import nl.shadowlink.tools.shadowmapper.gui.PickingType
import java.awt.AWTException
import java.awt.Point
import java.awt.Robot
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.nio.IntBuffer

/**
 * @author Shadow-Link
 */
class GlListener(
    private val main: MainForm,
    private val fm: FileManager,
    private val onFpsUpdateListener: ((fps: Float) -> Unit)? = null
) : GLEventListener {
    @JvmField
    val camera = Camera(0f, 2f, 5f, 0f, 2.5f, 0f, 0f, 1f, 0f)

    private val renderMap: RenderMap = RenderMap(GameType.GTA_IV, camera, fm)
    private val renderWater: RenderWater = RenderWater(fm)
    private val renderCars: RenderVehicles = RenderVehicles(fm)

    private val camSpeed = 0.5f
    private var wireFrame = false
    private var pick = false

    @JvmField
    var displayWater = true

    @JvmField
    var displayMap = true
    var displayZones = false

    @JvmField
    var displayCars = true
    private var mousePos = Point(0, 0)
    private var canvasPos = Point(0, 0)

    // Picking stuff
    private var selectBuf = IntArray(512)
    private var selectBuffer: IntBuffer = Buffers.newDirectIntBuffer(512)

    // Used for FPS
    private var fps = 0.0f
    private var previousTime: Long = 0

    fun keyPressed(evt: KeyEvent) {
        if (evt.keyCode == KeyEvent.VK_W) {
            camera.moveCamera(camSpeed)
        }
        if (evt.keyCode == KeyEvent.VK_S) {
            camera.moveCamera(-camSpeed)
        }
        if (evt.keyCode == KeyEvent.VK_A) {
            camera.strafeCamera(-camSpeed)
        }
        if (evt.keyCode == KeyEvent.VK_D) {
            camera.strafeCamera(camSpeed)
        }
    }

    fun mouseMoved(evt: MouseEvent) {
        try {
            val robby = Robot()
            val newMousePos = evt.point
            robby.mouseMove(canvasPos.x + mousePos.x, canvasPos.y + mousePos.y + 41)
            val angleY = (mousePos.x - canvasPos.x - (newMousePos.x - canvasPos.x)).toFloat() / 500
            val angleZ = (mousePos.y - canvasPos.y - (newMousePos.y - canvasPos.y)).toFloat() / 500
            val viewY = camera.viewY.toDouble()
            camera.viewY = (viewY + angleZ).toFloat()
            if (camera.viewY - camera.posY > 8) {
                camera.viewY = camera.posY + 8
            }
            if (camera.viewY - camera.viewY < -8) {
                camera.viewY = camera.posY - 8
            }
            camera.rotateView(-angleY)
        } catch (ex: AWTException) {
            println(ex)
        }
    }

    override fun display(drawable: GLAutoDrawable) {
        val gl = drawable.gl.gL2
        val glu = GLU()
        gl.glClear(GL.GL_COLOR_BUFFER_BIT or GL.GL_DEPTH_BUFFER_BIT)
        if (pick) startPicking(gl, mousePos.x, mousePos.y)
        gl.glLoadIdentity()
        glu.gluLookAt(
            camera.posX, camera.posY, camera.posZ, camera.viewX, camera.viewY,
            camera.viewZ, camera.upX, camera.upY, camera.upZ
        )
        if (wireFrame) {
            gl.glPolygonMode(GL.GL_FRONT, GL2.GL_LINE)
        } else {
            gl.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL)
        }
        gl.glRotatef(270f, 1.0f, 0.0f, 0.0f)
        if (displayMap) {
            renderMap.display(gl)
        }
        if (displayWater) {
            renderWater.display(gl)
        }
        if (displayCars) {
            // TODO: Fix this at some point
//            renderCars.display(gl);
        }
        if (pick) {
            stopPicking(gl)
            pick = false
        }
        updateFPS()
    }

    private fun startPicking(gl: GL2, mouseX: Int, mouseY: Int) {
        val glu = GLU()
        val viewport = IntArray(4)
        gl.glSelectBuffer(512, selectBuffer)
        gl.glGetIntegerv(GL.GL_VIEWPORT, viewport, 0)
        gl.glRenderMode(GL2.GL_SELECT)
        gl.glInitNames()
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION)
        gl.glPushMatrix()
        gl.glLoadIdentity()
        glu.gluPickMatrix(mouseX.toFloat(), (viewport[3] - mouseY).toFloat(), 5f, 5f, viewport, 0)
        val ratio = (viewport[2] + 0.0).toFloat() / viewport[3]
        glu.gluPerspective(45.0, ratio.toDouble(), 0.1, 1000.0)
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW)
    }

    private fun stopPicking(gl: GL2) {
        gl.glMatrixMode(GL2.GL_PROJECTION)
        gl.glPopMatrix()
        gl.glMatrixMode(GL2.GL_MODELVIEW)
        gl.glFlush()
        val hits = gl.glRenderMode(GL2.GL_RENDER)
        if (hits != 0) {
            selectBuffer[selectBuf]
            processHits(hits, selectBuf)
            selectBuf = IntArray(512)
            selectBuffer = Buffers.newDirectIntBuffer(512)
        } else {
            fm.setSelection(-1, -1, -1)
        }
    }

    private fun processHits(hits: Int, buffer: IntArray) {
        var names: Int
        var ptr = 0
        var cPickType = -1
        var cParam2 = -1
        var cParam3 = -1
        var minZ = -0x1
        for (i in 0 until hits) {
            names = buffer[ptr]
            println("Number of names for hit: $names")
            ptr++
            val z1 = buffer[ptr]
            println("z1 is " + buffer[ptr])
            ptr++
            println("z2 is " + buffer[ptr])
            ptr++
            if (names != 0) {
                val pickType = buffer[ptr]
                var param2 = -1
                var param3 = -1
                ptr++
                println("PickType: $pickType")
                when (pickType) {
                    PickingType.map -> {
                        param2 = buffer[ptr]
                        ptr++
                        param3 = buffer[ptr]
                        ptr++
                        println("IPL: $param2")
                        println("Item: $param3")
                    }

                    PickingType.water -> {
                        param2 = buffer[ptr]
                        ptr++
                        println("Plane: $param2")
                    }

                    PickingType.car -> {
                        param2 = buffer[ptr]
                        ptr++
                        param3 = buffer[ptr]
                        ptr++
                        println("IPL: $param2")
                        println("CarID: $param3")
                    }

                    else -> println("Picked nothing usefull")
                }
                if (z1 < minZ) {
                    minZ = z1
                    cPickType = pickType
                    cParam2 = param2
                    cParam3 = param3
                }
            }
        }
        fm.setSelection(cPickType, cParam2, cParam3)
        main.selectionChanged()
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
        val profile = GLProfile.get(GLProfile.GL2)
        val caps = GLCapabilities(profile)
        caps.hardwareAccelerated = true
        caps.doubleBuffered = true
        val gl = drawable.gl.gL2
        System.err.println("INIT GL IS: " + gl.javaClass.name)
        gl.glClearColor(0.250f, 0.250f, 0.250f, 0.0f)
        gl.glEnable(GL.GL_TEXTURE_2D)
        gl.glEnable(GL.GL_DEPTH_TEST)
        gl.glEnable(GL.GL_CULL_FACE)
        gl.glCullFace(GL.GL_BACK)
        gl.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL)
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA)
        gl.glEnable(GL.GL_BLEND)
        // gl.glDisable(gl.GL_COLOR_MATERIAL);

        renderWater.init()
//        renderCars.init(gl, fm);
    }

    private fun updateFPS() {
        val currentTime = System.currentTimeMillis()
        ++fps
        if (currentTime - previousTime >= 1000) {
            previousTime = currentTime
            onFpsUpdateListener?.invoke(fps)
            fps = 0.0f
        }
    }

    fun setWireFrame(wireFrame: Boolean) {
        this.wireFrame = wireFrame
    }

    fun setPick() {
        println("Start picking")
        pick = true
    }

    fun setCurrentMousePos(pos: Point) {
        mousePos = pos
    }

    fun setCanvasPosition(canvasPos: Point) {
        this.canvasPos = canvasPos
    }

    override fun dispose(arg0: GLAutoDrawable) {
        // TODO Auto-generated method stub
    }

    fun reloadMap() {
        renderMap.reload = true
    }
}