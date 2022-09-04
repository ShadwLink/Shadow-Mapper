package nl.shadowlink.tools.shadowmapper.gui.select;

import com.nikhaldimann.inieditor.IniEditor;
import nl.shadowlink.tools.shadowlib.utils.Filter;
import nl.shadowlink.tools.shadowlib.utils.Utils;
import nl.shadowlink.tools.shadowmapper.constants.ConstantsSettings;
import nl.shadowlink.tools.shadowmapper.gui.Finals;
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Shows the select form to select the installation the user wants to use
 *
 * @author Shadow-Link
 */
public class FormSelect implements ListSelectionListener {
    private JFrame frame;
    private JTable table;
    private JButton btnAddInstall;
    private JButton btnRemoveInstall;
    private JButton btnSelect;

    private static final String[] mExeNames = {"gtaiv.exe"};

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
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        btnSelect = new JButton("Select");
        btnSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectClicked();
            }
        });
        btnSelect.setBounds(335, 227, 89, 23);
        btnSelect.setEnabled(false);
        frame.getContentPane().add(btnSelect);

        btnAddInstall = new JButton("Add install");
        btnAddInstall.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                addInstallClicked();
            }
        });
        btnAddInstall.setBounds(236, 227, 89, 23);
        frame.getContentPane().add(btnAddInstall);

        btnRemoveInstall = new JButton("Remove install");
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
        table.getSelectionModel().addListSelectionListener(this);
        scrollPane.setViewportView(table);

        BufferedImage shadowmapperImage = null;
        try {
            shadowmapperImage = ImageIO.read(this.getClass().getResource("/images/shadowmapper.png"));
        } catch (IOException e) {
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
     * Save the install to the settings.ini
     *
     * @param install The install to save to the ini
     */
    private void saveInstallToIni(Install install) {
        if (mIniEditorSettings.hasSection(ConstantsSettings.INI_SECTION_INSTALLS)) {
            if (mIniEditorSettings.hasOption(ConstantsSettings.INI_SECTION_INSTALLS,
                    ConstantsSettings.INI_OPTION_INSTALL_COUNT)) {
                int installCount = Integer.valueOf(mIniEditorSettings.get(ConstantsSettings.INI_SECTION_INSTALLS,
                        ConstantsSettings.INI_OPTION_INSTALL_COUNT));

                // Update the install count
                mIniEditorSettings.set(ConstantsSettings.INI_SECTION_INSTALLS,
                        ConstantsSettings.INI_OPTION_INSTALL_COUNT, String.valueOf(installCount));

                // Add the install
                String optionInstallLocation = ConstantsSettings.INI_OPTION_INSTALL_LOCATION + installCount;
                String optionInstallName = ConstantsSettings.INI_OPTION_INSTALL_NAME + installCount;
                String optionInstallType = ConstantsSettings.INI_OPTION_INSTALL_TYPE + installCount;

                mIniEditorSettings
                        .set(ConstantsSettings.INI_SECTION_INSTALLS, optionInstallLocation, install.getPath());
                mIniEditorSettings.set(ConstantsSettings.INI_SECTION_INSTALLS, optionInstallName, install.getName());
                mIniEditorSettings.set(ConstantsSettings.INI_SECTION_INSTALLS, optionInstallType,
                        String.valueOf(install.getType()));

                try {
                    mIniEditorSettings.save(ConstantsSettings.INI_SETTINGS);
                } catch (IOException e) {
                    System.out.println("Exception saving settings.ini " + e.toString());
                }
            }
        }
    }

    /**
     * Populate the installs table with the installs data
     *
     * @param installs An array of installs
     */
    private void populateInstallsTable(Install[] installs) {
        ((InstallsTableModel) table.getModel()).setInstalls(installs);
    }

    /**
     * Clicked on the add install button
     */
    private void addInstallClicked() {
        File file = Utils.fileChooser(null, Finals.fileOpen, new Filter(mExeNames, "gtaiv.exe", true));

        if (file != null) {
            // TODO: Find gametype
            Install install = initializeNewInstallObject(file.getAbsolutePath(), "", 3);
            saveInstallToIni(install);
            addInstallToTable(install);
        }
    }

    /**
     * Initializes a new install object with the given path and gametype
     *
     * @param path     The path to point the install to
     * @param gameType The gametype this install points to
     * @return The install object
     */
    private Install initializeNewInstallObject(String path, String name, int gameType) {
        Install install = new Install();
        install.setPath(path);
        install.setName(name);
        install.setType(gameType);
        install.checkVersion(mIniEditorSettings);

        return install;
    }

    /**
     * Add a new install
     *
     * @param path     The path to the game
     * @param gameType The gametype
     */
    private void addInstallToTable(Install install) {

        // Create a temporary array to hold the installs, make it bigger and add
        // the new install
        Install[] tempInstalls = new Install[mInstalls.length + 1];
        for (int i = 0; i < mInstalls.length; i++) {
            tempInstalls[i] = mInstalls[i];
        }
        tempInstalls[mInstalls.length] = install;
        mInstalls = tempInstalls;

        tempInstalls = null;

        populateInstallsTable(mInstalls);
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

    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        int selectedRow = listSelectionEvent.getFirstIndex();
        if (selectedRow != -1) {
            btnRemoveInstall.setEnabled(true);
            btnSelect.setEnabled(true);
        } else {
            btnRemoveInstall.setEnabled(false);
            btnSelect.setEnabled(false);
        }
    }
}
