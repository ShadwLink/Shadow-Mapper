

/*
 * Select.java
 *
 * Created on 14-aug-2009, 18:17:11
 */

package nl.shadowlink.tools.shadowmapper.gui.install;

import kotlin.Unit;
import nl.shadowlink.tools.shadowlib.utils.Filter;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowlib.utils.Utils;
import nl.shadowlink.tools.shadowmapper.gui.Finals;
import nl.shadowlink.tools.shadowmapper.gui.LoadingBar;
import nl.shadowlink.tools.shadowmapper.utils.GuiFunctions;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import static nl.shadowlink.tools.shadowmapper.utils.GuiFunctions.setLookAndFeel;

/**
 * @author Shadow-Link
 */
public class Select extends JFrame {

    private static final String[] SUPPORTED_EXES = {GameType.GTA_IV.getExecutableName().toLowerCase()};

    private final InstallRepository installRepository = new InstallRepository(new IniInstallStorage());
    private Install selectedInstall;

    /**
     * Creates new form Select
     */
    public Select() {
        this.setIconImage(Toolkit.getDefaultToolkit().createImage("icon.png"));
        setLookAndFeel();
        initComponents();
        GuiFunctions.centerWindow$Shadow_Mapper(this);

        installRepository.observeInstalls(installs -> {
            fillGameList(installs);
            return Unit.INSTANCE;
        });
    }

    private void fillGameList(List<Install> installs) {
        listGames.removeAll();
        installs.forEach(install -> listGames.add(install.getName()));
    }

    private void initComponents() {

        listGames = new java.awt.List();
        buttonOK = new javax.swing.JButton();
        buttonAddInstall = new javax.swing.JButton();
        image = new javax.swing.JLabel();
        buttonRemove = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select install");
        setResizable(false);

        listGames.addItemListener(this::listGamesItemStateChanged);

        buttonOK.setText("Select");
        buttonOK.setEnabled(false);
        buttonOK.addActionListener(this::selectInstallButtonPressed);

        buttonAddInstall.setText("Add Install");
        buttonAddInstall.addActionListener(this::addInstallButtonPressed);

        image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/shadowmapper.png")));

        buttonRemove.addActionListener(this::removeInstallButtonPressed);
        buttonRemove.setText("Remove install");
        buttonRemove.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(
                                                        layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        layout.createSequentialGroup()
                                                                                .addComponent(buttonRemove)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(buttonAddInstall)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(buttonOK))
                                                                .addComponent(image))
                                                .addComponent(listGames, javax.swing.GroupLayout.DEFAULT_SIZE, 365,
                                                        Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addComponent(image)
                        .addGap(1, 1, 1)
                        .addComponent(listGames, javax.swing.GroupLayout.PREFERRED_SIZE, 196,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(buttonAddInstall).addComponent(buttonOK).addComponent(buttonRemove))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }

    private void removeInstallButtonPressed(java.awt.event.ActionEvent evt) {
        installRepository.removeInstall(selectedInstall);
        selectedInstall = null;
        buttonOK.setEnabled(false);
        buttonRemove.setEnabled(false);
    }

    private void selectInstallButtonPressed(java.awt.event.ActionEvent evt) {
        if (selectedInstall != null) {
            new LoadingBar(selectedInstall.getPath(), selectedInstall.getGameType());
        }
        this.dispose();
    }

    private void addInstallButtonPressed(java.awt.event.ActionEvent evt) {
        File file = Utils.fileChooser(this, Finals.fileOpen, new Filter(SUPPORTED_EXES, "IV Install Folder (" + GameType.GTA_IV.getExecutableName() + ")",
                true));
        if (file != null && file.exists() && file.isFile()) {
            String installName = JOptionPane.showInputDialog("Set the name of the install");
            installRepository.addInstall(installName, file.getParent() + "\\", GameType.GTA_IV);
        }
    }

    private void listGamesItemStateChanged(java.awt.event.ItemEvent evt) {
        if (listGames.getSelectedIndex() != -1) {
            selectedInstall = installRepository.getInstall(listGames.getSelectedIndex());
            buttonOK.setEnabled(true);
            buttonRemove.setEnabled(true);
        } else {
            buttonOK.setEnabled(false);
            buttonRemove.setEnabled(false);
        }
    }

    private javax.swing.JButton buttonOK;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JButton buttonAddInstall;
    private javax.swing.JLabel image;
    private java.awt.List listGames;
}
