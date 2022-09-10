

/*
 * browser.java
 *
 * Created on 27-okt-2009, 23:43:23
 */

package nl.shadowlink.tools.shadowmapper.gui;

import nl.shadowlink.tools.io.ByteReader;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.WriteFunctions;
import nl.shadowlink.tools.shadowlib.ide.Item_OBJS;
import nl.shadowlink.tools.shadowlib.img.ImgItem;
import nl.shadowlink.tools.shadowlib.model.model.Model;
import nl.shadowlink.tools.shadowlib.model.wdr.DrawableModel;
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic;
import nl.shadowlink.tools.shadowlib.utils.Filter;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowlib.utils.Utils;
import nl.shadowlink.tools.shadowmapper.preview.Preview;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

/**
 * @author Shadow-Link
 */
public class Browser extends javax.swing.JFrame {

    private DefaultListModel modelIMG = new DefaultListModel();
    private DefaultTableModel modelIMGItems = new DefaultTableModel() {
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    };

    private IDEForm ideForm;
    private boolean model;
    public FileManager fm;
    private Item_OBJS ideItem;
    private boolean filterEnabled = false;

    public Browser(FileManager fm) {
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().createImage("icon.png"));
        this.fm = fm;
        initComponents();
        initImgList();
        this.setVisible(true);

