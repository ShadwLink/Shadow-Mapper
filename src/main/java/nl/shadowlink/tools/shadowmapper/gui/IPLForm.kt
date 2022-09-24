/*
 * IPLForm.java
 *
 * Created on 29-okt-2009, 22:47:49
 */
package nl.shadowlink.tools.shadowmapper.gui

import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.shadowmapper.FileManager
import java.awt.Toolkit
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.event.ListSelectionEvent

/**
 * @author Shadow-Link
 */
class IPLForm(private val fm: FileManager, private val iplID: Int, private val pos: Vector3D) : JFrame() {
    private val modelIdeItems = DefaultListModel<String>()

    private val cancelButton = JButton()
    private val selectButton = JButton()
    private val jLabel1 = JLabel()
    private val jLabel2 = JLabel()
    private val ideJList = JList<String>()
    private val iplJList = JList<String>()
    private val jScrollPane1 = JScrollPane()
    private val jScrollPane2 = JScrollPane()
    private val jTextField1 = JTextField()

    /**
     * Creates new form IPLForm
     */
    init {
        this.iconImage = Toolkit.getDefaultToolkit().createImage("icon.png")
        initComponents()
        this.isVisible = true
    }

    private fun initComponents() {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "Select IDE"
        isResizable = false
        ideJList.model = fm.modelIDE
        ideJList.addListSelectionListener { evt: ListSelectionEvent -> onIdeSelectionChanged(evt) }
        jScrollPane1.setViewportView(ideJList)
        jLabel1.text = "Select the item you wish to add:"
        jLabel2.text = "Search: "
        selectButton.text = "Select"
        selectButton.isEnabled = false
        selectButton.addActionListener { evt: ActionEvent -> onSelectButtonClicked(evt) }
        cancelButton.text = "Cancel"
        cancelButton.addActionListener { evt: ActionEvent -> onCancelButtonClicked(evt) }
        iplJList.model = modelIdeItems
        iplJList.addListSelectionListener { evt: ListSelectionEvent -> onIplItemSelected(evt) }
        jScrollPane2.setViewportView(iplJList)
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(
                                                    layout.createSequentialGroup()
                                                        .addComponent(jLabel2)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jTextField1)
                                                )
                                                .addComponent(
                                                    jScrollPane1,
                                                    GroupLayout.Alignment.LEADING,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    192,
                                                    GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                    GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addComponent(cancelButton)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(selectButton)
                                                )
                                                .addComponent(
                                                    jScrollPane2,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    201,
                                                    GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                )
                        )
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE.toInt())
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(
                                    jTextField1,
                                    GroupLayout.PREFERRED_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(selectButton)
                                .addComponent(cancelButton)
                        )
                        .addContainerGap(11, Short.MAX_VALUE.toInt())
                )
        )
        pack()
    }

    private fun onIdeSelectionChanged(evt: ListSelectionEvent) {
        modelIdeItems.clear()
        for (i in fm.ides[ideJList.selectedIndex].itemObjs.indices) {
            modelIdeItems.addElement(fm.ides[ideJList.selectedIndex].itemObjs[i].modelName)
        }
    }

    private fun onCancelButtonClicked(evt: ActionEvent) {
        dispose()
    }

    private fun onSelectButtonClicked(evt: ActionEvent) {
        fm.addIPLItem(iplJList.selectedValue.toString(), iplID, pos)
        dispose()
    }

    private fun onIplItemSelected(evt: ListSelectionEvent) {
        selectButton.isEnabled = iplJList.selectedIndex != -1
    }
}