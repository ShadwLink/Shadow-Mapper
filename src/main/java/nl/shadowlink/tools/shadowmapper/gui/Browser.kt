package nl.shadowlink.tools.shadowmapper.gui

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.img.Img
import nl.shadowlink.tools.shadowlib.img.ImgItem
import nl.shadowlink.tools.shadowlib.model.model.Model
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.filechooser.ExtensionFilter
import nl.shadowlink.tools.shadowlib.utils.filechooser.FileChooserUtil.openFileChooser
import nl.shadowlink.tools.shadowmapper.preview.Preview
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import org.netbeans.lib.awtextra.AbsoluteConstraints
import org.netbeans.lib.awtextra.AbsoluteLayout
import java.awt.Toolkit
import java.awt.event.*
import java.util.*
import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.table.DefaultTableModel

/**
 * @author Shadow-Link
 */
class Browser : JFrame {
    private val checkCUT = JCheckBox()
    private val checkWAD = JCheckBox()
    private val checkWBD = JCheckBox()
    private val checkWBN = JCheckBox()
    private val checkWDD = JCheckBox()
    private val checkWDR = JCheckBox()
    private val checkWFT = JCheckBox()
    private val checkWPL = JCheckBox()
    private val checkWTD = JCheckBox()
    private val fileMenu = JMenu()
    private val editMenu = JMenu()
    private val mainMenuBar = JMenuBar()
    private val importMenuItem = JMenuItem()
    private val exportMenuItem = JMenuItem()
    private val replaceMenuItem = JMenuItem()
    private val deleteMenuItem = JMenuItem()
    private val createNewImgMenuItem = JMenuItem()
    private val deleteImgMenuItem = JMenuItem()
    private val closeMenuItem = JMenuItem()
    private val jPanel1 = JPanel()
    private val jScrollPane1 = JScrollPane()
    private val jScrollPane2 = JScrollPane()
    private val jToggleButton1 = JToggleButton()
    private val labelFileCount = JLabel()
    private val labelProps = JLabel()
    private val listImg = JList<String>()
    private val listItems = JTable()
    private val textFilter = JTextField()

