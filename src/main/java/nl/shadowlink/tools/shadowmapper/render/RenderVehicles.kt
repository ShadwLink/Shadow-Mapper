package nl.shadowlink.tools.shadowmapper.render

import com.jogamp.opengl.GL2
import nl.shadowlink.tools.shadowlib.model.model.Model
import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.gui.PickingType

/**
 * @author Shadow-Link
 */
class RenderVehicles(
    private val fm: FileManager,
) {

    private val glCarList = IntArray(5)

    fun init(gl: GL2) {
//        val txd = TextureDic("resources/vehicles.txd", null, GameType.GTA_IV, 23655)
//        val tempCar = Model()
//        tempCar.loadDFF("resources/car.dff")
//        tempCar.attachTXD(txd)
//        createGenList(gl, 0, tempCar)
//        tempCar.reset()
//        tempCar.loadDFF("resources/boat.dff")
//        tempCar.attachTXD(txd)
//        createGenList(gl, 1, tempCar)
//        tempCar.reset()
//        tempCar.loadDFF("resources/plane.dff")
//        tempCar.attachTXD(txd)
//        createGenList(gl, 2, tempCar)
//        tempCar.reset()
//        tempCar.loadDFF("resources/heli.dff")
//        tempCar.attachTXD(txd)
//        createGenList(gl, 3, tempCar)
//        tempCar.reset()
//        tempCar.loadDFF("resources/bike.dff")
//        tempCar.attachTXD(txd)
//        createGenList(gl, 4, tempCar)
    }

    private fun createGenList(gl: GL2, id: Int, mdl: Model) {
        glCarList[id] = gl.glGenLists(1)
        gl.glNewList(glCarList[id], GL2.GL_COMPILE)
        mdl.render(gl)
        gl.glEndList()
    }

    fun display(gl: GL2) {
        gl.glPushName(PickingType.car)
        fm.ipls.forEachIndexed { index, ipl ->
            gl.glPushName(index)
            if (ipl.selected) {
                ipl.items_cars.forEachIndexed { vehicleIndex, car ->
                    gl.glPushName(vehicleIndex)
                    gl.glPushMatrix()
                    if (car.selected) gl.glColor3f(0.9f, 0f, 0f) else gl.glColor3f(1f, 1f, 1f)

                    // Vector4D rot = item.rotation.getAxisAngle();
                    gl.glTranslatef(car.position.x, car.position.y, car.position.z)
                    gl.glRotatef(1.0f, car.rotation.x, car.rotation.y, car.rotation.z)
                    gl.glCallList(glCarList[0])
                    gl.glPopMatrix()
                    gl.glPopName()
                }
            }
            gl.glPopName()
        }
        gl.glPopName()
    }
}