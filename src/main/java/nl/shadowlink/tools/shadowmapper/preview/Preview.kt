package nl.shadowlink.tools.shadowmapper.preview

import com.jogamp.opengl.awt.GLCanvas
import com.jogamp.opengl.util.Animator
import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import java.awt.Toolkit
import java.awt.event.*
import java.util.*
import javax.swing.*
import javax.swing.event.ListSelectionEvent

/**
 * @author Shadow-Link
 */
class Preview(
    private val fm: FileManager,
    private val imgID: Int,
    private val itemID: Int
) : JFrame() {
    private val gLCanvas = GLCanvas()
    private val jList1 = JList<String>()
    private val jScrollPane1 = JScrollPane()

    private val animator: Animator
    private val glListener = GlListener()
    private val list = DefaultListModel<String>()

    /**
     * Creates new form Preview
     */
    init {
        this.iconImage = Toolkit.getDefaultToolkit().createImage("icon.png")
        initComponents()
        this.isVisible = true
        this.centerWindow()

        animator = Animator(gLCanvas)
        animator.start()
        glListener.setCanvasPosition(gLCanvas.location)
        init()
    }

    private fun init() {
        val item = fm.imgs[imgID].items[itemID]
        val rf = ReadFunctions(fm.imgs[imgID].path)
        rf.seek(item.offset)
        val itemName = item.name.lowercase(Locale.getDefault())
        when {
            itemName.endsWith(".wdr") -> {
                list.addElement(item.name)
                glListener.type = 0
            }

            itemName.endsWith(".wft") -> {
                list.addElement(item.name)
                glListener.type = 1
            }

            itemName.endsWith(".wtd") -> {
                val br = rf.getByteReader(item.size)
                // TODO: Something changed here, what?
                val txd = TextureDic(itemName, br, GameType.GTA_IV, true, item.size)
                txd.textures.forEach { texture ->
                    list.addElement(texture.diffuseTexName)
                }
                glListener.type = 3
            }

            itemName.endsWith(".wbd") -> {
                val br = rf.getByteReader(item.size)
                rf.seek(item.offset)
                // TODO: What happened to WBD files?
//            WBDFile wbd = new WBDFile(br);
//            for (int i = 0; i < wbd.bounds.hashCount; i++) {
//                list.addElement(wbd.bounds.hashes.Values.get(i) + "(" + wbd.bounds.phBounds._items.get(i).type + ")");
//            }
//            wbd = null;
                glListener.type = 4
            }

            itemName.endsWith(".wbn") -> {
                rf.getByteReader(item.size)
                rf.seek(item.offset)
                glListener.type = 5
            }

            else -> {
                JOptionPane.showMessageDialog(
                    this,
                    "Only WDR, WFT, WBD and WTD files are supported at this moment",
                    "Unable to preview file",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
        glListener.br = rf.getByteReader(item.size)
        glListener.size = item.size
        glListener.load = true
    }

    private fun initComponents() {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "Shadow Mapper - Preview"
        isResizable = false
        gLCanvas.addGLEventListener(glListener)
        gLCanvas.addMouseWheelListener { evt: MouseWheelEvent -> gLCanvas1MouseWheelMoved(evt) }
        gLCanvas.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(evt: MouseEvent) {
                gLCanvas1MousePressed(evt)
            }
        })
        gLCanvas.addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(evt: MouseEvent) {
                gLCanvas1MouseDragged(evt)
            }

            override fun mouseMoved(evt: MouseEvent) {
                gLCanvas1MouseMoved(evt)
            }
        })
        gLCanvas.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(evt: KeyEvent) {
                gLCanvas1KeyPressed(evt)
            }
        })
        jList1.model = list
        jList1.selectionMode = ListSelectionModel.SINGLE_SELECTION
        jList1.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(evt: MouseEvent) {
                jList1MouseClicked(evt)
            }
        })
        jList1.addListSelectionListener { evt: ListSelectionEvent -> jList1ValueChanged(evt) }
        jScrollPane1.setViewportView(jList1)
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(gLCanvas, GroupLayout.PREFERRED_SIZE, 512, GroupLayout.PREFERRED_SIZE)
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE.toInt())
                .addComponent(gLCanvas, GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE.toInt())
        )
        pack()
    }

    private fun gLCanvas1KeyPressed(evt: KeyEvent) {
        glListener.keyPressed(evt)
    }

    private fun gLCanvas1MouseMoved(evt: MouseEvent) {}
    private fun gLCanvas1MouseDragged(evt: MouseEvent) {
        glListener.mouseMoved(evt)
    }

    private fun gLCanvas1MouseWheelMoved(evt: MouseWheelEvent) {
        glListener.mouseWheelMoved(evt)
    }

    private fun gLCanvas1MousePressed(evt: MouseEvent) {
        glListener.mousePressed(evt)
    }

    private fun jList1ValueChanged(evt: ListSelectionEvent) {
        if (jList1.selectedIndex != -1) glListener.setSelected(jList1.selectedIndex)
    }

    private fun jList1MouseClicked(evt: MouseEvent) {
        if (evt.clickCount == 2) {
//            if (glListener.wbd != null) {
//                JOptionPane.showMessageDialog(this, glListener.wbd.bounds.phBounds._items.get(jList1.getSelectedIndex()).phBvh.info);
//            }
        }
    }
}