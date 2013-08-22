package shadowmapper.select;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
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
		populateInstallsTable(mInstalls);
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 106, 414, 110);
		frame.getContentPane().add(scrollPane);

		table = new JTable(new InstallsTableModel());
		table.setBounds(10, 85, 414, 131);
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);
		table.getColumnModel().getColumn(4).setMinWidth(44);
		scrollPane.setViewportView(table);

		BufferedImage shadowmapperImage = null;
		try {
			shadowmapperImage = ImageIO.read(this.getClass().getResource("/images/shadowmapper.png"));
		} catch (IOException e) {
			System.out.println("Exception while loading image");
			e.printStackTrace();
		}

		JLabel label = new JLabel(new ImageIcon(shadowmapperImage));
		label.setBounds(10, 11, 414, 84);
		frame.getContentPane().add(label);
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
	 * Load installs from ini
	 * 
	 * @return Array of installs
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
	 * Populate the installs table with the installs data
	 * 
	 * @param installs
	 *            An array of installs
	 */
	private void populateInstallsTable(Install[] installs) {
		((InstallsTableModel) table.getModel()).setInstalls(installs);
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
