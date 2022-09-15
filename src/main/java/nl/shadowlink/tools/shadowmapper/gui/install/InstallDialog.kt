package nl.shadowlink.tools.shadowmapper.gui.install

import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.filechooser.FileChooserUtil.openFileChooser
import nl.shadowlink.tools.shadowlib.utils.filechooser.FileNameFilter
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.setLookAndFeel
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ItemEvent
import javax.swing.*
import kotlin.collections.List
import java.awt.List as AwtList

/**
 * @author Shadow-Link
 */
class InstallDialog(
    onInstallSelectedListener: (install: Install) -> Unit
) : JDialog() {

    private val installRepository = InstallRepository(IniInstallStorage())
    private var selectedInstall: Install? = null

    private var buttonOK = JButton()
    private var buttonRemove = JButton()
    private var buttonAddInstall = JButton()
    private var image = JLabel()
    private var listGames = AwtList()

    /**
     * Creates new form Select
     */
    init {
        setIconImage(Toolkit.getDefaultToolkit().createImage("icon.png"))
        setLookAndFeel()
        initComponents(onInstallSelectedListener)
        this.centerWindow()
        isModal = true
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        modalityType = ModalityType.APPLICATION_MODAL
        installRepository.observeInstalls { installs: List<Install> ->
            fillGameList(installs)
        }
    }

    private fun fillGameList(installs: List<Install>) {
        listGames.removeAll()
        installs.forEach { install ->
            listGames.add(install.name)
        }
    }

    private fun initComponents(onInstallSelectedListener: (install: Install) -> Unit) {
        title = "Select install"
        isResizable = false
        listGames.addItemListener { evt: ItemEvent -> listGamesItemStateChanged(evt) }
        buttonOK.text = "Select"
        buttonOK.isEnabled = false
        buttonOK.addActionListener { e: ActionEvent? -> selectInstallButtonPressed(onInstallSelectedListener) }
        buttonAddInstall.text = "Add Install"
        buttonAddInstall.addActionListener { evt: ActionEvent -> addInstallButtonPressed(evt) }
        image.icon = ImageIcon(javaClass.getResource("/Images/shadowmapper.png"))
        buttonRemove.addActionListener { evt: ActionEvent -> removeInstallButtonPressed(evt) }
        buttonRemove.text = "Remove install"
        buttonRemove.isEnabled = false
        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    layout.createParallelGroup(
                                        GroupLayout.Alignment.LEADING
                                    )
                                        .addGroup(
                                            GroupLayout.Alignment.TRAILING,
                                            layout.createSequentialGroup()
                                                .addComponent(buttonRemove)
                                                .addPreferredGap(
                                                    LayoutStyle.ComponentPlacement.RELATED
                                                )
                                                .addComponent(buttonAddInstall)
                                                .addPreferredGap(
                                                    LayoutStyle.ComponentPlacement.UNRELATED
                                                )
                                                .addComponent(buttonOK)
                                        )
                                        .addComponent(image)
                                )
                                .addComponent(listGames, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE.toInt())
                        ).addContainerGap()
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                    .addComponent(image)
                    .addGap(1, 1, 1)
                    .addComponent(
                        listGames, GroupLayout.PREFERRED_SIZE, 196,
                        GroupLayout.PREFERRED_SIZE
                    )
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonAddInstall).addComponent(buttonOK).addComponent(buttonRemove)
                    )
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
            )
        )
        pack()
    }

    private fun removeInstallButtonPressed(evt: ActionEvent) {
        selectedInstall?.let { installRepository.removeInstall(it) }
        selectedInstall = null
        buttonOK.isEnabled = false
        buttonRemove.isEnabled = false
    }

    private fun selectInstallButtonPressed(onInstallSelectedListener: (install: Install) -> Unit) {
        selectedInstall?.let { onInstallSelectedListener(it) }
        dispose()
    }

    private fun addInstallButtonPressed(evt: ActionEvent) {
        val file = openFileChooser(this, FileNameFilter(SUPPORTED_EXES, "IV Install Folder"))
        if (file != null && file.exists() && file.isFile) {
            val installName = JOptionPane.showInputDialog("Set the name of the install")
            installRepository.addInstall(installName, file.parent + "\\", GameType.GTA_IV)
        }
    }

    private fun listGamesItemStateChanged(evt: ItemEvent) {
        if (listGames.selectedIndex != -1) {
            selectedInstall = installRepository.getInstall(listGames.selectedIndex)
            buttonOK.isEnabled = true
            buttonRemove.isEnabled = true
        } else {
            buttonOK.isEnabled = false
            buttonRemove.isEnabled = false
        }
    }

    companion object {
        private val SUPPORTED_EXES: Set<String> = setOf(GameType.GTA_IV.executableName)
    }
}