    private val modelIMG = DefaultListModel<String>()
    private val modelImgItems: DefaultTableModel = object : DefaultTableModel() {
        override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
            return false
        }
    }
    private var onFileSelectedListener: ((item: ImgItem, img: Img) -> Unit)? = null
    private val fm: FileManager
    private var filterEnabled = false

    constructor(fm: FileManager) {
        this.iconImage = Toolkit.getDefaultToolkit().createImage("icon.png")
        this.centerWindow()
        this.isVisible = true

        this.fm = fm
        initComponents()
        initImgList()
    }

    constructor(
        fm: FileManager,
        onFileSelected: (item: ImgItem, img: Img) -> Unit,
        isModelSelectionMode: Boolean
    ) : this(fm) {
        this.onFileSelectedListener = onFileSelected

        if (isModelSelectionMode) {
            checkWTD.isSelected = false
        } else {
            checkWDR.isSelected = false
            checkWDD.isSelected = false
            checkWFT.isSelected = false
        }
        checkWAD.isSelected = false
        checkWBD.isSelected = false
        checkWBN.isSelected = false
        checkCUT.isSelected = false
        checkWPL.isSelected = false
        disableTypeFilterEditing()
    }

    private fun initImgList() {
        modelIMG.clear()
        for (i in fm.imgs.indices) {
            var split = fm.imgs[i].fileName.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (split.size == 1) {
                split = fm.imgs[i].fileName.split("\\\\".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            modelIMG.addElement(split[split.size - 1])
        }
        listImg.selectedIndex = 0
    }

    private fun initItemTable() {
        while (modelImgItems.rowCount != 0) {
            modelImgItems.removeRow(0)
        }
        for (i in fm.imgs[listImg.selectedIndex].items.indices) {
            val imgItem = fm.imgs[listImg.selectedIndex].items[i]
            if (!filterEnabled || imgItem.name.lowercase(Locale.getDefault()).contains(textFilter.text)) {
                if (imgItem.name.endsWith(".wdr") && checkWDR.isSelected || imgItem.name.endsWith(
                        ".wdd"
                    ) && checkWDD.isSelected || imgItem.name.endsWith(
                        ".wft"
                    ) && checkWFT.isSelected || imgItem.name.endsWith(".wtd") && checkWTD.isSelected || imgItem.name.endsWith(
                        ".wbn"
                    ) && checkWBN.isSelected || imgItem.name.endsWith(".wbd") && checkWBD.isSelected || imgItem.name.endsWith(
                        ".wad"
                    ) && checkWAD.isSelected || imgItem.name.endsWith(
                        ".cut"
                    ) && checkCUT.isSelected || imgItem.name.endsWith(".wpl") && checkWPL.isSelected
                ) {
                    modelImgItems.addRow(
                        arrayOf(
                            imgItem.name,
                            imgItem.size.toString(),
                            imgItem.offset.toString(),
                            imgItem.type.toString()
                        )
                    )
                }
            }
        }
        labelFileCount.text = "File Count: " + fm.imgs[listImg.selectedIndex].totalItemCount
        jToggleButton1.isSelected = fm.imgs[listImg.selectedIndex].encrypted
        labelProps.text = "Props: " + fm.imgs[listImg.selectedIndex].containsProps
    }

    private fun initComponents() {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "Resource Browser"
        isResizable = false
        contentPane.layout = AbsoluteLayout()
        listImg.model = modelIMG
        listImg.addListSelectionListener { evt: ListSelectionEvent -> listIMGValueChanged(evt) }
        jScrollPane1.setViewportView(listImg)
        contentPane.add(jScrollPane1, AbsoluteConstraints(10, 90, 220, 450))
        listItems.model = modelImgItems
        modelImgItems.addColumn("Name")
        modelImgItems.addColumn("Size (bytes)")
        modelImgItems.addColumn("Offset")
        modelImgItems.addColumn("Type")
        listItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        listItems.showHorizontalLines = false
        listItems.showVerticalLines = false
        listItems.tableHeader.reorderingAllowed = false
        listItems.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(evt: MouseEvent) {
                listItemsMouseClicked(evt)
            }
        })
        jScrollPane2.setViewportView(listItems)
        contentPane.add(jScrollPane2, AbsoluteConstraints(240, 20, 560, 520))
        labelFileCount.text = "File Count: 0"
        contentPane.add(labelFileCount, AbsoluteConstraints(120, 560, -1, -1))
        textFilter.text = "Filter"
        textFilter.addFocusListener(object : FocusAdapter() {
            override fun focusGained(evt: FocusEvent) {
                textFilterFocusGained(evt)
            }

            override fun focusLost(evt: FocusEvent) {
                textFilterFocusLost(evt)
            }
        })
        textFilter.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(evt: KeyEvent) {
                textFilterKeyReleased(evt)
            }

            override fun keyTyped(evt: KeyEvent) {
                textFilterKeyTyped(evt)
            }
        })
        contentPane.add(textFilter, AbsoluteConstraints(240, 0, 110, -1))
        jToggleButton1.icon = ImageIcon(javaClass.getResource("/Images/decrypted.png")) // NOI18N
        jToggleButton1.toolTipText = "Encrypt"
        jToggleButton1.border = null
        jToggleButton1.isFocusable = false
        jToggleButton1.selectedIcon = ImageIcon(javaClass.getResource("/Images/encrypted.png")) // NOI18N
        jToggleButton1.addItemListener { evt: ItemEvent -> jToggleButton1ItemStateChanged(evt) }
        contentPane.add(jToggleButton1, AbsoluteConstraints(0, 550, -1, -1))
        labelProps.text = "Props: "
        contentPane.add(labelProps, AbsoluteConstraints(30, 560, -1, -1))
        jPanel1.border = BorderFactory.createTitledBorder("Filter files")
        jPanel1.layout = AbsoluteLayout()
        checkWDR.isSelected = true
        checkWDR.text = "wdr"
        checkWDR.addItemListener { evt: ItemEvent -> checkWDRItemStateChanged(evt) }
        jPanel1.add(checkWDR, AbsoluteConstraints(10, 20, -1, -1))
        checkWDD.isSelected = true
        checkWDD.text = "wdd"
        checkWDD.addItemListener { evt: ItemEvent -> checkWDDItemStateChanged(evt) }
        jPanel1.add(checkWDD, AbsoluteConstraints(10, 40, -1, -1))
        checkWFT.isSelected = true
        checkWFT.text = "wft"
        checkWFT.addItemListener { evt: ItemEvent -> checkWFTItemStateChanged(evt) }
        jPanel1.add(checkWFT, AbsoluteConstraints(10, 60, -1, -1))
        checkWAD.isSelected = true
        checkWAD.text = "wad"
        checkWAD.addItemListener { evt: ItemEvent -> checkWADItemStateChanged(evt) }
        jPanel1.add(checkWAD, AbsoluteConstraints(90, 60, -1, -1))
        checkWTD.isSelected = true
        checkWTD.text = "wtd"
        checkWTD.addItemListener { evt: ItemEvent -> checkWTDItemStateChanged(evt) }
        jPanel1.add(checkWTD, AbsoluteConstraints(160, 60, -1, 20))
        checkWBD.isSelected = true
        checkWBD.text = "wbd"
        checkWBD.addItemListener { evt: ItemEvent -> checkWBDItemStateChanged(evt) }
        jPanel1.add(checkWBD, AbsoluteConstraints(90, 40, -1, -1))
        checkWBN.isSelected = true
        checkWBN.text = "wbn"
        checkWBN.addItemListener { evt: ItemEvent -> checkWBNItemStateChanged(evt) }
        jPanel1.add(checkWBN, AbsoluteConstraints(90, 20, -1, -1))
        checkCUT.isSelected = true
        checkCUT.text = "cut"
        checkCUT.addItemListener { evt: ItemEvent -> checkCUTItemStateChanged(evt) }
        jPanel1.add(checkCUT, AbsoluteConstraints(160, 20, -1, -1))
        checkWPL.isSelected = true
        checkWPL.text = "wpl"
        checkWPL.addItemListener { evt: ItemEvent -> checkWPLItemStateChanged(evt) }
        jPanel1.add(checkWPL, AbsoluteConstraints(160, 40, -1, -1))
        contentPane.add(jPanel1, AbsoluteConstraints(10, 0, 220, 90))
        fileMenu.text = "File"
        createNewImgMenuItem.text = "New IMG"
        createNewImgMenuItem.addActionListener { evt: ActionEvent -> jMenuItem5ActionPerformed(evt) }
        fileMenu.add(createNewImgMenuItem)
        deleteImgMenuItem.text = "Delete IMG"
        fileMenu.add(deleteImgMenuItem)
        closeMenuItem.text = "Close browser"
        closeMenuItem.addActionListener { evt: ActionEvent -> onCloseClicked(evt) }
        fileMenu.add(closeMenuItem)
        mainMenuBar.add(fileMenu)
        editMenu.text = "Edit"
        importMenuItem.text = "Import File"
        importMenuItem.addActionListener { evt: ActionEvent -> onImportFileClicked(evt) }
        editMenu.add(importMenuItem)
        exportMenuItem.text = "Export File"
        exportMenuItem.addActionListener { evt: ActionEvent -> onExportFileClicked(evt) }
        editMenu.add(exportMenuItem)
        replaceMenuItem.text = "Replace File"
        replaceMenuItem.addActionListener { evt: ActionEvent -> onReplaceFileClicked(evt) }
        editMenu.add(replaceMenuItem)
        deleteMenuItem.text = "Delete File"
        deleteMenuItem.addActionListener { evt: ActionEvent -> onDeleteFileClicked(evt) }
        editMenu.add(deleteMenuItem)
        mainMenuBar.add(editMenu)
        jMenuBar = mainMenuBar
        pack()
    }

    private fun listIMGValueChanged(evt: ListSelectionEvent) {
        if (listImg.selectedIndex != -1) initItemTable()
    } //GEN-LAST:event_listIMGValueChanged

    private fun jToggleButton1ItemStateChanged(evt: ItemEvent) {
        fm.imgs[listImg.selectedIndex].encrypted = jToggleButton1.isSelected
    }

    private fun listItemsMouseClicked(evt: MouseEvent) {
        if (evt.clickCount == 2) {
            if (onFileSelectedListener != null) {
                if (listItems.selectedRow != -1 && listImg.selectedIndex != -1) {
                    fm.imgs[listImg.selectedIndex].findItem(
                        listItems.getValueAt(listItems.selectedRow, 0).toString()
                    )?.let { onFileSelectedListener?.invoke(it, fm.imgs[listImg.selectedIndex]) }

                    dispose()
                }
            } else {
                Preview(
                    fm, listImg.selectedIndex, fm.imgs[listImg.selectedIndex].findItemIndex(
                        listItems.getValueAt(listItems.selectedRow, 0).toString()
                    )
                )
            }
        }
    }

    private fun onExportFileClicked(evt: ActionEvent) {
        if (listItems.selectedRow != -1 && listImg.selectedIndex != -1) {
            val item = fm.imgs[listImg.selectedIndex].items[fm.imgs[listImg.selectedIndex].findItemIndex(
                "" + listItems.getValueAt(
                    listItems.selectedRow, 0
                )
            )]
            val name = item.name
            println("You selected " + name + " from img " + fm.imgs[listImg.selectedIndex].fileName)
            val file = openFileChooser(this, ExtensionFilter(supportedExtensions, "GTA File")) ?: return
            try {
                val rf = ReadFunctions(fm.imgs[listImg.selectedIndex].fileName)
                rf.seek(item.offset)
                val wf = WriteFunctions(file.path)
                val newFile = rf.readArray(item.size)
                wf.write(newFile)
                wf.closeFile()
                rf.closeFile()
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(this, "Unable to read IMG archive")
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select file to export")
        }
    }

    private fun onImportFileClicked(evt: ActionEvent) {
        if (listImg.selectedIndex != -1) {
            val file = openFileChooser(this, ExtensionFilter(supportedExtensions, "GTA File"))
            if (file != null) {
                if (file.extension.equals("dff", true)) {
                    val mdl = Model()
                    mdl.loadDFF(file.absolutePath)
                    println("Started dff conversion")
                    fm.imgs[listImg.selectedIndex].addItem(mdl, file.name)
                } else if (file.extension.equals("txd", true)) {
                    val txd = TextureDic(file.absolutePath, GameType.GTA_IV)
                    println("Started txd conversion")
                    fm.imgs[listImg.selectedIndex].addItem(txd, file.name)
                } else {
                    fm.imgs[listImg.selectedIndex].addItem(file)
                }
                initItemTable()
            }
        }
    } //GEN-LAST:event_jMenuItem1ActionPerformed

    private fun onCloseClicked(evt: ActionEvent) { //GEN-FIRST:event_jMenuItem7ActionPerformed
        dispose()
    } //GEN-LAST:event_jMenuItem7ActionPerformed

    private fun jMenuItem5ActionPerformed(evt: ActionEvent) { //GEN-FIRST:event_jMenuItem5ActionPerformed
        val file = openFileChooser(this, ExtensionFilter(setOf("img"), "GTA IMG File"))
        if (file != null && !file.exists()) {
            fm.addIMG(file.absolutePath)
            initImgList()
        }
    } //GEN-LAST:event_jMenuItem5ActionPerformed

    private fun disableTypeFilterEditing() {
        checkWDR.isEnabled = false
        checkWDD.isEnabled = false
        checkWFT.isEnabled = false
        checkWTD.isEnabled = false
        checkWAD.isEnabled = false
        checkWBN.isEnabled = false
        checkWBD.isEnabled = false
        checkCUT.isEnabled = false
        checkWPL.isEnabled = false
    }

    private fun onDeleteFileClicked(evt: ActionEvent) { //GEN-FIRST:event_jMenuItem4ActionPerformed
        if (listImg.selectedIndex != -1 && listItems.selectedRow != -1) {
            fm.imgs[listImg.selectedIndex].items.removeAt(listItems.selectedRow)
            initItemTable()
            fm.imgs[listImg.selectedIndex].isChanged = true
        }
    } //GEN-LAST:event_jMenuItem4ActionPerformed

    private fun textFilterKeyTyped(evt: KeyEvent) { //GEN-FIRST:event_textFilterKeyTyped
        /*System.out.println("Filter started " + textFilter.getText());
        evt.getKeyChar();
        initItemTable();*/
    } //GEN-LAST:event_textFilterKeyTyped

    private fun textFilterFocusGained(evt: FocusEvent) { //GEN-FIRST:event_textFilterFocusGained
        if (!filterEnabled) textFilter.text = ""
        filterEnabled = true
    } //GEN-LAST:event_textFilterFocusGained

    private fun textFilterFocusLost(evt: FocusEvent) { //GEN-FIRST:event_textFilterFocusLost
        if (textFilter.text.equals("", ignoreCase = true)) {
            textFilter.text = "Filter"
            filterEnabled = false
        }
    } //GEN-LAST:event_textFilterFocusLost

    private fun textFilterKeyReleased(evt: KeyEvent) { //GEN-FIRST:event_textFilterKeyReleased
        println("Filter started " + textFilter.text)
        println(evt.keyChar)
        initItemTable()
    } //GEN-LAST:event_textFilterKeyReleased

    private fun checkWDRItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkWDRItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkWDRItemStateChanged

    private fun checkWDDItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkWDDItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkWDDItemStateChanged

    private fun checkWFTItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkWFTItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkWFTItemStateChanged

    private fun checkWADItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkWADItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkWADItemStateChanged

    private fun checkWTDItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkWTDItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkWTDItemStateChanged

    private fun checkWBNItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkWBNItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkWBNItemStateChanged

    private fun checkWBDItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkWBDItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkWBDItemStateChanged

    private fun checkCUTItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkCUTItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkCUTItemStateChanged

    private fun checkWPLItemStateChanged(evt: ItemEvent) { //GEN-FIRST:event_checkWPLItemStateChanged
        initItemTable()
    } //GEN-LAST:event_checkWPLItemStateChanged

    private fun onReplaceFileClicked(evt: ActionEvent) { //GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_jMenuItem3ActionPerformed

    companion object {
        private val supportedExtensions = setOf("wdr", "wtd", "wbd", "wbn", "wdd", "wft", "wpl")
    }
}