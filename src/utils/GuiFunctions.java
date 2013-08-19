package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import shadowmapper.Main;

public class GuiFunctions {

	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			System.out.println("Can't find system LookAndFeel\nSetting LookAndFeel to crossplatform");
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex1) {
				System.out.println("Unable to set the LookAndFeel");
			}
		}
	}
}
