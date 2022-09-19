/*
 * HashishGen.java
 *
 * Created on 21-dec-2009, 21:53:12
 */
package nl.shadowlink.tools.shadowmapper.gui

import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.setLookAndFeel
import nl.shadowlink.tools.shadowlib.utils.hashing.OneAtATimeHasher
import java.awt.event.ActionEvent
import javax.swing.*

/**
 * @author Shadow-Link
 */
class HashishGen : JFrame() {

    private var buttonConvert = JButton().apply {
        text = ">>"
        addActionListener { evt: ActionEvent -> onConvertButtonClicked(evt) }
    }
    private var labelModelName = JLabel().apply {
        text = "Model Name:"
    }
    private var labelHash = JLabel().apply {
        text = "Hash:"
    }
    private var textFieldInput = JTextField()
    private var textFieldOutput = JTextField()

    /**
     * Creates new form HashishGen
     */
    init {
        isVisible = true
        setLookAndFeel()
        initComponents()
        centerWindow()
    }

    private fun initComponents() {
        title = "HashishGen"
        defaultCloseOperation = HIDE_ON_CLOSE

        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    textFieldInput,
                                    GroupLayout.PREFERRED_SIZE,
                                    147,
                                    GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(labelModelName)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonConvert)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(labelHash, GroupLayout.Alignment.LEADING)
                                .addComponent(textFieldOutput, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE.toInt())
                        )
                        .addContainerGap()
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelModelName)
                                .addComponent(labelHash)
                        )
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                            textFieldOutput,
                                            GroupLayout.PREFERRED_SIZE,
                                            GroupLayout.DEFAULT_SIZE,
                                            GroupLayout.PREFERRED_SIZE
                                        )
                                        .addContainerGap()
                                )
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(
                                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(
                                                    textFieldInput,
                                                    GroupLayout.PREFERRED_SIZE,
                                                    GroupLayout.DEFAULT_SIZE,
                                                    GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(buttonConvert)
                                        )
                                        .addContainerGap()
                                )
                        )
                )
        )
        pack()
    }

    private fun onConvertButtonClicked(evt: ActionEvent) {
        textFieldOutput.text = OneAtATimeHasher.getHashKey(textFieldInput.text).toString()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            HashishGen().isVisible = true
        }
    }
}