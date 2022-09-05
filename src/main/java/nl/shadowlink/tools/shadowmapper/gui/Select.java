

/*
 * Select.java
 *
 * Created on 14-aug-2009, 18:17:11
 */

package nl.shadowlink.tools.shadowmapper.gui;

import com.nikhaldimann.inieditor.IniEditor;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.shadowlib.utils.Constants;
import nl.shadowlink.tools.shadowlib.utils.Filter;
import nl.shadowlink.tools.shadowlib.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class Select extends javax.swing.JFrame {
    private static final String HASHED_KEY = "DEA375EF1E6EF2223A1221C2C575C47BF17EFA5E".toLowerCase();

    private IniEditor versionsIni;
    private IniEditor settingsIni;

    /**
     * Creates new form Select
     */
    public Select() {
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().createImage("icon.png"));
        setLookAndFeel();
        initComponents();

        // center the window
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        versionsIni = new IniEditor();
        try {
            File versionsIni = new File(Select.class.getResource("/versions.ini").toURI().getPath());
            this.versionsIni.load(versionsIni);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Unable to load ini");
            this.dispose();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Load stored settings
        settingsIni = new IniEditor();
        try {
            File settingsFile = new File("settings.ini");
            if (settingsFile.exists()) {
                settingsIni.load("settings.ini");
            } else {
                settingsIni.addSection("installs");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to load stored settings");
        } finally {
            fillGameList();
        }
    }

    private void fillGameList() {
        int id = 1;
        listGames.removeAll();
        while (settingsIni.hasOption("installs", "loc" + id)) {
            listGames.add(settingsIni.get("installs", "name" + id));
            id++;
        }
    }

    private byte[] findKey(String gameDir) {
        ReadFunctions rf = new ReadFunctions();
        rf.openFile(gameDir + "GTAIV.exe");
        byte[] key = new byte[32];
        boolean foundKey = false;
        int cOffset = 1;
        while (!foundKey) {
            String sOffset = versionsIni.get("versions", "offset" + cOffset);
            String[] offsetSplit = sOffset.split("x");
            int offset = Integer.parseInt(offsetSplit[1], 16);
            rf.seek(offset);
            rf.readBytes(key);

            try {
                String keyHash = calulateSHA1sum(key);
                System.out.println("KEY: " + keyHash + " version " + versionsIni.get("versions", "name" + cOffset));

                if (keyHash.equals(HASHED_KEY)) {
                    foundKey = true;
                    System.out.println("Your version is: " + versionsIni.get("versions", "name" + cOffset));
                } else {
                    cOffset++;
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Unable to generate SHA-1 sum");
                throw new RuntimeException(e);
            }
        }
        rf.closeFile();
        return key;
    }

    public static String calulateSHA1sum(byte[] convertme) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArray2Hex(md.digest(convertme));
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public byte[] createHash(String text, String method) {
        try {
            byte[] b = text.getBytes();
            MessageDigest algorithm = MessageDigest.getInstance(method);
            algorithm.reset();
            algorithm.update(b);
            byte messageDigest[] = algorithm.digest();
            return messageDigest;
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }

    public String asHex(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        listGames = new java.awt.List();
        buttonOK = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        buttonRemove = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{{null, null, null, null},
                {null, null, null, null}, {null, null, null, null}, {null, null, null, null}}, new String[]{
                "Title 1", "Title 2", "Title 3", "Title 4"}) {
            boolean[] canEdit = new boolean[]{false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select install");
        setResizable(false);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }

            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });

        listGames.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listGamesItemStateChanged(evt);
            }
        });
        listGames.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listGamesActionPerformed(evt);
            }
        });

        buttonOK.setText("Select");
        buttonOK.setEnabled(false);
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOKActionPerformed(evt);
            }
        });

        jButton1.setText("Add Install");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/shadowmapper.png"))); // NOI18N

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
                                                                                .addComponent(jButton1)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(buttonOK))
                                                                .addComponent(jLabel1))
                                                .addComponent(listGames, javax.swing.GroupLayout.DEFAULT_SIZE, 365,
                                                        Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(1, 1, 1)
                        .addComponent(listGames, javax.swing.GroupLayout.PREFERRED_SIZE, 196,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1).addComponent(buttonOK).addComponent(buttonRemove))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowLostFocus
        this.requestFocus();
    }// GEN-LAST:event_formWindowLostFocus

    private void buttonOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_buttonOKActionPerformed
        int gameType = Integer.valueOf(settingsIni.get("installs", "type" + (listGames.getSelectedIndex() + 1)));
        String gameDir = settingsIni.get("installs", "loc" + (listGames.getSelectedIndex() + 1));
        // check if dir exists and contains the exe
        if (gameType == Finals.gIV) {
            // new Main(gameDir, gameType,
            // findKey(gameDir));//.loadGame(gameDir, gameType,
            // findKey(gameDir));
            new LoadingBar(gameDir, Constants.GameType.GTA_IV, findKey(gameDir));
        } else {
            // new Main(gameDir, gameType, null);//main.loadGame(gameDir,
            // gameType, null);
        }
        this.dispose();
    }// GEN-LAST:event_buttonOKActionPerformed

    private void listGamesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_listGamesActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_listGamesActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        String[] extensions = {"gtaiv.exe"};
        File file = Utils.fileChooser(this, Finals.fileOpen, new Filter(extensions, "IV Install Folder (gtaIV.exe)",
                true));
        if (file != null && file.exists()) {
            int installID = 1;
            while (settingsIni.hasOption("installs", "loc" + installID)) {
                installID++;
            }
            String install = file.getPath().substring(0, file.getPath().length() - 9);
            settingsIni.set("installs", "loc" + installID, install);
            String installName = JOptionPane.showInputDialog("Set the name of the install");
            if (installName != null) {
                settingsIni.set("installs", "name" + installID, installName);
                settingsIni.set("installs", "type" + installID, "3");
                try {
                    settingsIni.save("settings.ini");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Unable to write to ini, it might be in use by another program.");
                }
                fillGameList();
            } else {
                JOptionPane.showMessageDialog(this, "You didn't give a name to the install");
            }
        }
    }// GEN-LAST:event_jButton1ActionPerformed

    private void listGamesItemStateChanged(java.awt.event.ItemEvent evt) {// GEN-FIRST:event_listGamesItemStateChanged
        if (listGames.getSelectedIndex() != -1) {
            buttonOK.setEnabled(true);
            // buttonRemove.setEnabled(true);
        } else {
            buttonOK.setEnabled(false);
            // buttonRemove.setEnabled(false);
        }
    }// GEN-LAST:event_listGamesItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Select().setVisible(true);
            }
        });
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            System.out.println("Can't find system LookAndFeel\nSetting LookAndFeel to crossplatform");
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex1) {
                System.out.println("Unable to set the LookAndFeel");
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonOK;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.awt.List listGames;
    // End of variables declaration//GEN-END:variables

}
