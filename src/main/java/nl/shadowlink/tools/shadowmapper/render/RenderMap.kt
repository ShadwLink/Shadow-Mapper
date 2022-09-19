package nl.shadowlink.tools.shadowmapper.render

import com.jogamp.opengl.GL2
import nl.shadowlink.tools.io.ByteReader
import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.shadowlib.ide.Item_OBJS
import nl.shadowlink.tools.shadowlib.img.ImgItem
import nl.shadowlink.tools.shadowlib.ipl.Item_INST
import nl.shadowlink.tools.shadowlib.model.model.Model
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.gui.PickingType
import nl.shadowlink.tools.shadowmapper.utils.GlUtil.drawCube
import java.util.*
import kotlin.math.sqrt

/**
 * @author Shadow-Link
 */
class RenderMap(
    private val gameType: GameType,
    private val camera: Camera,
    private val fm: FileManager,
) {

    private var glDisplayList: IntArray = IntArray(0)
    var pick = false
    var reload = false
    var loading = false
    var added = false
    var tempIDE: Item_OBJS? = null
    var tempIPL: Item_INST? = null

    fun display(gl: GL2) {
        if (!loading) {
            if (reload) {
                loading = true
                println("Started loading")
                loadMap(gl)
                reload = false
                loading = false
                println("Loading finished")
            }
            if (added) {
                loading = true
                println("Loading added model")
                loadAddedModel(gl)
                loading = false
                added = false
                println("Loading added model finished")
            }
            gl.glPushName(PickingType.map)
            fm.ipls.forEachIndexed { iplIndex, ipl ->
                if (ipl.selected && ipl.itemsLoaded) {
                    gl.glPushName(iplIndex)
                    ipl.items_inst.forEachIndexed { instanceIndex, item ->
                        if (!item.name.lowercase(Locale.getDefault()).contains("lod")) {
                            if (getDistance(camera.pos, item.position) < item.drawDistance) {
                                gl.glPushName(instanceIndex)
                                if (item.selected) {
                                    gl.glColor3f(0.9f, 0f, 0f)
                                } else {
                                    gl.glColor3f(1f, 1f, 1f)
                                }
                                gl.glPushMatrix()
                                gl.glTranslatef(item.position.x, item.position.y, item.position.z)
                                gl.glRotatef(item.axisAngle.w, item.axisAngle.x, item.axisAngle.y, item.axisAngle.z)
                                gl.glCallList(glDisplayList[item.glListID])
                                gl.glPopMatrix()
                                gl.glPopName()
                            }
                        }
                    }
                    gl.glPopName()
                }
            }
            gl.glPopName()
        }
        gl.glFlush()
    }

    private fun loadMap(gl: GL2) {
        // Clear existing display list
        glDisplayList.forEach { dl -> gl.glDeleteLists(dl, 1) }

        val boolList: ArrayList<Boolean> = ArrayList<Boolean>()
        val ideList: ArrayList<Item_OBJS> = ArrayList<Item_OBJS>()
        fm.ipls.forEach { ipl ->
            if (ipl.selected) {
                ipl.items_inst.forEach { instance ->
                    var ideNumber = 0
                    var ideItem = fm.ides[ideNumber].findItem(instance.name) as? Item_OBJS
                    while (ideItem == null) {
                        ideNumber++
                        ideItem = if (ideNumber < fm.ides.size) {
                            fm.ides[ideNumber].findItem(instance.name) as? Item_OBJS
                        } else {
                            println("I really can't find in IDE: ${instance.name}")
                            break
                        }
                    }
                    if (ideItem != null) {
                        var found = false
                        for (i in ideList.indices) {
                            if (ideList[i] == ideItem) {
                                found = true
                                instance.glListID = i + 1
                                instance.drawDistance = ideItem.drawDistance[0]
                            }
                        }
                        if (!found) {
                            instance.glListID = ideList.size + 1
                            instance.drawDistance = ideItem.drawDistance[0]
                            ideList.add(ideItem)
                            boolList.add(false)
                        }
                    } else {
                        instance.glListID = 0
                    }
                }

                ipl.itemsLoaded = true
            }
        }
        glDisplayList = IntArray(ideList.size + 1)
        println("ideList Size: " + ideList.size)
        for (imgNumber in fm.imgs.indices) {
            val rf = ReadFunctions(fm.imgs[imgNumber].file)
            println("Opened: " + fm.imgs[imgNumber].fileName)
            for (i in ideList.indices) {
                if (!boolList[i]) {
                    var modelName = ""
                    var item: ImgItem? = null
                    if (ideList[i].WDD != "null") {
                        modelName = ideList[i].WDD + ".wdd"
                        item = fm.imgs[imgNumber].findItem(modelName)
                    } else {
                        modelName = ideList[i].modelName + ".wdr"
                        item = fm.imgs[imgNumber].findItem(modelName)
                        if (item == null) item = fm.imgs[imgNumber].findItem(ideList[i].modelName + ".wft")
                    }
                    if (item != null) {
                        rf.seek(item.offset)
                        var br = rf.getByteReader(item.size)
                        var mdl: Model? = null
                        if (item.name.endsWith(".wdr")) {
                            println(item.name)
                            mdl = Model().loadWDR(br, item.size)
                        } else if (item.name.endsWith(".wdd")) {
                            mdl = Model().loadWDD(br, item.size, ideList[i]!!.modelName)
                        } else if (item.name.endsWith(".wft")) {
                            println("Loading WFT: " + item.name)
                            mdl = Model().loadWFT(br, item.size)
                        }
                        val texName = ideList[i].textureName + ".wtd"
                        item = fm.imgs[imgNumber].findItem(texName)
                        if (item != null) {
                            rf.seek(item.offset)
                            br = rf.getByteReader(item.size)
                            val txd = TextureDic(texName, br, gameType, item.size)
                            mdl?.attachTXD(txd)
                        }
                        glDisplayList[i + 1] = gl.glGenLists(1)
                        gl.glNewList(glDisplayList[i + 1], GL2.GL_COMPILE)
                        if (mdl != null) {
                            mdl.render(gl)
                        } else {
                            drawCube(gl, 10f, 0.1f, 0.5f, 0.9f)
                        }
                        gl.glEndList()
                        mdl!!.reset()
                        boolList[i] = true
                    }
                }
            }
            rf.closeFile()
        }
    }

    private fun loadAddedModel(gl: GL2) {
        val tempList = glDisplayList // store all glInts to a temp array
        glDisplayList = IntArray(tempList.size + 1)
        for (i in tempList.indices) {
            glDisplayList[i] = tempList[i]
        }
        var item: ImgItem? = null
        var imgID = -1
        var i = 0
        while (item == null || i < fm.imgs.size) {
            if (tempIDE!!.WDD != "null") item = fm.imgs[i].findItem(tempIDE!!.WDD + ".wdd") else {
                item = fm.imgs[i].findItem(tempIDE!!.modelName + ".wdr")
                if (item == null) item = fm.imgs[i].findItem(tempIDE!!.modelName + ".wft")
            }
            if (item != null) imgID = i
            i++
        }
        if (item != null) {
            val rf = ReadFunctions(fm.imgs[imgID].fileName)
            rf.seek(item.offset)
            var br: ByteReader? = rf.getByteReader(item.size)
            var mdl: Model? = null
            if (item.name.endsWith(".wdr")) {
                println(item.name)
                mdl = Model().loadWDR(br, item.size) // load the
                // model
                // from img
            } else if (item.name.endsWith(".wdd")) {
                mdl = Model().loadWDD(br, item.size, tempIDE!!.modelName)
            } else if (item.name.endsWith(".wft")) {
                println("Loading WFT: " + item.name)
                mdl = Model().loadWFT(br, item.size)
            }
            br = null
            item = fm.imgs[imgID].findItem(tempIDE!!.textureName + ".wtd")
            if (item != null) {
                rf.seek(item.offset)
                br = rf.getByteReader(item.size)
                val txd = TextureDic(tempIDE!!.textureName + ".wtd", br, gameType, item.size)
                mdl?.attachTXD(txd)
            }
            glDisplayList[tempList.size] = gl.glGenLists(1)
            gl.glNewList(glDisplayList[tempList.size], GL2.GL_COMPILE)
            if (mdl != null) {
                mdl.render(gl)
            } else {
                drawCube(gl, 10f, 0.1f, 0.5f, 0.9f)
            }
            gl.glEndList()
            mdl!!.reset()
            rf.closeFile()
            tempIPL!!.glListID = tempList.size
        } else {
            tempIPL!!.glListID = 0
        }
        tempIDE = null
        tempIPL = null
    }

    private fun getDistance(test1: Vector3D, test2: Vector3D): Float {
        val dx = test1.x - test2.x
        val dy = 0 - test1.z - test2.y
        val dz = test1.y - test2.z
        return sqrt((dx * dx + dy * dy + dz * dz).toDouble()).toFloat()
    }

    fun addLoadModelToLoad(tempIPL: Item_INST?, tempIDE: Item_OBJS?) {
        this.tempIPL = tempIPL
        this.tempIDE = tempIDE
        added = true
    }
}