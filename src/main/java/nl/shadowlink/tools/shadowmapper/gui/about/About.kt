package nl.shadowlink.tools.shadowmapper.gui.about

import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.centerWindow
import org.netbeans.lib.awtextra.AbsoluteConstraints
import org.netbeans.lib.awtextra.AbsoluteLayout
import java.awt.Graphics
import java.awt.Toolkit
import javax.swing.*

/**
 * @author Shadow-Link
 */
class About(
    programName: String,
    version: String,
    releaseDate: String,
    private val thanks: Array<String>
) : JDialog() {

    private var currentHeight = 150
    private var fps = 1f
    private var previousTime: Long = 0
    private var wait = 3f
    private var currentWait = 0f

    private val specialThanksLabel = JLabel()
    private val jLabel2 = JLabel()
    private val websiteLabel = JLabel()
    private val repositoryUrlLabel = JLabel()
    private var jPanel1 = JPanel()
    private val labelDate = JLabel()
    private val labelName = JLabel()
    private val copyrightLabel = JLabel()
    private val labelVersion = JLabel()

    /**
     * Creates new form About
     */
    init {
        setIconImage(Toolkit.getDefaultToolkit().createImage("icon.png"))
        initComponents(programName)
        centerWindow()

        labelName.text = programName
        labelVersion.text = version
        labelDate.text = releaseDate

        isVisible = true
    }

    fun paintPanel(g: Graphics) {
        for (i in thanks.indices) {
            g.drawString(thanks[i], (105 - thanks[i].length * 2.6).toInt(), i * 10 + currentHeight)
        }
        if (currentWait >= wait) {
            if (currentHeight <= thanks.size * -10) {
                currentHeight = 140
            } else {
                currentHeight -= 1
            }
            currentWait = 0f
        } else {
            currentWait += 0.7.toFloat()
        }
        updateFPS()
        this.repaint()
    }

    private fun updateFPS() {
        val currentTime = System.currentTimeMillis()
        ++fps
        if (currentTime - previousTime >= 1000) {
            previousTime = currentTime
            val finalFPS = fps
            wait = finalFPS / 60
            fps = 0f
        }
    }

    private fun initComponents(programName: String) {
        jPanel1 = object : JPanel() {
            override fun paint(g: Graphics) {
                paintPanel(g)
            }
        }
        defaultCloseOperation = DISPOSE_ON_CLOSE
        title = programName
        isResizable = false
        contentPane.layout = AbsoluteLayout()
        val jPanel1Layout = GroupLayout(jPanel1)
        jPanel1.layout = jPanel1Layout
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 220, Short.MAX_VALUE.toInt())
        )
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 140, Short.MAX_VALUE.toInt())
        )
        contentPane.add(jPanel1, AbsoluteConstraints(260, 30, 220, 140))
        specialThanksLabel.text = "Special Thanks"
        contentPane.add(specialThanksLabel, AbsoluteConstraints(330, 10, -1, -1))
        jLabel2.icon = ImageIcon(javaClass.getResource("/Images/logo_about.png"))
        contentPane.add(jLabel2, AbsoluteConstraints(110, 0, 110, 110))
        contentPane.add(labelName, AbsoluteConstraints(10, 10, -1, -1))
        contentPane.add(labelVersion, AbsoluteConstraints(10, 30, -1, -1))
        contentPane.add(labelDate, AbsoluteConstraints(10, 50, -1, -1))
        copyrightLabel.text = "©2009 Shadow-Link"
        contentPane.add(copyrightLabel, AbsoluteConstraints(10, 130, -1, -1))
        repositoryUrlLabel.text = "GitHub: https://github.com/ShadwLink/Shadow-Mapper"
        contentPane.add(repositoryUrlLabel, AbsoluteConstraints(10, 150, -1, -1))
        websiteLabel.text = "Website: https://www.Shadow-Link.nl"
        contentPane.add(websiteLabel, AbsoluteConstraints(10, 170, -1, -1))
        pack()
    }
}