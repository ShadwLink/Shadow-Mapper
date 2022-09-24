package nl.shadowlink.tools.shadowmapper.gui

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.FPSAnimator
import nl.shadowlink.tools.shadowlib.utils.encryption.EncryptionKeyExtractor
import nl.shadowlink.tools.shadowlib.utils.filechooser.ExtensionFilter
import nl.shadowlink.tools.shadowlib.utils.filechooser.FileChooserUtil.openFileChooser
import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.checklist.CheckListManager
import nl.shadowlink.tools.shadowmapper.gui.about.About
import nl.shadowlink.tools.shadowmapper.gui.install.Install
import nl.shadowlink.tools.shadowmapper.gui.install.InstallDialog
import nl.shadowlink.tools.shadowmapper.render.GlListener
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.showError
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.event.*
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

/**
 * @author Shadow-Link
 */
class MainForm : JFrame() {
    private val animator: FPSAnimator
    var glListener: GlListener
    var checkList: CheckListManager? = null
    private val fm = FileManager()

    private var buttonDelIDEItem = JButton()
    private var buttonDeleteIPLItem = JButton()
    private var buttonEditIDEItem = JButton()
    private var buttonEditIPLItem = JButton()
    private var buttonGroup1 = ButtonGroup()
    private var buttonNewIDEItem = JButton()
    private var buttonNewIPLItem = JButton()
    private var buttonSelectModel = JButton()
    private var buttonSelectModel1 = JButton()
    private var buttonSelectModel2 = JButton()
    private var checkCars = JCheckBox()
    private var checkMap = JCheckBox()
    private var checkWater = JCheckBox()
    private var checkZones = JCheckBox()
    private var comboIPLType = JComboBox<String>()
    private var gLCanvas1 = GLCanvas()
    private var jButton1 = JButton()
    private var jButton10 = JButton()
    private var jButton2 = JButton()
    private var jButton3 = JButton()
    private var jButton4 = JButton()
    private var mapCleanerButton = JButton()
    private var jButton6 = JButton()
    private var jButton7 = JButton()
    private var jButton9 = JButton()
    private var jCheckBox1 = JCheckBox()
    private var idesJList = JList<String>()
    private var fileMenu = JMenu()
    private var editMenu = JMenu()
    private var helpMenu = JMenu()
    private var mainMenuBar = JMenuBar()
    private var aboutMenuItem = JMenuItem()
    private var resourceBrowserMenuItem = JMenuItem()
    private var hashishGenMenuItem = JMenuItem()
    private var exitMenuItem = JMenuItem()
    private var saveInstallMenuItem = JMenuItem()
    private var mapCleanerMenuItem = JMenuItem()
    private var jPanel1 = JPanel()
    private var jPanel2 = JPanel()
    private var jPanel3 = JPanel()
    private var jPanel4 = JPanel()
    private var jScrollPane1 = JScrollPane()
    private var jScrollPane2 = JScrollPane()
    private var jScrollPane3 = JScrollPane()
    private var jScrollPane4 = JScrollPane()
    private var jScrollPane5 = JScrollPane()
    private var jToggleButton1 = JToggleButton()
    private var jToggleButton2 = JToggleButton()
    private var jToggleButton3 = JToggleButton()
    private var labelCameraPosition = JLabel()
    private var labelFPS = JLabel()
    private var labelLodName = JLabel()
    private var labelModelName = JLabel()
    private var labelPosition = JLabel()
    private var labelRotation = JLabel()
    private var labelTextureName = JLabel()
    private var listIDE = JTabbedPane()
    private var listIDEItems = JList<String>()
    private var listIPL = JList<String>()
    private var listIPLItems = JList<String>()
    private var listScene = JList<String>()
    private var panelIDE = JPanel()
    private var panelIPL = JPanel()
    private var panelMapper = JPanel()
    private var panelRender = JPanel()
    private var spinnerPosX = JSpinner()
    private var spinnerPosY = JSpinner()
    private var spinnerPosZ = JSpinner()
    private var spinnerRotX = JSpinner()
    private var spinnerRotY = JSpinner()
    private var spinnerRotZ = JSpinner()
    private var textLODName = JTextField()
    private var textModelName = JTextField()
    private var textTextureName = JTextField()
    private var textX = JTextField()
    private var textY = JTextField()
    private var textZ = JTextField()

    /**
     * Creates new form Main
     */
    init {
        glListener = GlListener(this, fm) { fps: Float ->
            labelFPS.text = "FPS: $fps"
        }
        this.iconImage = Toolkit.getDefaultToolkit().createImage("icon.png")
        initComponents()
        this.extendedState = MAXIMIZED_BOTH
        animator = FPSAnimator(gLCanvas1, 60, true)
        animator.start()
        glListener.setCanvasPosition(gLCanvas1.location)
        this.isVisible = true
        InstallDialog { install: Install ->
            val loadingDialog = LoadingDialog()
            val keyExtractor = EncryptionKeyExtractor()
            val key = keyExtractor.getKey(install.path)
            if (key == null) {
                this.showError("Error loading install", "Unable to detect encryption key")
                dispose()
            }
            fm.startLoading(loadingDialog, install, key!!)
        }.isVisible = true
    }

