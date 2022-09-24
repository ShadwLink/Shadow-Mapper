package nl.shadowlink.tools.shadowmapper.gui

import com.jogamp.opengl.awt.GLCanvas
import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.io.Vector4D
import nl.shadowlink.tools.shadowlib.ide.ItemObject
import nl.shadowlink.tools.shadowlib.img.Img
import nl.shadowlink.tools.shadowlib.img.ImgItem
import nl.shadowlink.tools.shadowlib.model.model.Model
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowmapper.FileManager
import org.netbeans.lib.awtextra.AbsoluteConstraints
import org.netbeans.lib.awtextra.AbsoluteLayout
import java.awt.event.ActionEvent
import javax.swing.*

/**
 * @author Shadow-Link
 */
class IDEForm : JFrame {
    private var mode = 0
    private var ideID = 0
    private var itemID = 0
    private val fm: FileManager
    private var boundsMin: Vector3D? = null
    private var boundsMax: Vector3D? = null
    private var boundsSphere: Vector4D? = null

    private var cancelButton = JButton()
    private var buttonModel = JButton()
    private var okButton = JButton()
    private var buttonTexture = JButton()
    private var gLCanvas1 = GLCanvas()
    private var jButton3 = JButton()
    private var jLabel1 = JLabel()
    private var jLabel2 = JLabel()
    private var jLabel3 = JLabel()
    private var jLabel4 = JLabel()
    private var jLabel6 = JLabel()
    private var jLabel7 = JLabel()
    private var jPanel1 = JPanel()
    private var labelBoundsMax = JLabel()
    private var labelBoundsMin = JLabel()
    private var labelSphere = JLabel()
    private var textDraw = JTextField()
    private var textFlag1 = JTextField()
    private var textFlag2 = JTextField()
    private var textModel = JTextField()
    private var textText = JTextField()
    private var textWDD = JTextField()

    constructor(ideID: Int, itemID: Int, fm: FileManager) {
        this.ideID = ideID
        this.itemID = itemID
        mode = 1 //edit item
        initComponents()
        val objs = fm.ides[ideID].itemObjs[itemID]
        textModel.text = objs.modelName
        textText.text = objs.textureName
        textWDD.text = objs.WDD
        textDraw.text = objs.drawDistance[0].toString()
        textFlag1.text = objs.flag1.toString()
        textFlag2.text = objs.flag2.toString()
        labelBoundsMin.text = "Bounds Min: ${objs.boundsMin.x}, ${objs.boundsMin.y}, ${objs.boundsMin.z}"
        labelBoundsMax.text = "Bounds Max: ${objs.boundsMax.x}, ${objs.boundsMax.y}, ${objs.boundsMax.z}"
        labelSphere.text =
            "Sphere: ${objs.boundsSphere.x}, ${objs.boundsSphere.y}, ${objs.boundsSphere.z}, ${objs.boundsSphere.w}"
        this.fm = fm
        this.isVisible = true
    }

    constructor(ideID: Int, fm: FileManager) {
        mode = 2 //new IDE entry
        initComponents()
        this.ideID = ideID
        this.fm = fm
        this.isVisible = true
    }

