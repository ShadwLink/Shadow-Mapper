package nl.shadowlink.tools.shadowmapper.utils

import nl.shadowlink.tools.shadowmapper.gui.MainForm
import java.awt.Toolkit
import java.awt.Window
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException

object GuiFunctions {
    @JvmStatic
    fun setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (ex: ClassNotFoundException) {
            Logger.getLogger(MainForm::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: InstantiationException) {
            Logger.getLogger(MainForm::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: IllegalAccessException) {
            Logger.getLogger(MainForm::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: UnsupportedLookAndFeelException) {
            println("Can't find system LookAndFeel\nSetting LookAndFeel to crossplatform")
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())
            } catch (ex1: Exception) {
                println("Unable to set the LookAndFeel")
            }
        }
    }

    @JvmStatic
    internal fun Window.centerWindow() {
        val toolkit = Toolkit.getDefaultToolkit()
        val screenSize = toolkit.screenSize
        val x = (screenSize.width - width) / 2
        val y = (screenSize.height - height) / 2
        this.setLocation(x, y)
    }
}
