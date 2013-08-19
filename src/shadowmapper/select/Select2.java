package shadowmapper.select;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import utils.GuiFunctions;
import ch.ubique.inieditor.IniEditor;

public class Select2 {
	private static final String INI_SETTINGS = "settings.ini";
	private static final String INI_SECTION_INSTALLS = "installs";
	private static final String INI_SECTION_VERSIONS = "versions";
	private static final String INI_OPTION_INSTALL_LOCATION = "loc";
	private static final String INI_OPTION_INSTALL_NAME = "name";
	private static final String INI_OPTION_INSTALL_TYPE = "type";

	private JFrame frame;
	private JTable table;

	private IniEditor mIniEditorSettings;
	private Install[] mInstalls;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Select2 window = new Select2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Select2() {
		GuiFunctions.setLookAndFeel();
		initialize();
		initializeIni();
		mInstalls = loadInstalls();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectClicked();
			}
		});
		btnSelect.setBounds(335, 227, 89, 23);
		btnSelect.setEnabled(false);
		frame.getContentPane().add(btnSelect);

		JButton btnAddInstall = new JButton("Add install");
		btnAddInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				addInstallClicked();
			}
		});
		btnAddInstall.setBounds(236, 227, 89, 23);
		frame.getContentPane().add(btnAddInstall);

		JButton btnRemoveInstall = new JButton("Remove install");
		btnRemoveInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeInstallClicked();
			}
		});
		btnRemoveInstall.setBounds(125, 227, 101, 23);
		btnRemoveInstall.setEnabled(false);
		frame.getContentPane().add(btnRemoveInstall);

		table = new JTable();
		table.setBounds(10, 85, 414, 131);
		frame.getContentPane().add(table);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 414, 63);
		frame.getContentPane().add(panel);
	}

	/**
	 * Initialize the ini file
	 */
	private void initializeIni() {
		mIniEditorSettings = new IniEditor();
		try {
			mIniEditorSettings.load(INI_SETTINGS);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "settings.ini missing or corrupted, please redownload shadow mapper");
		}
	}

	/**
	 * Load install from ini
	 */
	private Install[] loadInstalls() {
		// Check if the install section even exists
		if (mIniEditorSettings.hasSection(INI_SECTION_INSTALLS)) {
			int installIndex = 1;

			// Check if the install exists
			String optionInstallLocation = INI_OPTION_INSTALL_LOCATION + installIndex;
			String optionInstallName = INI_OPTION_INSTALL_NAME + installIndex;
			String optionInstallType = INI_OPTION_INSTALL_TYPE + installIndex;
			while (mIniEditorSettings.hasOption(INI_SECTION_INSTALLS, optionInstallLocation)) {
				Install install = new Install();
				install.setPath(mIniEditorSettings.get(INI_SECTION_INSTALLS, optionInstallLocation));
				install.setName(mIniEditorSettings.get(INI_SECTION_INSTALLS, optionInstallName));
				install.setType(Integer.valueOf(mIniEditorSettings.get(INI_SECTION_INSTALLS, optionInstallType)));

				installIndex++;
				optionInstallLocation = INI_OPTION_INSTALL_LOCATION + installIndex;
			}
		}

		// TODO Return array of installs
		return null;
	}

	/**
	 * Clicked on the add install button
	 */
	private void addInstallClicked() {
		System.out.println("Add install clicked");
	}

	/**
	 * Clicked on the remove install button
	 */
	private void removeInstallClicked() {
		System.out.println("Remove install clicked");
	}

	/**
	 * Clicked on the select button
	 */
	private void selectClicked() {
		System.out.println("Select clicked");
	}
}