    private fun initComponents() {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "IDE Form"
        contentPane.layout = AbsoluteLayout()
        contentPane.add(gLCanvas1, AbsoluteConstraints(10, 10, 270, 270))
        textDraw.text = "300"
        contentPane.add(textDraw, AbsoluteConstraints(370, 110, 60, -1))
        jLabel1.icon = ImageIcon(javaClass.getResource("/Images/error.png")) // NOI18N
        jLabel1.text = "Model File: "
        contentPane.add(jLabel1, AbsoluteConstraints(290, 20, -1, -1))
        jLabel2.icon = ImageIcon(javaClass.getResource("/Images/error.png")) // NOI18N
        jLabel2.text = "Texture File: "
        contentPane.add(jLabel2, AbsoluteConstraints(290, 50, -1, -1))
        textWDD.text = "null"
        textWDD.isEnabled = false
        contentPane.add(textWDD, AbsoluteConstraints(370, 80, 140, -1))
        buttonTexture.text = "Select"
        buttonTexture.addActionListener { evt -> buttonTextureActionPerformed(evt) }
        contentPane.add(buttonTexture, AbsoluteConstraints(520, 50, 70, -1))
        buttonModel.text = "Select"
        buttonModel.addActionListener { evt -> buttonModelActionPerformed(evt) }
        contentPane.add(buttonModel, AbsoluteConstraints(520, 20, 70, -1))
        jLabel3.text = "Draw Distance:"
        contentPane.add(jLabel3, AbsoluteConstraints(290, 110, -1, -1))
        textFlag1.text = "0"
        contentPane.add(textFlag1, AbsoluteConstraints(370, 140, 60, -1))
        jLabel4.text = "Flag1:"
        contentPane.add(jLabel4, AbsoluteConstraints(290, 140, -1, -1))
        jLabel6.text = "Flag2:"
        contentPane.add(jLabel6, AbsoluteConstraints(290, 170, -1, -1))
        textModel.text = null
        contentPane.add(textModel, AbsoluteConstraints(370, 20, 140, -1))
        textText.text = "null"
        contentPane.add(textText, AbsoluteConstraints(370, 50, 140, -1))
        jLabel7.text = "WDD:"
        contentPane.add(jLabel7, AbsoluteConstraints(290, 80, -1, -1))
        textFlag2.text = "0"
        contentPane.add(textFlag2, AbsoluteConstraints(370, 170, 60, -1))
        jButton3.text = "Select"
        jButton3.isEnabled = false
        contentPane.add(jButton3, AbsoluteConstraints(520, 80, 70, -1))
        jPanel1.border = BorderFactory.createTitledBorder("Auto Generated")
        labelBoundsMin.text = "Bounds Min: "
        labelBoundsMax.text = "Bounds Max: "
        labelSphere.text = "Sphere: "
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(labelBoundsMin)
                                .addComponent(labelSphere)
                                .addComponent(labelBoundsMax)
                        )
                        .addContainerGap(213, Short.MAX_VALUE.toInt())
                )
        )
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout.createSequentialGroup()
                        .addComponent(labelBoundsMin)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelBoundsMax)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelSphere)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                )
        )
        contentPane.add(jPanel1, AbsoluteConstraints(290, 202, 300, 80))
        okButton.text = "Ok"
        okButton.addActionListener { evt -> buttonOkActionPerformed(evt) }
        contentPane.add(okButton, AbsoluteConstraints(540, 290, -1, -1))
        cancelButton.text = "Cancel"
        cancelButton.addActionListener { evt -> buttonCancelActionPerformed(evt) }
        contentPane.add(cancelButton, AbsoluteConstraints(470, 290, -1, -1))
        pack()
    }

    private fun buttonOkActionPerformed(evt: ActionEvent) {
        if (mode == 1) {
            saveIDEItem()
            fm.ides[ideID].setSaveRequired()
        } else if (mode == 2) {
            val tmpItem = ItemObject(GameType.GTA_IV)
            itemID = fm.addIDEItem(tmpItem, ideID)
            saveIDEItem()
        }
        dispose()
    }

    private fun saveIDEItem() {
        val objs = fm.ides[ideID].itemObjs[itemID]
        objs.modelName = textModel.text
        objs.textureName = textText.text
        objs.WDD = textWDD.text.ifEmpty { "null" }
        objs.drawDistance = FloatArray(1)
        objs.drawDistance[0] = textDraw.text.toFloat()
        objs.flag1 = textFlag1.text.toInt()
        objs.flag2 = textFlag2.text.toInt()
        boundsMax?.let { objs.boundsMax = it }
        boundsMin?.let { objs.boundsMin = it }
        boundsSphere?.let { objs.boundsSphere = it }
    }

    private fun setModel(imgItem: ImgItem, img: Img) {
        textModel.text = imgItem.nameWithoutExtension
        val rf = ReadFunctions(img.fileName)
        rf.seek(imgItem.offset)
        val tmpMdl = Model()
        tmpMdl.loadWDRSystem(rf.getByteReader(imgItem.size), imgItem.size)
        rf.closeFile()
        boundsMin = tmpMdl.boundsMin.toVector3D()
        boundsMax = tmpMdl.boundsMax.toVector3D()
        boundsSphere = tmpMdl.center.apply { w = tmpMdl.boundsMax.x }
        labelBoundsMin.text = "Bounds Min: $boundsMin"
        labelBoundsMax.text = "Bounds Max: $boundsMax"
        labelSphere.text = "Bounds Sphere: $boundsSphere"
        tmpMdl.reset()
    }

    private fun setTexture(texture: String) {
        textText.text = texture
    }

    private fun buttonCancelActionPerformed(evt: ActionEvent) {
        dispose()
    }

    private fun buttonModelActionPerformed(evt: ActionEvent) {
        Browser.createModelPickerBrowser(fm) { imgItem: ImgItem, img: Img ->
            setModel(imgItem, img)
        }
    }

    private fun buttonTextureActionPerformed(evt: ActionEvent) {
        Browser.createTxdPickerBrowser(fm) { imgItem: ImgItem, _: Img? ->
            setTexture(imgItem.nameWithoutExtension)
        }
    }
}