        //center the window
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
    }

    public Browser(FileManager fm, IDEForm ideForm, boolean model, boolean models) {
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().createImage("icon.png"));
        this.fm = fm;
        this.ideForm = ideForm;
        this.model = model;
        initComponents();
        initImgList();
        this.setVisible(true);

        //center the window
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        if (models) {
            checkWTD.setSelected(false);
        } else {
            checkWDR.setSelected(false);
            checkWDD.setSelected(false);
            checkWFT.setSelected(false);
        }
        checkWAD.setSelected(false);
        checkWBD.setSelected(false);
        checkWBN.setSelected(false);
        checkCUT.setSelected(false);
        checkWPL.setSelected(false);

        disableTypeFilterEditing();
    }

    public Browser(FileManager fm, Item_OBJS item, boolean models) {
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().createImage("icon.png"));
        this.fm = fm;
        this.ideItem = item;
        initComponents();
        initImgList();
        this.setVisible(true);

        //center the window
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - this.getWidth()) / 2;
        int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);

        if (models) {
            checkWTD.setSelected(false);
        } else {
            checkWDR.setSelected(false);
            checkWDD.setSelected(false);
            checkWFT.setSelected(false);
        }
        checkWAD.setSelected(false);
        checkWBD.setSelected(false);
        checkWBN.setSelected(false);
        checkCUT.setSelected(false);
        checkWPL.setSelected(false);

        disableTypeFilterEditing();
    }

    private void initImgList() {
        modelIMG.clear();
        for (int i = 0; i < fm.imgs.length; i++) {
            String[] split = fm.imgs[i].getFileName().split("/");
            if (split.length == 1) {
                split = fm.imgs[i].getFileName().split("\\\\");
            }
            modelIMG.addElement(split[split.length - 1]);
        }
        listIMG.setSelectedIndex(0);
    }

    private void initItemTable() {
        while (modelIMGItems.getRowCount() != 0) {
            modelIMGItems.removeRow(0);
        }
        for (int i = 0; i < fm.imgs[listIMG.getSelectedIndex()].getItems().size(); i++) {
            ImgItem imgItem = fm.imgs[listIMG.getSelectedIndex()].getItems().get(i);
            if (!filterEnabled) {
                if ((fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wdr") && checkWDR.isSelected()) ||
                        (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wdd") && checkWDD.isSelected()) ||
                        (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wft") && checkWFT.isSelected()) ||
                        (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wtd") && checkWTD.isSelected()) ||
                        (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wbn") && checkWBN.isSelected()) ||
                        (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wbd") && checkWBD.isSelected()) ||
                        (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wad") && checkWAD.isSelected()) ||
                        (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".cut") && checkCUT.isSelected()) ||
                        (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wpl") && checkWPL.isSelected())) {
                    String[] string = new String[4];
                    string[0] = fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName();
                    string[1] = "" + fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getSize();
                    string[2] = "" + fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getOffset();
                    string[3] = "" + fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getType();
                    modelIMGItems.addRow(string);
                }
            } else {
                if (imgItem.getName().toLowerCase().contains(textFilter.getText())) {
                    if ((fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wdr") && checkWDR.isSelected()) ||
                            (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wdd") && checkWDD.isSelected()) ||
                            (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wft") && checkWFT.isSelected()) ||
                            (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wtd") && checkWTD.isSelected()) ||
                            (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wbn") && checkWBN.isSelected()) ||
                            (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wbd") && checkWBD.isSelected()) ||
                            (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wad") && checkWAD.isSelected()) ||
                            (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".cut") && checkCUT.isSelected()) ||
                            (fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName().endsWith(".wpl") && checkWPL.isSelected())) {
                        String[] string = new String[4];
                        string[0] = fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getName();
                        string[1] = "" + fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getSize();
                        string[2] = "" + fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getOffset();
                        string[3] = "" + fm.imgs[listIMG.getSelectedIndex()].getItems().get(i).getType();
                        modelIMGItems.addRow(string);
                    }
                }
            }
        }
        labelFileCount.setText("File Count: " + fm.imgs[listIMG.getSelectedIndex()].getItems().size());
        jToggleButton1.setSelected(fm.imgs[listIMG.getSelectedIndex()].getEncrypted());
        labelProps.setText("Props: " + fm.imgs[listIMG.getSelectedIndex()].getContainsProps());
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listIMG = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        listItems = new javax.swing.JTable();
        labelFileCount = new javax.swing.JLabel();
        textFilter = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        labelProps = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        checkWDR = new javax.swing.JCheckBox();
        checkWDD = new javax.swing.JCheckBox();
        checkWFT = new javax.swing.JCheckBox();
        checkWAD = new javax.swing.JCheckBox();
        checkWTD = new javax.swing.JCheckBox();
        checkWBD = new javax.swing.JCheckBox();
        checkWBN = new javax.swing.JCheckBox();
        checkCUT = new javax.swing.JCheckBox();
        checkWPL = new javax.swing.JCheckBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Resource Browser");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        listIMG.setModel(modelIMG);
        listIMG.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listIMGValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(listIMG);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 220, 450));

        listItems.setModel(modelIMGItems);
        modelIMGItems.addColumn("Name");
        modelIMGItems.addColumn("Size (bytes)");
        modelIMGItems.addColumn("Offset");
        modelIMGItems.addColumn("Type");
        listItems.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        listItems.setShowHorizontalLines(false);
        listItems.setShowVerticalLines(false);
        listItems.getTableHeader().setReorderingAllowed(false);
        listItems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listItemsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(listItems);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 560, 520));

        labelFileCount.setText("File Count: 0");
        getContentPane().add(labelFileCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 560, -1, -1));

        textFilter.setText("Filter");
        textFilter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textFilterFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                textFilterFocusLost(evt);
            }
        });
        textFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textFilterKeyReleased(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                textFilterKeyTyped(evt);
            }
        });
        getContentPane().add(textFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 110, -1));

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/decrypted.png"))); // NOI18N
        jToggleButton1.setToolTipText("Encrypt");
        jToggleButton1.setBorder(null);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/encrypted.png"))); // NOI18N
        jToggleButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButton1ItemStateChanged(evt);
            }
        });
        getContentPane().add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, -1, -1));

        labelProps.setText("Props: ");
        getContentPane().add(labelProps, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 560, -1, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter files"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        checkWDR.setSelected(true);
        checkWDR.setText("wdr");
        checkWDR.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkWDRItemStateChanged(evt);
            }
        });
        jPanel1.add(checkWDR, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        checkWDD.setSelected(true);
        checkWDD.setText("wdd");
        checkWDD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkWDDItemStateChanged(evt);
            }
        });
        jPanel1.add(checkWDD, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        checkWFT.setSelected(true);
        checkWFT.setText("wft");
        checkWFT.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkWFTItemStateChanged(evt);
            }
        });
        jPanel1.add(checkWFT, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        checkWAD.setSelected(true);
        checkWAD.setText("wad");
        checkWAD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkWADItemStateChanged(evt);
            }
        });
        jPanel1.add(checkWAD, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, -1));

        checkWTD.setSelected(true);
        checkWTD.setText("wtd");
        checkWTD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkWTDItemStateChanged(evt);
            }
        });
        jPanel1.add(checkWTD, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, -1, 20));

        checkWBD.setSelected(true);
        checkWBD.setText("wbd");
        checkWBD.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkWBDItemStateChanged(evt);
            }
        });
        jPanel1.add(checkWBD, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, -1, -1));

        checkWBN.setSelected(true);
        checkWBN.setText("wbn");
        checkWBN.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkWBNItemStateChanged(evt);
            }
        });
        jPanel1.add(checkWBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        checkCUT.setSelected(true);
        checkCUT.setText("cut");
        checkCUT.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkCUTItemStateChanged(evt);
            }
        });
        jPanel1.add(checkCUT, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, -1, -1));

        checkWPL.setSelected(true);
        checkWPL.setText("wpl");
        checkWPL.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkWPLItemStateChanged(evt);
            }
        });
        jPanel1.add(checkWPL, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 220, 90));

        jMenu1.setText("File");

        jMenuItem5.setText("New IMG");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setText("Delete IMG");
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Close browser");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem1.setText("Import File");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setText("Export File");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Replace File");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setText("Delete File");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void listIMGValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listIMGValueChanged
        if (listIMG.getSelectedIndex() != -1) initItemTable();
    }//GEN-LAST:event_listIMGValueChanged

    private void jToggleButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButton1ItemStateChanged
        fm.imgs[listIMG.getSelectedIndex()].setEncrypted(jToggleButton1.isSelected());
    }//GEN-LAST:event_jToggleButton1ItemStateChanged

    private void listItemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listItemsMouseClicked
        if (evt.getClickCount() == 2) {
            if (ideForm != null) {
                if (listItems.getSelectedRow() != -1 && listIMG.getSelectedIndex() != -1) {
                    if (ideItem != null) {
                        ImgItem imgitem = fm.imgs[listIMG.getSelectedIndex()].findItem(ideItem.modelName);
                        ReadFunctions rf = new ReadFunctions(fm.imgs[listIMG.getSelectedIndex()].getFileName());
                        rf.seek(imgitem.getOffset());
                        ByteReader br = rf.getByteReader(imgitem.getSize());
                        if (imgitem.getName().endsWith(".wdr")) {

                            DrawableModel sys = new Model().loadWDRSystem(br, imgitem.getSize()); //load the model from img
                            ideItem.boundsMax.x = sys.BoundsMax.x;
                            ideItem.boundsMax.y = sys.BoundsMax.y;
                            ideItem.boundsMax.z = sys.BoundsMax.z;
                            ideItem.boundsMin.x = sys.BoundsMin.x;
                            ideItem.boundsMin.y = sys.BoundsMin.y;
                            ideItem.boundsMin.z = sys.BoundsMin.z;
                            ideItem.boundsSphere.x = sys.Center.x;
                            ideItem.boundsSphere.y = sys.Center.y;
                            ideItem.boundsSphere.z = sys.Center.z;
                            sys = null;
                        }
                        rf.closeFile();
                    }
                    String name = fm.imgs[listIMG.getSelectedIndex()].getItems().get(fm.imgs[listIMG.getSelectedIndex()].findItemIndex("" + listItems.getValueAt(listItems.getSelectedRow(), 0))).getName();
                    name = name.substring(0, name.length() - 4);
                    if (ideForm != null) {
                        if (model)
                            ideForm.setModel(name, listIMG.getSelectedIndex(), fm.imgs[listIMG.getSelectedIndex()].findItemIndex("" + listItems.getValueAt(listItems.getSelectedRow(), 0)));
                        else ideForm.setTexture(name);
                    }
                    System.out.println("You selected " + name + " from img " + fm.imgs[listIMG.getSelectedIndex()].getFileName());
                    this.dispose();
                }
            } else {
                new Preview(fm, listIMG.getSelectedIndex(), fm.imgs[listIMG.getSelectedIndex()].findItemIndex("" + listItems.getValueAt(listItems.getSelectedRow(), 0)));
            }
        }
    }//GEN-LAST:event_listItemsMouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (listItems.getSelectedRow() != -1 && listIMG.getSelectedIndex() != -1) {
            ImgItem item = fm.imgs[listIMG.getSelectedIndex()].getItems().get(fm.imgs[listIMG.getSelectedIndex()].findItemIndex("" + listItems.getValueAt(listItems.getSelectedRow(), 0)));
            String name = item.getName();
            System.out.println("You selected " + name + " from img " + fm.imgs[listIMG.getSelectedIndex()].getFileName());
            String[] extensions = {".wdr", ".wtd", ".wbd", ".wbn", ".wdd", ".wft", ".wpl"};
            File file = Utils.fileChooser(this, Finals.fileSave, new Filter(extensions, "GTA File", false));
            try {
                ReadFunctions rf = new ReadFunctions(fm.imgs[listIMG.getSelectedIndex()].getFileName());
                rf.seek(item.getOffset());
                WriteFunctions wf = new WriteFunctions(file.getPath());
                byte[] newFile = rf.readArray(item.getSize());
                wf.write(newFile);
                wf.closeFile();
                rf.closeFile();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Unable to read IMG archive");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select file to export");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        if (listIMG.getSelectedIndex() != -1) {
            String[] extensions = {".dff", ".wdr", ".wtd", ".wbd", ".wbn", ".wdd", ".wft", ".txd", ".wpl"};
            File file = Utils.fileChooser(this, Finals.fileOpen, new Filter(extensions, "GTA File", false));
            if (file != null) {
                if (file.getName().endsWith(".dff") || file.getName().endsWith(".DFF")) {
                    Model mdl = new Model();
                    mdl.loadDFF(file.getAbsolutePath());
                    System.out.println("Started dff conversion");
                    fm.imgs[listIMG.getSelectedIndex()].addItem(mdl, file.getName());
                    mdl = null;
                } else if (file.getName().endsWith(".txd") || file.getName().endsWith(".TXD")) {
                    TextureDic txd = new TextureDic(file.getAbsolutePath(), GameType.GTA_IV);
                    System.out.println("Started txd conversion");
                    fm.imgs[listIMG.getSelectedIndex()].addItem(txd, file.getName());
                    txd = null;
                } else {
                    fm.imgs[listIMG.getSelectedIndex()].addItem(file);
                }
                initItemTable();
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        String[] extensions = {".img"};
        File file = Utils.fileChooser(this, Finals.fileOpen, new Filter(extensions, "GTA IMG File", false));
        if (file != null && !file.exists()) {
            fm.addIMG(file.getAbsolutePath());
            initImgList();
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void disableTypeFilterEditing() {
        checkWDR.setEnabled(false);
        checkWDD.setEnabled(false);
        checkWFT.setEnabled(false);
        checkWTD.setEnabled(false);
        checkWAD.setEnabled(false);
        checkWBN.setEnabled(false);
        checkWBD.setEnabled(false);
        checkCUT.setEnabled(false);
        checkWPL.setEnabled(false);
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        if (listIMG.getSelectedIndex() != -1 && listItems.getSelectedRow() != -1) {
            fm.imgs[listIMG.getSelectedIndex()].getItems().remove(listItems.getSelectedRow());
            initItemTable();
            fm.imgs[listIMG.getSelectedIndex()].setChanged(true);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void textFilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFilterKeyTyped
        /*System.out.println("Filter started " + textFilter.getText());
        evt.getKeyChar();
        initItemTable();*/
    }//GEN-LAST:event_textFilterKeyTyped

    private void textFilterFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFilterFocusGained
        if (!filterEnabled) textFilter.setText("");
        filterEnabled = true;
    }//GEN-LAST:event_textFilterFocusGained

    private void textFilterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFilterFocusLost
        if (textFilter.getText().equalsIgnoreCase("")) {
            textFilter.setText("Filter");
            filterEnabled = false;
        }
    }//GEN-LAST:event_textFilterFocusLost

    private void textFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFilterKeyReleased
        System.out.println("Filter started " + textFilter.getText());
        System.out.println(evt.getKeyChar());
        initItemTable();
    }//GEN-LAST:event_textFilterKeyReleased

    private void checkWDRItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkWDRItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkWDRItemStateChanged

    private void checkWDDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkWDDItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkWDDItemStateChanged

    private void checkWFTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkWFTItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkWFTItemStateChanged

    private void checkWADItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkWADItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkWADItemStateChanged

    private void checkWTDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkWTDItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkWTDItemStateChanged

    private void checkWBNItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkWBNItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkWBNItemStateChanged

    private void checkWBDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkWBDItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkWBDItemStateChanged

    private void checkCUTItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkCUTItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkCUTItemStateChanged

    private void checkWPLItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkWPLItemStateChanged
        initItemTable();
    }//GEN-LAST:event_checkWPLItemStateChanged

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox checkCUT;
    private javax.swing.JCheckBox checkWAD;
    private javax.swing.JCheckBox checkWBD;
    private javax.swing.JCheckBox checkWBN;
    private javax.swing.JCheckBox checkWDD;
    private javax.swing.JCheckBox checkWDR;
    private javax.swing.JCheckBox checkWFT;
    private javax.swing.JCheckBox checkWPL;
    private javax.swing.JCheckBox checkWTD;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel labelFileCount;
    private javax.swing.JLabel labelProps;
    private javax.swing.JList listIMG;
    private javax.swing.JTable listItems;
    private javax.swing.JTextField textFilter;
    // End of variables declaration//GEN-END:variables

}
