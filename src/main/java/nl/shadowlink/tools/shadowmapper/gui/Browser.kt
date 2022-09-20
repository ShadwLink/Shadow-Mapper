package nl.shadowlink.tools.shadowmapper.gui

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.img.Img
import nl.shadowlink.tools.shadowlib.img.ImgItem
import nl.shadowlink.tools.shadowlib.utils.filechooser.ExtensionFilter
import nl.shadowlink.tools.shadowlib.utils.filechooser.FileChooserUtil.openFileChooser
import nl.shadowlink.tools.shadowmapper.CommandResult
import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.preview.Preview
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.showError
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
    private val deleteMenuItem = JMenuItem()
    private val createNewImgMenuItem = JMenuItem()
    private val deleteImgMenuItem = JMenuItem()
    private val closeMenuItem = JMenuItem()
    private val jPanel1 = JPanel()
    private val jScrollPane1 = JScrollPane()
    private val jScrollPane2 = JScrollPane()
    private val isEncryptedToggleButton = JToggleButton()
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
        this.isVisible = true
        this.fm = fm
        initComponents()
        centerWindow()
        initImgList()
    }

    private constructor(
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

    private fun updateItemTable() {
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
        isEncryptedToggleButton.isSelected = fm.imgs[listImg.selectedIndex].isEncrypted
    }

    private fun initComponents() {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "Resource Browser"
        isResizable = false
        contentPane.layout = AbsoluteLayout()
        listImg.model = modelIMG
        listImg.addListSelectionListener { evt: ListSelectionEvent -> listImgValueChanged(evt) }
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
                // Do nothing
            }
        })
        contentPane.add(textFilter, AbsoluteConstraints(240, 0, 110, -1))
        isEncryptedToggleButton.icon = ImageIcon(javaClass.getResource("/Images/decrypted.png")) // NOI18N
        isEncryptedToggleButton.toolTipText = "Encrypt"
        isEncryptedToggleButton.border = null
        isEncryptedToggleButton.isFocusable = false
        isEncryptedToggleButton.selectedIcon = ImageIcon(javaClass.getResource("/Images/encrypted.png")) // NOI18N
        isEncryptedToggleButton.addItemListener { evt: ItemEvent -> onEncryptionToggleStateChanged(evt) }
        contentPane.add(isEncryptedToggleButton, AbsoluteConstraints(0, 550, -1, -1))
        labelProps.text = "Props: "
        contentPane.add(labelProps, AbsoluteConstraints(30, 560, -1, -1))
        jPanel1.border = BorderFactory.createTitledBorder("Filter files")
        jPanel1.layout = AbsoluteLayout()
        checkWDR.isSelected = true
        checkWDR.text = "wdr"
        checkWDR.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkWDR, AbsoluteConstraints(10, 20, -1, -1))
        checkWDD.isSelected = true
        checkWDD.text = "wdd"
        checkWDD.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkWDD, AbsoluteConstraints(10, 40, -1, -1))
        checkWFT.isSelected = true
        checkWFT.text = "wft"
        checkWFT.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkWFT, AbsoluteConstraints(10, 60, -1, -1))
        checkWAD.isSelected = true
        checkWAD.text = "wad"
        checkWAD.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkWAD, AbsoluteConstraints(90, 60, -1, -1))
        checkWTD.isSelected = true
        checkWTD.text = "wtd"
        checkWTD.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkWTD, AbsoluteConstraints(160, 60, -1, 20))
        checkWBD.isSelected = true
        checkWBD.text = "wbd"
        checkWBD.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkWBD, AbsoluteConstraints(90, 40, -1, -1))
        checkWBN.isSelected = true
        checkWBN.text = "wbn"
        checkWBN.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkWBN, AbsoluteConstraints(90, 20, -1, -1))
        checkCUT.isSelected = true
        checkCUT.text = "cut"
        checkCUT.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkCUT, AbsoluteConstraints(160, 20, -1, -1))
        checkWPL.isSelected = true
        checkWPL.text = "wpl"
        checkWPL.addItemListener { evt: ItemEvent -> onExtensionFilterStateChanged(evt) }
        jPanel1.add(checkWPL, AbsoluteConstraints(160, 40, -1, -1))
        contentPane.add(jPanel1, AbsoluteConstraints(10, 0, 220, 90))
        fileMenu.text = "File"
        createNewImgMenuItem.text = "New IMG"
        createNewImgMenuItem.addActionListener { evt: ActionEvent -> onAddImgClicked(evt) }
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
        deleteMenuItem.text = "Delete File"
        deleteMenuItem.addActionListener { evt: ActionEvent -> onDeleteFileClicked(evt) }
        editMenu.add(deleteMenuItem)
        mainMenuBar.add(editMenu)
        jMenuBar = mainMenuBar
        pack()
    }

    private fun listImgValueChanged(evt: ListSelectionEvent) {
        if (listImg.selectedIndex != -1) updateItemTable()
    }

    private fun onEncryptionToggleStateChanged(evt: ItemEvent) {
        getSelectedImg()?.toggleEncryption(isEncryptedToggleButton.isSelected)
    }

    private fun listItemsMouseClicked(evt: MouseEvent) {
        if (evt.clickCount == 2) {
            if (onFileSelectedListener != null) {
                val selectedImgItem = getSelectedImgItem()
                val selectedImg = getSelectedImg()
                if (selectedImgItem != null && selectedImg != null) {
                    onFileSelectedListener?.invoke(selectedImgItem, selectedImg)
                    dispose()
                }
            } else {
                Preview(
                    fm,
                    listImg.selectedIndex,
                    fm.imgs[listImg.selectedIndex].findItemIndex(
                        listItems.getValueAt(listItems.selectedRow, 0).toString()
                    )
                )
            }
        }
    }

    private fun getSelectedImgItem(): ImgItem? {
        return if (listItems.selectedRow != -1 && listImg.selectedIndex != -1) {
            fm.imgs[listImg.selectedIndex].findItem(
                listItems.getValueAt(listItems.selectedRow, 0).toString()
            )
        } else null
    }

    private fun getSelectedImg(): Img? {
        return if (listImg.selectedIndex != -1) {
            fm.imgs[listImg.selectedIndex]
        } else null
    }

    private fun onExportFileClicked(evt: ActionEvent) {
        val selectedImgItem = getSelectedImgItem()
        val selectedImg = getSelectedImg()
        if (selectedImg != null && selectedImgItem != null) {
            val file = openFileChooser(this, ExtensionFilter(supportedExtensions, "GTA File")) ?: return
            try {
                val rf = ReadFunctions(selectedImg.fileName)
                rf.seek(selectedImgItem.offset)
                val wf = WriteFunctions(file.path)
                val newFile = rf.readArray(selectedImgItem.size)
                wf.write(newFile)
                wf.closeFile()
                rf.closeFile()
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(this, "Unable to read IMG archive")
            }
        }
    }

    private fun onImportFileClicked(evt: ActionEvent) {
        val selectedImg = getSelectedImg() ?: return
        val file = openFileChooser(this, ExtensionFilter(supportedExtensions, "GTA File")) ?: return
        selectedImg.addItem(file)
        updateItemTable()
    }

    private fun onCloseClicked(evt: ActionEvent) {
        dispose()
    }

    private fun onAddImgClicked(evt: ActionEvent) {
        val file = openFileChooser(this, ExtensionFilter(setOf("img"), "GTA IMG File"), fm.gamePath.toFile())
        if (file != null) {
            val result = fm.addNewImg(file.toPath())
            if (result is CommandResult.Failed) {
                showError("Failed adding img", result.error)
            } else {
                initImgList()
            }
        }
    }

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

    private fun onDeleteFileClicked(evt: ActionEvent) {
        val selectedImg = getSelectedImg()
        val selectedImgItem = getSelectedImgItem()
        if (selectedImg != null && selectedImgItem != null) {
            selectedImg.removeItem(selectedImgItem)
            updateItemTable()
        }
    }

    private fun textFilterFocusGained(evt: FocusEvent) {
        if (!filterEnabled) textFilter.text = ""
        filterEnabled = true
    }

    private fun textFilterFocusLost(evt: FocusEvent) {
        if (textFilter.text.equals("", ignoreCase = true)) {
            textFilter.text = "Filter"
            textFilter.toolTipText = "Filter"
            filterEnabled = false
        }
    }

    private fun textFilterKeyReleased(evt: KeyEvent) {
        updateItemTable()
    }

    private fun onExtensionFilterStateChanged(evt: ItemEvent) {
        updateItemTable()
    }

    companion object {
        private val supportedExtensions = setOf("wdr", "wtd", "wbd", "wbn", "wdd", "wft", "wpl")

        fun createTxdPickerBrowser(
            fm: FileManager,
            onFileSelected: (item: ImgItem, img: Img) -> Unit,
        ) = Browser(fm, onFileSelected, isModelSelectionMode = false)

        fun createModelPickerBrowser(
            fm: FileManager,
            onFileSelected: (item: ImgItem, img: Img) -> Unit,
        ) = Browser(fm, onFileSelected, isModelSelectionMode = true)
    }
}