    private fun initComponents() {
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        title = "Shadow Mapper"
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(evt: WindowEvent) {
                formWindowClosing(evt)
            }
        })
        gLCanvas1.addGLEventListener(glListener)
        gLCanvas1.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(evt: MouseEvent) {
                gLCanvas1MouseClicked(evt)
            }

            override fun mousePressed(evt: MouseEvent) {
                gLCanvas1MousePressed(evt)
            }
        })
        gLCanvas1.addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(evt: MouseEvent) {
                gLCanvas1MouseDragged(evt)
            }
        })
        gLCanvas1.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(evt: KeyEvent) {
                gLCanvas1KeyPressed(evt)
            }
        })
        jButton1.text = "Render"
        jButton1.addActionListener { evt -> jButton1ActionPerformed(evt) }
        listScene.model = fm.modelIPL
        checkList = CheckListManager(listScene).apply {
            setFileManager(fm)
        }
        listScene.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(evt: MouseEvent) {
                listSceneMouseClicked(evt)
            }
        })
        jScrollPane1.setViewportView(listScene)
        jPanel3.border = BorderFactory.createTitledBorder("What to render")
        checkCars.isSelected = true
        checkCars.text = "Cars"
        checkCars.addActionListener { evt -> checkCarsActionPerformed(evt) }
        checkZones.text = "Zones"
        checkZones.isEnabled = false
        checkWater.isSelected = true
        checkWater.text = "Water"
        checkWater.addItemListener { evt -> checkWaterItemStateChanged(evt) }
        checkMap.isSelected = true
        checkMap.text = "Map"
        checkMap.addItemListener { evt -> checkMapItemStateChanged(evt) }
        jCheckBox1.text = "Paths"
        jCheckBox1.isEnabled = false
        val jPanel3Layout = GroupLayout(jPanel3)
        jPanel3.layout = jPanel3Layout
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel3Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel3Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    jPanel3Layout.createSequentialGroup().addComponent(checkWater)
                                        .addGap(18, 18, 18).addComponent(checkCars)
                                        .addGap(18, 18, 18).addComponent(checkMap)
                                )
                                .addGroup(
                                    jPanel3Layout.createSequentialGroup().addComponent(checkZones)
                                        .addGap(18, 18, 18).addComponent(jCheckBox1)
                                )
                        )
                        .addContainerGap(81, Short.MAX_VALUE.toInt())
                )
        )
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel3Layout
                        .createSequentialGroup()
                        .addGroup(
                            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(checkWater).addComponent(checkCars)
                                .addComponent(checkMap)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(checkZones).addComponent(jCheckBox1)
                        )
                )
        )
        val panelRenderLayout = GroupLayout(panelRender)
        panelRender.layout = panelRenderLayout
        panelRenderLayout.setHorizontalGroup(
            panelRenderLayout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    panelRenderLayout.createSequentialGroup().addContainerGap().addComponent(jButton1)
                        .addContainerGap(205, Short.MAX_VALUE.toInt())
                )
                .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE.toInt())
        )
        panelRenderLayout.setVerticalGroup(
            panelRenderLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING
            ).addGroup(
                GroupLayout.Alignment.TRAILING,
                panelRenderLayout
                    .createSequentialGroup()
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE.toInt())
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(
                        jPanel3, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1)
                    .addContainerGap()
            )
        )
        listIDE.addTab("Scene", panelRender)
        listIPL.setModel(fm.modelIPL)
        listIPL.addListSelectionListener(ListSelectionListener { evt -> listIPLValueChanged(evt) })
        jScrollPane4.setViewportView(listIPL)
        jButton9.text = "New"
        jButton9.addActionListener { evt -> onAddIplClicked(evt) }
        jButton10.text = "Delete"
        buttonNewIPLItem.text = "New"
        buttonNewIPLItem.isEnabled = false
        buttonNewIPLItem.addActionListener { evt -> buttonNewIPLItemActionPerformed(evt) }
        buttonEditIPLItem.text = "Edit"
        buttonEditIPLItem.isEnabled = false
        buttonDeleteIPLItem.text = "Delete"
        buttonDeleteIPLItem.isEnabled = false
        buttonDeleteIPLItem.addActionListener { evt -> buttonDeleteIPLItemActionPerformed(evt) }
        listIPLItems.setModel(fm.modelIPLItems)
        listIPLItems.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(evt: MouseEvent) {
                listIPLItemsMouseClicked(evt)
            }
        })
        listIPLItems.addListSelectionListener { evt -> listIPLItemsValueChanged(evt) }
        jScrollPane5.setViewportView(listIPLItems)
        comboIPLType.setModel(
            DefaultComboBoxModel(
                arrayOf(
                    "Instances", "Garages", "Cars",
                    "Cull", "Strbig", "LODCull", "Zone", "Blok"
                )
            )
        )
        comboIPLType.isEnabled = false
        comboIPLType.addItemListener { evt -> comboIPLTypeItemStateChanged(evt) }
        val panelIPLLayout = GroupLayout(panelIPL)
        panelIPL.layout = panelIPLLayout
        panelIPLLayout
            .setHorizontalGroup(
                panelIPLLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(
                        panelIPLLayout
                            .createSequentialGroup()
                            .addContainerGap()
                            .addGroup(
                                panelIPLLayout
                                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(
                                        jScrollPane4,
                                        GroupLayout.Alignment.TRAILING,
                                        GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE.toInt()
                                    )
                                    .addComponent(
                                        jScrollPane5,
                                        GroupLayout.Alignment.TRAILING,
                                        GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE.toInt()
                                    )
                                    .addGroup(
                                        panelIPLLayout
                                            .createSequentialGroup()
                                            .addComponent(jButton9)
                                            .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED
                                            )
                                            .addComponent(jButton10)
                                    )
                                    .addGroup(
                                        panelIPLLayout
                                            .createSequentialGroup()
                                            .addComponent(
                                                buttonNewIPLItem,
                                                GroupLayout.PREFERRED_SIZE,
                                                53,
                                                GroupLayout.PREFERRED_SIZE
                                            )
                                            .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED
                                            )
                                            .addComponent(buttonEditIPLItem)
                                            .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED
                                            )
                                            .addComponent(buttonDeleteIPLItem)
                                            .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.UNRELATED
                                            )
                                            .addComponent(comboIPLType, 0, 73, Short.MAX_VALUE.toInt())
                                    )
                            ).addContainerGap()
                    )
            )
        panelIPLLayout.setVerticalGroup(
            panelIPLLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    panelIPLLayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            panelIPLLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton10).addComponent(jButton9)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(
                            jScrollPane4, GroupLayout.PREFERRED_SIZE, 278,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            panelIPLLayout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(
                                    comboIPLType, GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(buttonNewIPLItem).addComponent(buttonEditIPLItem)
                                .addComponent(buttonDeleteIPLItem)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE.toInt())
                        .addContainerGap()
                )
        )
        listIDE.addTab("IPL", panelIPL)
        idesJList.setModel(fm.modelIDE)
        idesJList.addListSelectionListener(ListSelectionListener { evt -> jList2ValueChanged(evt) })
        jScrollPane2.setViewportView(idesJList)
        jButton3.text = "New"
        jButton3.addActionListener { evt -> onAddIdeClicked(evt) }
        jButton4.text = "Delete"
        buttonNewIDEItem.text = "New"
        buttonNewIDEItem.isEnabled = false
        buttonNewIDEItem.addActionListener { evt -> buttonNewIDEItemActionPerformed(evt) }
        buttonDelIDEItem.text = "Delete"
        buttonDelIDEItem.isEnabled = false
        buttonDelIDEItem.addActionListener { evt -> buttonDelIDEItemActionPerformed(evt) }
        buttonEditIDEItem.text = "Edit"
        buttonEditIDEItem.isEnabled = false
        buttonEditIDEItem.addActionListener { evt -> buttonEditIDEItemActionPerformed(evt) }
        listIDEItems.setModel(fm.modelIDEItems)
        listIDEItems.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(evt: MouseEvent) {
                listIDEItemsMouseClicked(evt)
            }
        })
        listIDEItems.addListSelectionListener(ListSelectionListener { evt -> listIDEItemsValueChanged(evt) })
        jScrollPane3.setViewportView(listIDEItems)
        val panelIDELayout = GroupLayout(panelIDE)
        panelIDE.layout = panelIDELayout
        panelIDELayout
            .setHorizontalGroup(
                panelIDELayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        panelIDELayout
                            .createSequentialGroup()
                            .addContainerGap()
                            .addGroup(
                                panelIDELayout
                                    .createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addComponent(
                                        jScrollPane2,
                                        GroupLayout.Alignment.LEADING,
                                        GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE.toInt()
                                    )
                                    .addComponent(
                                        jScrollPane3,
                                        GroupLayout.Alignment.LEADING,
                                        GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE.toInt()
                                    )
                                    .addGroup(
                                        GroupLayout.Alignment.LEADING,
                                        panelIDELayout
                                            .createSequentialGroup()
                                            .addComponent(jButton3)
                                            .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED
                                            )
                                            .addComponent(jButton4)
                                    )
                                    .addGroup(
                                        GroupLayout.Alignment.LEADING,
                                        panelIDELayout
                                            .createSequentialGroup()
                                            .addComponent(buttonNewIDEItem)
                                            .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED
                                            )
                                            .addComponent(buttonEditIDEItem)
                                            .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED
                                            )
                                            .addComponent(buttonDelIDEItem)
                                    )
                            )
                            .addContainerGap()
                    )
            )
        panelIDELayout.setVerticalGroup(
            panelIDELayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    panelIDELayout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            panelIDELayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton3).addComponent(jButton4)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(
                            jScrollPane2, GroupLayout.PREFERRED_SIZE, 264,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(
                            panelIDELayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonNewIDEItem).addComponent(buttonEditIDEItem)
                                .addComponent(buttonDelIDEItem)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE.toInt())
                        .addContainerGap()
                )
        )
        listIDE.addTab("IDE", panelIDE)
        jPanel1.border = BorderFactory.createTitledBorder("Parameters")
        labelModelName.text = "Model: "
        labelTextureName.text = "Texture Dictionary:"
        labelLodName.text = "LOD Name: "
        textTextureName.isEditable = false
        textLODName.isEditable = false
        textModelName.isEditable = false
        buttonSelectModel.text = "..."
        buttonSelectModel.isEnabled = false
        buttonSelectModel1.text = "..."
        buttonSelectModel1.isEnabled = false
        buttonSelectModel2.text = "..."
        buttonSelectModel2.isEnabled = false
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(labelTextureName).addComponent(labelLodName)
                                .addComponent(labelModelName)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(textModelName, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE.toInt())
                                .addComponent(textTextureName)
                                .addComponent(textLODName)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    buttonSelectModel1,
                                    GroupLayout.PREFERRED_SIZE, 26,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    buttonSelectModel,
                                    GroupLayout.PREFERRED_SIZE, 26,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    buttonSelectModel2,
                                    GroupLayout.PREFERRED_SIZE, 26,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addContainerGap(11, Short.MAX_VALUE.toInt())
                )
        )
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel1Layout
                        .createSequentialGroup()
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelModelName)
                                .addComponent(buttonSelectModel)
                                .addComponent(
                                    textModelName, GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelTextureName)
                                .addComponent(
                                    textTextureName, GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(buttonSelectModel1)
                        )
                        .addGap(9, 9, 9)
                        .addGroup(
                            jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelLodName)
                                .addComponent(
                                    textLODName, GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(buttonSelectModel2)
                        )
                )
        )
        jPanel2.border = BorderFactory.createTitledBorder("Object Placement")
        labelPosition.text = "Position"
        labelRotation.text = "Rotation"
        spinnerRotX.model =
            SpinnerNumberModel(java.lang.Float.valueOf(0.0f), null, null, java.lang.Float.valueOf(1.0f))
        spinnerRotX.addChangeListener { evt -> spinnerRotXStateChanged(evt) }
        spinnerPosX.model =
            SpinnerNumberModel(java.lang.Float.valueOf(0.0f), null, null, java.lang.Float.valueOf(0.1f))
        spinnerPosX.addChangeListener { evt -> spinnerPosXStateChanged(evt) }
        spinnerPosY.model =
            SpinnerNumberModel(java.lang.Float.valueOf(0.0f), null, null, java.lang.Float.valueOf(1.0f))
        spinnerPosY.addChangeListener { evt -> spinnerPosYStateChanged(evt) }
        spinnerRotY.model =
            SpinnerNumberModel(java.lang.Float.valueOf(0.0f), null, null, java.lang.Float.valueOf(1.0f))
        spinnerRotY.addChangeListener { evt -> spinnerRotYStateChanged(evt) }
        spinnerPosZ.model =
            SpinnerNumberModel(java.lang.Float.valueOf(0.0f), null, null, java.lang.Float.valueOf(1.0f))
        spinnerPosZ.addChangeListener { evt -> spinnerPosZStateChanged(evt) }
        spinnerRotZ.model =
            SpinnerNumberModel(java.lang.Float.valueOf(0.0f), null, null, java.lang.Float.valueOf(1.0f))
        spinnerRotZ.addChangeListener { evt -> spinnerRotZStateChanged(evt) }
        val jPanel2Layout = GroupLayout(jPanel2)
        jPanel2.layout = jPanel2Layout
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel2Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(labelPosition).addComponent(labelRotation)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel2Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    spinnerRotX, GroupLayout.Alignment.TRAILING,
                                    GroupLayout.PREFERRED_SIZE, 58,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    spinnerPosX, GroupLayout.PREFERRED_SIZE, 58,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel2Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    spinnerRotY, GroupLayout.Alignment.TRAILING,
                                    GroupLayout.PREFERRED_SIZE, 58,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    spinnerPosY, GroupLayout.PREFERRED_SIZE, 58,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            jPanel2Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    spinnerRotZ, GroupLayout.Alignment.TRAILING,
                                    GroupLayout.PREFERRED_SIZE, 58,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    spinnerPosZ, GroupLayout.PREFERRED_SIZE, 58,
                                    GroupLayout.PREFERRED_SIZE
                                )
                        ).addContainerGap()
                )
        )
        jPanel2Layout
            .setVerticalGroup(
                jPanel2Layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(
                        jPanel2Layout
                            .createSequentialGroup()
                            .addContainerGap()
                            .addGroup(
                                jPanel2Layout
                                    .createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(
                                        jPanel2Layout
                                            .createSequentialGroup()
                                            .addComponent(
                                                spinnerPosZ,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE
                                            )
                                            .addPreferredGap(
                                                LayoutStyle.ComponentPlacement.RELATED
                                            )
                                            .addComponent(
                                                spinnerRotZ,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE
                                            )
                                    )
                                    .addGroup(
                                        jPanel2Layout
                                            .createParallelGroup(
                                                GroupLayout.Alignment.LEADING
                                            )
                                            .addGroup(
                                                jPanel2Layout
                                                    .createSequentialGroup()
                                                    .addGroup(
                                                        jPanel2Layout
                                                            .createParallelGroup(
                                                                GroupLayout.Alignment.BASELINE
                                                            )
                                                            .addComponent(
                                                                labelPosition
                                                            )
                                                            .addComponent(
                                                                spinnerPosX,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                            )
                                                    )
                                                    .addPreferredGap(
                                                        LayoutStyle.ComponentPlacement.RELATED
                                                    )
                                                    .addGroup(
                                                        jPanel2Layout
                                                            .createParallelGroup(
                                                                GroupLayout.Alignment.BASELINE
                                                            )
                                                            .addComponent(
                                                                labelRotation
                                                            )
                                                            .addComponent(
                                                                spinnerRotX,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.PREFERRED_SIZE
                                                            )
                                                    )
                                            )
                                            .addGroup(
                                                jPanel2Layout
                                                    .createSequentialGroup()
                                                    .addComponent(
                                                        spinnerPosY,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE
                                                    )
                                                    .addPreferredGap(
                                                        LayoutStyle.ComponentPlacement.RELATED
                                                    )
                                                    .addComponent(
                                                        spinnerRotY,
                                                        GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE,
                                                        GroupLayout.PREFERRED_SIZE
                                                    )
                                            )
                                    )
                            )
                            .addContainerGap(6, Short.MAX_VALUE.toInt())
                    )
            )
        jPanel4.border = BorderFactory.createTitledBorder("Debug")
        jButton6.text = "Convert IPL"
        jButton6.isEnabled = false
        jButton7.text = "Convert IDE"
        jButton7.isEnabled = false
        val jPanel4Layout = GroupLayout(jPanel4)
        jPanel4.layout = jPanel4Layout
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel4Layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            jPanel4Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton6)
                                .addComponent(
                                    jButton7, GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt()
                                )
                        )
                        .addContainerGap(150, Short.MAX_VALUE.toInt())
                )
        )
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    jPanel4Layout.createSequentialGroup().addComponent(jButton6)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                )
        )
        val panelMapperLayout = GroupLayout(panelMapper)
        panelMapper.layout = panelMapperLayout
        panelMapperLayout.setHorizontalGroup(
            panelMapperLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING
            ).addGroup(
                panelMapperLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        panelMapperLayout
                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(
                                panelMapperLayout
                                    .createSequentialGroup()
                                    .addComponent(
                                        jPanel1, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt()
                                    )
                                    .addGap(2, 2, 2)
                            )
                            .addGroup(
                                panelMapperLayout
                                    .createSequentialGroup()
                                    .addComponent(
                                        jPanel2, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt()
                                    )
                                    .addContainerGap()
                            )
                            .addGroup(
                                panelMapperLayout
                                    .createSequentialGroup()
                                    .addComponent(
                                        jPanel4, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt()
                                    )
                                    .addContainerGap()
                            )
                    )
            )
        )
        panelMapperLayout.setVerticalGroup(
            panelMapperLayout.createParallelGroup(
                GroupLayout.Alignment.LEADING
            ).addGroup(
                panelMapperLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addComponent(
                        jPanel2, GroupLayout.PREFERRED_SIZE, 90,
                        GroupLayout.PREFERRED_SIZE
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(
                        jPanel1, GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(
                        jPanel4, GroupLayout.DEFAULT_SIZE,
                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt()
                    ).addGap(136, 136, 136)
            )
        )
        listIDE.addTab("Properties", panelMapper)
        labelFPS.text = "FPS: 0"
        labelCameraPosition.text = "DirectPosition"
        textX.text = "0.0000"
        textX.autoscrolls = false
        textX.isEnabled = false
        textX.maximumSize = Dimension(6, 20)
        textX.addKeyListener(object : KeyAdapter() {
            override fun keyTyped(evt: KeyEvent) {
                textXKeyTyped(evt)
            }
        })
        textY.text = "0.0000"
        textY.isEnabled = false
        textZ.text = "0.0000"
        textZ.isEnabled = false
        buttonGroup1.add(jToggleButton1)
        jToggleButton1.icon = ImageIcon(javaClass.getResource("/Images/shaded.png")) // NOI18N
        jToggleButton1.isSelected = true
        jToggleButton1.toolTipText = "Shaded"
        jToggleButton1.isBorderPainted = false
        jToggleButton1.isFocusable = false
        jToggleButton1.selectedIcon = ImageIcon(javaClass.getResource("/Images/shadedSel.png")) // NOI18N
        buttonGroup1.add(jToggleButton2)
        jToggleButton2.icon = ImageIcon(javaClass.getResource("/Images/wireframe.png")) // NOI18N
        jToggleButton2.toolTipText = "Wireframe"
        jToggleButton2.isFocusable = false
        jToggleButton2.selectedIcon = ImageIcon(javaClass.getResource("/Images/wireframeSel.png")) // NOI18N
        jToggleButton2.addItemListener { evt -> jToggleButton2ItemStateChanged(evt) }
        buttonGroup1.add(jToggleButton3)
        jToggleButton3.icon = ImageIcon(javaClass.getResource("/Images/wireframeshaded.png")) // NOI18N
        jToggleButton3.isFocusable = false
        jToggleButton3.selectedIcon = ImageIcon(
            javaClass.getResource(
                "/Images/wireframeshadedSel.png"
            )
        ) // NOI18N
        jButton2.icon = ImageIcon(javaClass.getResource("/Images/browser.png")) // NOI18N
        jButton2.toolTipText = "Resource Browser"
        jButton2.border = null
        jButton2.isFocusable = false
        jButton2.addActionListener { evt -> onOpenResourceBrowserClicked(evt) }
        mapCleanerButton.icon = ImageIcon(javaClass.getResource("/Images/mapcleaner.png")) // NOI18N
        mapCleanerButton.toolTipText = "Map Cleaner"
        mapCleanerButton.border = null
        mapCleanerButton.isEnabled = false
        mapCleanerButton.isFocusable = false
        fileMenu.text = "File"
        saveInstallMenuItem.text = "Save install"
        saveInstallMenuItem.isEnabled = false
        saveInstallMenuItem.addActionListener { evt -> onSaveClicked(evt) }
        fileMenu.add(saveInstallMenuItem)
        exitMenuItem.text = "Exit"
        exitMenuItem.addActionListener { evt -> onCloseClicked(evt) }
        fileMenu.add(exitMenuItem)
        mainMenuBar.add(fileMenu)
        editMenu.text = "Edit"
        resourceBrowserMenuItem.text = "Resource Browser"
        resourceBrowserMenuItem.addActionListener { evt: ActionEvent -> onResourceBrowserClicked(evt) }
        editMenu.add(resourceBrowserMenuItem)
        hashishGenMenuItem.text = "HashishGen"
        hashishGenMenuItem.addActionListener { evt: ActionEvent -> onHashishGenClicked(evt) }
        editMenu.add(hashishGenMenuItem)
        mapCleanerMenuItem.text = "Map Cleaner"
        mapCleanerMenuItem.isEnabled = false
        editMenu.add(mapCleanerMenuItem)
        mainMenuBar.add(editMenu)
        helpMenu.text = "Help"
        aboutMenuItem.text = "About"
        aboutMenuItem.addActionListener { evt -> jMenuItem1ActionPerformed(evt) }
        helpMenu.add(aboutMenuItem)
        mainMenuBar.add(helpMenu)
        jMenuBar = mainMenuBar
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    GroupLayout.Alignment.TRAILING,
                    layout.createSequentialGroup()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jButton2)
                                        .addPreferredGap(
                                            LayoutStyle.ComponentPlacement.RELATED
                                        )
                                        .addComponent(mapCleanerButton)
                                        .addGap(18, 18, 18)
                                        .addComponent(
                                            jToggleButton2,
                                            GroupLayout.PREFERRED_SIZE, 24,
                                            GroupLayout.PREFERRED_SIZE
                                        )
                                        .addPreferredGap(
                                            LayoutStyle.ComponentPlacement.RELATED
                                        )
                                        .addComponent(
                                            jToggleButton3,
                                            GroupLayout.PREFERRED_SIZE, 24,
                                            GroupLayout.PREFERRED_SIZE
                                        )
                                        .addPreferredGap(
                                            LayoutStyle.ComponentPlacement.RELATED
                                        )
                                        .addComponent(
                                            jToggleButton1,
                                            GroupLayout.PREFERRED_SIZE, 24,
                                            GroupLayout.PREFERRED_SIZE
                                        )
                                )
                                .addGroup(
                                    GroupLayout.Alignment.TRAILING,
                                    layout.createSequentialGroup()
                                        .addGroup(
                                            layout.createParallelGroup(
                                                GroupLayout.Alignment.TRAILING
                                            )
                                                .addComponent(
                                                    gLCanvas1,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    715, Short.MAX_VALUE.toInt()
                                                )
                                                .addGroup(
                                                    layout.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(labelFPS)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(
                                                            labelCameraPosition
                                                        )
                                                        .addGap(8, 8, 8)
                                                        .addComponent(
                                                            textX,
                                                            GroupLayout.PREFERRED_SIZE,
                                                            44,
                                                            GroupLayout.PREFERRED_SIZE
                                                        )
                                                        .addPreferredGap(
                                                            LayoutStyle.ComponentPlacement.RELATED
                                                        )
                                                        .addComponent(
                                                            textY,
                                                            GroupLayout.PREFERRED_SIZE,
                                                            44,
                                                            GroupLayout.PREFERRED_SIZE
                                                        )
                                                        .addPreferredGap(
                                                            LayoutStyle.ComponentPlacement.RELATED
                                                        )
                                                        .addComponent(
                                                            textZ,
                                                            GroupLayout.PREFERRED_SIZE,
                                                            42,
                                                            GroupLayout.PREFERRED_SIZE
                                                        )
                                                )
                                        )
                                        .addGap(10, 10, 10)
                                )
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(
                            listIDE, GroupLayout.PREFERRED_SIZE, 287,
                            GroupLayout.PREFERRED_SIZE
                        )
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                GroupLayout.Alignment.TRAILING,
                layout.createSequentialGroup()
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(jToggleButton2, 0, 0, Short.MAX_VALUE.toInt())
                            .addComponent(jToggleButton3, 0, 0, Short.MAX_VALUE.toInt())
                            .addComponent(jToggleButton1, GroupLayout.PREFERRED_SIZE, 23, Short.MAX_VALUE.toInt())
                            .addComponent(
                                mapCleanerButton, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt()
                            )
                            .addComponent(
                                jButton2, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt()
                            )
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(
                                listIDE, GroupLayout.Alignment.LEADING,
                                GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE.toInt()
                            )
                            .addComponent(
                                gLCanvas1, GroupLayout.Alignment.LEADING,
                                GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE.toInt()
                            )
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(labelFPS)
                            .addComponent(labelCameraPosition)
                            .addComponent(
                                textZ, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addComponent(
                                textY, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                            .addComponent(
                                textX, GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE
                            )
                    )
            )
        )
        pack()
    } // </editor-fold>//GEN-END:initComponents

    private fun jMenuItem1ActionPerformed(evt: ActionEvent) { // GEN-FIRST:event_jMenuItem1ActionPerformed
        val thanks = arrayOf(
            "Aru ", "DeXx  ", "JostVice", "Johnline", "OinkOink", "Paroxum  ", "REspawn  ",
            "supermortalhuman ", "Tim  ", "", "Everyone I forgot"
        )
        About("Shadow Mapper", "Beta 0.1a", "31-12-2009", thanks)
    } // GEN-LAST:event_jMenuItem1ActionPerformed

    private fun gLCanvas1KeyPressed(evt: KeyEvent) { // GEN-FIRST:event_gLCanvas1KeyPressed
        if (evt.keyCode == KeyEvent.VK_DELETE) {
            when (fm.selType) {
                PickingType.map -> if (fm.selParam1 != -1 && fm.selParam2 != -1) {
                    fm.ipls[fm.selParam1].itemsInst.removeAt(fm.selParam2)
                    fm.updateIPLItemList(fm.selParam1, fm.selParam1)
                }

                else -> println("Nothing to delete")
            }
        }
        glListener.keyPressed(evt)
    } // GEN-LAST:event_gLCanvas1KeyPressed

    private fun gLCanvas1MouseDragged(evt: MouseEvent) { // GEN-FIRST:event_gLCanvas1MouseDragged
        glListener.mouseMoved(evt)
    } // GEN-LAST:event_gLCanvas1MouseDragged

    private fun jButton1ActionPerformed(evt: ActionEvent) { // GEN-FIRST:event_jButton1ActionPerformed
        glListener.reloadMap()
    } // GEN-LAST:event_jButton1ActionPerformed

    private fun gLCanvas1MouseClicked(evt: MouseEvent) { // GEN-FIRST:event_gLCanvas1MouseClicked
        if (evt.clickCount == 2) {
            glListener.setPick()
        }
    } // GEN-LAST:event_gLCanvas1MouseClicked

    private fun gLCanvas1MousePressed(evt: MouseEvent) { // GEN-FIRST:event_gLCanvas1MousePressed
        println("Set pos " + evt.x + ", " + evt.y)
        glListener.setCurrentMousePos(evt.point)
    } // GEN-LAST:event_gLCanvas1MousePressed

    private fun jToggleButton2ItemStateChanged(evt: ItemEvent) { // GEN-FIRST:event_jToggleButton2ItemStateChanged
        glListener.setWireFrame(jToggleButton2.isSelected)
    } // GEN-LAST:event_jToggleButton2ItemStateChanged

    private fun spinnerPosXStateChanged(evt: ChangeEvent) { // GEN-FIRST:event_spinnerPosXStateChanged
        when (fm.selType) {
            PickingType.map -> {
                fm.ipls[fm.selParam1].itemsInst[fm.selParam2].position.x = (spinnerPosX.value as Float)
                fm.ipls[fm.selParam1].changed = true
            }
        }
    } // GEN-LAST:event_spinnerPosXStateChanged

    private fun spinnerPosYStateChanged(evt: ChangeEvent) { // GEN-FIRST:event_spinnerPosYStateChanged
        when (fm.selType) {
            PickingType.map -> {
                fm.ipls[fm.selParam1].itemsInst[fm.selParam2].position.y = (spinnerPosY.value as Float)
                fm.ipls[fm.selParam1].changed = true
            }
        }
    } // GEN-LAST:event_spinnerPosYStateChanged

    private fun spinnerPosZStateChanged(evt: ChangeEvent) { // GEN-FIRST:event_spinnerPosZStateChanged
        when (fm.selType) {
            PickingType.map -> {
                fm.ipls[fm.selParam1].itemsInst[fm.selParam2].position.z = (spinnerPosZ.value as Float)
                fm.ipls[fm.selParam1].changed = true
            }
        }
    } // GEN-LAST:event_spinnerPosZStateChanged

    private fun textXKeyTyped(evt: KeyEvent) { // GEN-FIRST:event_textXKeyTyped
        glListener.camera.pos.x = java.lang.Float.valueOf(textX.text)
    } // GEN-LAST:event_textXKeyTyped

    private fun formWindowClosing(evt: WindowEvent) { // GEN-FIRST:event_formWindowClosing
        SaveScreen(fm, true)
    } // GEN-LAST:event_formWindowClosing

    private fun checkMapItemStateChanged(evt: ItemEvent) { // GEN-FIRST:event_checkMapItemStateChanged
        glListener.displayMap = checkMap.isSelected
    } // GEN-LAST:event_checkMapItemStateChanged

    private fun checkWaterItemStateChanged(evt: ItemEvent) {
        glListener.displayWater = checkWater.isSelected
    } // GEN-LAST:event_checkWaterItemStateChanged

    private fun listIPLValueChanged(evt: ListSelectionEvent) {
        fm.updateIPLItemList(listIPL.selectedIndex, comboIPLType.selectedIndex)
        if (listIPL.selectedIndex != -1) {
            buttonNewIPLItem.isEnabled = true
            comboIPLType.setEnabled(true)
        } else {
            buttonNewIPLItem.isEnabled = false
            comboIPLType.setEnabled(false)
        }
    }

    private fun comboIPLTypeItemStateChanged(evt: ItemEvent) {
        fm.updateIPLItemList(listIPL.selectedIndex, comboIPLType.selectedIndex)
    }

    private fun buttonDeleteIPLItemActionPerformed(evt: ActionEvent) {
        when (comboIPLType.selectedIndex) {
            0 -> fm.ipls[listIPL.selectedIndex].itemsInst.removeAt(
                listIPLItems.selectedIndex
            )

            2 -> fm.ipls[listIPL.selectedIndex].itemsCars.removeAt(listIPLItems.selectedIndex)
        }
        fm.ipls[listIPL.selectedIndex].changed = true
        fm.updateIPLItemList(listIPL.selectedIndex, comboIPLType.selectedIndex)
    }

    private fun jList2ValueChanged(evt: ListSelectionEvent) {
        fm.updateIDEItemList(idesJList.selectedIndex, 0)
        buttonNewIDEItem.isEnabled = true
        buttonDelIDEItem.isEnabled = true
    }

    private fun onResourceBrowserClicked(evt: ActionEvent) {
        val browser = Browser(fm)
    }

    private fun onHashishGenClicked(evt: ActionEvent) {
        HashishGen()
    }

    private fun listIPLItemsMouseClicked(evt: MouseEvent) {
        if (evt.clickCount == 2) {
            when (comboIPLType.selectedIndex) {
                0 -> fm.setSelection(PickingType.map, listIPL.selectedIndex, listIPLItems.selectedIndex)
                2 -> fm.setSelection(PickingType.car, listIPL.selectedIndex, listIPLItems.selectedIndex)
            }
            selectionChanged()
        }
    }

    private fun listIDEItemsMouseClicked(evt: MouseEvent) {
        if (evt.clickCount == 2) {
            IDEForm(idesJList.selectedIndex, listIDEItems.selectedIndex, fm)
        }
    }

    private fun buttonEditIDEItemActionPerformed(evt: ActionEvent) {
        IDEForm(idesJList.selectedIndex, listIDEItems.selectedIndex, fm)
    }

    private fun listIDEItemsValueChanged(evt: ListSelectionEvent) {
        buttonEditIDEItem.isEnabled = listIDEItems.selectedIndex != -1
    }

    private fun buttonNewIDEItemActionPerformed(evt: ActionEvent) {
        IDEForm(idesJList.selectedIndex, fm)
    }

    private fun listSceneMouseClicked(evt: MouseEvent) {
        if (evt.clickCount == 2) {
            val instance = fm.ipls[listScene.selectedIndex].itemsInst[0]
            glListener.camera.pos.x = instance.position.x
            glListener.camera.pos.z = 0 - instance.position.y
            glListener.camera.pos.y = instance.position.z
            glListener.camera.view.x = instance.position.x
            glListener.camera.view.z = 0 - instance.position.y + 5.0f
            glListener.camera.view.y = instance.position.z - 0.5f
            glListener.camera.up.x = 0f
            glListener.camera.up.z = 0f
            glListener.camera.up.y = 1f
        }
    }

    private fun buttonNewIPLItemActionPerformed(evt: ActionEvent) {
        when (comboIPLType.selectedIndex) {
            0 -> IPLForm(fm, listIPL.selectedIndex, glListener.camera.view)
            2 -> CarForm(fm, listIPL.selectedIndex, glListener.camera.view)
        }
    }

    private fun buttonDelIDEItemActionPerformed(evt: ActionEvent) {
        fm.ides[idesJList.selectedIndex].itemObjs.removeAt(listIDEItems.selectedIndex)
        fm.ides[idesJList.selectedIndex].setSaveRequired()
        fm.updateIDEItemList(idesJList.selectedIndex, 0)
    }

    private fun onAddIdeClicked(evt: ActionEvent) {
        val file = openFileChooser(this, ExtensionFilter(setOf("ide"), "IDE File"), null)
        fm.addNewIDE(file)
    }

    private fun onCloseClicked(evt: ActionEvent) {
        dispose()
    }

    private fun onAddIplClicked(evt: ActionEvent) {
        val file = openFileChooser(this, ExtensionFilter(setOf("wpl"), "WPL File"), null)
        fm.addNewIPL(file)
    }

    private fun onSaveClicked(evt: ActionEvent) {
        SaveScreen(fm, false)
    }

    private fun checkCarsActionPerformed(evt: ActionEvent) {
        glListener.displayCars = checkCars.isSelected
    }

    private fun listIPLItemsValueChanged(evt: ListSelectionEvent) {
        if (listIPLItems.selectedIndex != -1) {
            buttonEditIPLItem.isEnabled = true
            buttonDeleteIPLItem.isEnabled = true
        } else {
            buttonEditIPLItem.isEnabled = false
            buttonDeleteIPLItem.isEnabled = false
        }
    }

    private fun spinnerRotXStateChanged(evt: ChangeEvent) {
        when (fm.selType) {
            PickingType.map -> {
                fm.ipls[fm.selParam1].itemsInst[fm.selParam2].axisAngle.x = (spinnerRotX.value as Float)
                fm.ipls[fm.selParam1].changed = true
            }
        }
    }

    private fun spinnerRotYStateChanged(evt: ChangeEvent) {
        when (fm.selType) {
            PickingType.map -> {
                fm.ipls[fm.selParam1].itemsInst[fm.selParam2].axisAngle.y = (spinnerRotY.value as Float)
                fm.ipls[fm.selParam1].changed = true
            }
        }
    }

    private fun spinnerRotZStateChanged(evt: ChangeEvent) {
        when (fm.selType) {
            PickingType.map -> {
                fm.ipls[fm.selParam1].itemsInst[fm.selParam2].axisAngle.z = (spinnerRotZ.value as Float)
                fm.ipls[fm.selParam1].changed = true
            }
        }
    }

    private fun onOpenResourceBrowserClicked(evt: ActionEvent) {
        Browser(fm)
    }

    fun selectionChanged() {
        when (fm.selType) {
            PickingType.map -> {
                val instance = fm.ipls[fm.selParam1].itemsInst[fm.selParam2]
                textModelName.text = instance.name
                spinnerPosX.value = instance.position.x
                spinnerPosY.value = instance.position.y
                spinnerPosZ.value = instance.position.z
                spinnerRotX.value = instance.axisAngle.x
                spinnerRotY.value = instance.axisAngle.y
                spinnerRotZ.value = instance.axisAngle.z
                listIPL.selectedIndex = fm.selParam1
                listIPLItems.setSelectedIndex(fm.selParam2)
            }

            PickingType.water -> textModelName.text = "Water"
            PickingType.car -> {
                val car = fm.ipls[fm.selParam1].itemsCars[fm.selParam2]
                textModelName.text = car.name
                spinnerPosX.value = car.position.x
                spinnerPosY.value = car.position.y
                spinnerPosZ.value = car.position.z
                spinnerRotX.value = car.rotation.x
                spinnerRotY.value = car.rotation.y
                spinnerRotZ.value = car.rotation.z
            }

            else -> textModelName.text = "Unknown"
        }
    }
}