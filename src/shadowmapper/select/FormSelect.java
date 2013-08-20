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
import constants.ConstantsSettings;

public class FormSelect {
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
					FormSelect window = new FormSelect();
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
	public FormSelect() {
		GuiFunctions.setLookAndFeel();
		initialize();
		initializeIni();
		mInstalls = loadInstalls();
		for (Install install : mInstalls) {
			System.out.println("Install: " + install.toString());
		}
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
			mIniEditorSettings.load(ConstantsSettings.INI_SETTINGS);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "settings.ini missing or corrupted, please redownload shadow mapper");
		}
	}

	/**
	 * Load install from ini
	 */
	private Install[] loadInstalls() {
		Install[] installs = new Install[0];

		// Check if the install section even exists
		if (mIniEditorSettings.hasSection(ConstantsSettings.INI_SECTION_INSTALLS)) {
			if (mIniEditorSettings.hasOption(ConstantsSettings.INI_SECTION_INSTALLS,
					ConstantsSettings.INI_OPTION_INSTALL_COUNT)) {
				int installCount = Integer.valueOf(mIniEditorSettings.get(ConstantsSettings.INI_SECTION_INSTALLS,
						ConstantsSettings.INI_OPTION_INSTALL_COUNT));

				installs = new Install[installCount];

				String optionInstallLocation = "";
				String optionInstallName = "";
				String optionInstallType = "";
				for (int i = 0; i < installCount; i++) {
					optionInstallLocation = ConstantsSettings.INI_OPTION_INSTALL_LOCATION + i;
					optionInstallName = ConstantsSettings.INI_OPTION_INSTALL_NAME + i;
					optionInstallType = ConstantsSettings.INI_OPTION_INSTALL_TYPE + i;

					if (mIniEditorSettings.hasOption(ConstantsSettings.INI_SECTION_INSTALLS, optionInstallLocation)) {
						Install install = new Install();
						install.setPath(mIniEditorSettings.get(ConstantsSettings.INI_SECTION_INSTALLS,
								optionInstallLocation));
						install.setName(mIniEditorSettings.get(ConstantsSettings.INI_SECTION_INSTALLS,
								optionInstallName));
						install.setType(Integer.valueOf(mIniEditorSettings.get(ConstantsSettings.INI_SECTION_INSTALLS,
								optionInstallType)));

						install.checkVersion(mIniEditorSettings);

						installs[i] = install;
					}
				}
			}
		}

		return installs;
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