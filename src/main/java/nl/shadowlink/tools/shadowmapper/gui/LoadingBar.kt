package nl.shadowlink.tools.shadowmapper.gui

import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import java.awt.Toolkit
import javax.swing.*

/**
 * @author Shadow-Link
 */
internal class LoadingBar : JFrame(), LoadingStatusCallbacks {

    private var loadingBar = JProgressBar()
    private var loadingLabel = JLabel()

    /**
     * Creates new form LoadingBar
     */
    init {
        iconImage = Toolkit.getDefaultToolkit().createImage("icon.png")
        initComponents()
        isVisible = true
        centerWindow()
    }

    override fun onStartLoading(fileCount: Int) {
        loadingBar.value = 0
        loadingBar.minimum = 0
        loadingBar.maximum = fileCount
    }

    override fun onStartLoadingWpl(wplCount: Int) {
        loadingBar.value = 0
        loadingBar.minimum = 0
        loadingBar.maximum = wplCount
    }

    override fun onLoadingStatusChanged(status: String) {
        loadingLabel.text = status
    }

    override fun onLoadingValueIncreased() {
        loadingBar.value = loadingBar.value + 1
    }

    override fun onLoadingFinished() {
        dispose()
    }

    private fun initComponents() {
        loadingLabel = JLabel()
        loadingBar = JProgressBar()
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        title = "Loading"
        isResizable = false
        loadingLabel.text = "Loading: "
        loadingBar.isStringPainted = true

        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(loadingBar, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
                                .addComponent(loadingLabel)
                        )
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                )
        )
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(loadingLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(
                            loadingBar,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                )
        )
        pack()
    }
}