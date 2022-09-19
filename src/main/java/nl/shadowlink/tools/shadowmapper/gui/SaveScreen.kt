/*
 * SaveScreen.java
 *
 * Created on 17-nov-2009, 13:33:14
 */
package nl.shadowlink.tools.shadowmapper.gui

import nl.shadowlink.tools.shadowmapper.FileManager
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import java.awt.Toolkit
import java.awt.event.ActionEvent
import javax.swing.*
import kotlin.system.exitProcess

/**
 * @author Shadow-Link
 */
class SaveScreen(
    private val fm: FileManager,
    private val close: Boolean
) : JFrame() {

    /**
     * Creates new form SaveScreen
     */
    init {
        if (fm.saveModel.isEmpty && close) exitProcess(0)

        this.iconImage = Toolkit.getDefaultToolkit().createImage("icon.png")
        initComponents()
        this.isVisible = true
        this.centerWindow()
    }

    private fun initComponents() {
        val jScrollPane1 = JScrollPane()
        val jList1: JList<String> = JList<String>()
        val jLabel1 = JLabel()
        val saveButton = JButton()
        val noButton = JButton()
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = "Save"
        jList1.setModel(fm.saveModel)
        jScrollPane1.setViewportView(jList1)
        jLabel1.text = "The following files has been changed, do you want to save them?"
        saveButton.text = "Save"
        saveButton.addActionListener { evt: ActionEvent -> onSaveButtonClicked(evt) }
        noButton.text = "No"
        noButton.addActionListener { evt: ActionEvent -> onNoButtonClicked(evt) }
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE.toInt())
                                .addGroup(
                                    layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(
                                            layout.createSequentialGroup()
                                                .addComponent(saveButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(
                                                    noButton,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    57,
                                                    GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                        .addComponent(
                                            jScrollPane1,
                                            GroupLayout.PREFERRED_SIZE,
                                            325,
                                            GroupLayout.PREFERRED_SIZE
                                        )
                                )
                        )
                        .addContainerGap()
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(15, 15, 15)
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                            LayoutStyle.ComponentPlacement.RELATED,
                            GroupLayout.DEFAULT_SIZE,
                            Short.MAX_VALUE.toInt()
                        )
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(noButton)
                                .addComponent(saveButton)
                        )
                        .addContainerGap()
                )
        )
        pack()
    }

    private fun onNoButtonClicked(evt: ActionEvent) {
        dispose()
        if (close) System.exit(0)
    }

    private fun onSaveButtonClicked(evt: ActionEvent) {
        fm.save()
        if (close) System.exit(0) else dispose()
    }
}