

/*
 * IDEForm.java
 *
 * Created on 29-okt-2009, 22:47:41
 */

package nl.shadowlink.tools.shadowmapper.gui;

import com.jogamp.opengl.awt.GLCanvas;
import kotlin.Unit;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector3D;
import nl.shadowlink.tools.io.Vector4D;
import nl.shadowlink.tools.shadowlib.ide.Item_OBJS;
import nl.shadowlink.tools.shadowlib.img.Img;
import nl.shadowlink.tools.shadowlib.img.ImgItem;
import nl.shadowlink.tools.shadowlib.model.model.Model;
import nl.shadowlink.tools.shadowlib.utils.GameType;

/**
 * @author Shadow-Link
 */
public class IDEForm extends javax.swing.JFrame {
    private int mode = 0;
    private int ideID = 0;
    private int itemID = 0;
    private FileManager fm;

    private Vector3D boundsMin;
    private Vector3D boundsMax;
    private Vector4D boundsSphere;

    /**
     * Creates new form IDEForm
     */
    public IDEForm() {
        this.setIconImage(java.awt.Toolkit.getDefaultToolkit().createImage("icon.png"));
        initComponents();
        this.setVisible(true);
    }

    public IDEForm(int ideID, int itemID, FileManager fm) {
        this.ideID = ideID;
        this.itemID = itemID;
        mode = 1; //edit item
        initComponents();

        Item_OBJS objs = fm.ides.get(ideID).items_objs.get(itemID);
        textModel.setText(objs.modelName);
        textText.setText(objs.textureName);
        textWDD.setText(objs.WDD);
        textDraw.setText(String.valueOf(objs.drawDistance[0]));
        textFlag1.setText(String.valueOf(objs.flag1));
        textFlag2.setText(String.valueOf(objs.flag2));
        labelBoundsMin.setText("Bounds Min: " + objs.boundsMin.x + ", " + objs.boundsMin.y + ", " + objs.boundsMin.z);
        labelBoundsMax.setText("Bounds Max: " + objs.boundsMax.x + ", " + objs.boundsMax.y + ", " + objs.boundsMax.z);
        labelSphere.setText("Sphere: " + objs.boundsSphere.x + ", " + objs.boundsSphere.y + ", " + objs.boundsSphere.z + ", " + objs.boundsSphere.w);
        this.fm = fm;
        this.setVisible(true);
    }

    public IDEForm(int ideID, FileManager fm) {
        mode = 2; //new IDE entry
        initComponents();
        this.ideID = ideID;
        this.fm = fm;
        this.setVisible(true);
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

        gLCanvas1 = new GLCanvas();
        textDraw = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textWDD = new javax.swing.JTextField();
        buttonTexture = new javax.swing.JButton();
        buttonModel = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        textFlag1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        textModel = new javax.swing.JTextField();
        textText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        textFlag2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        labelBoundsMin = new javax.swing.JLabel();
        labelBoundsMax = new javax.swing.JLabel();
        labelSphere = new javax.swing.JLabel();
        buttonOk = new javax.swing.JButton();
        buttonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("IDE Form");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(gLCanvas1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 270, 270));

        textDraw.setText("300");
        getContentPane().add(textDraw, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 60, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/error.png"))); // NOI18N
        jLabel1.setText("Model File: ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/error.png"))); // NOI18N
        jLabel2.setText("Texture File: ");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, -1, -1));

        textWDD.setText("null");
        textWDD.setEnabled(false);
        textWDD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textWDDActionPerformed(evt);
            }
        });
        getContentPane().add(textWDD, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 80, 140, -1));

        buttonTexture.setText("Select");
        buttonTexture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTextureActionPerformed(evt);
            }
        });
        getContentPane().add(buttonTexture, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 50, 70, -1));

        buttonModel.setText("Select");
        buttonModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonModelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 20, 70, -1));

        jLabel3.setText("Draw Distance:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, -1, -1));

        textFlag1.setText("0");
        getContentPane().add(textFlag1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, 60, -1));

        jLabel4.setText("Flag1:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 140, -1, -1));

        jLabel6.setText("Flag2:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, -1, -1));

        textModel.setText(null);
        getContentPane().add(textModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 140, -1));

        textText.setText("null");
        getContentPane().add(textText, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 50, 140, -1));

        jLabel7.setText("WDD:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 80, -1, -1));

        textFlag2.setText("0");
        getContentPane().add(textFlag2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, 60, -1));

        jButton3.setText("Select");
        jButton3.setEnabled(false);
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 80, 70, -1));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Auto Generated"));

        labelBoundsMin.setText("Bounds Min: ");

        labelBoundsMax.setText("Bounds Max: ");

        labelSphere.setText("Sphere: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelBoundsMin)
                                        .addComponent(labelSphere)
                                        .addComponent(labelBoundsMax))
                                .addContainerGap(213, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelBoundsMin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelBoundsMax)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelSphere)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 202, 300, 80));

        buttonOk.setText("Ok");
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });
        getContentPane().add(buttonOk, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 290, -1, -1));

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        getContentPane().add(buttonCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 290, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textWDDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textWDDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textWDDActionPerformed

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        if (mode == 1) {
            saveIDEItem();
            fm.ides.get(ideID).changed = true;
        } else if (mode == 2) {
            Item_OBJS tmpItem = new Item_OBJS(GameType.GTA_IV);
            itemID = fm.addIDEItem(tmpItem, ideID);
            saveIDEItem();
        }
        this.dispose();
    }//GEN-LAST:event_buttonOkActionPerformed

    private void saveIDEItem() {
        Item_OBJS objs = fm.ides.get(ideID).items_objs.get(itemID);
        objs.modelName = textModel.getText();
        objs.textureName = textText.getText();
        objs.WDD = textWDD.getText();
        if (objs.WDD.isEmpty()) objs.WDD = "null";
        objs.drawDistance = new float[1];
        objs.drawDistance[0] = Float.valueOf(textDraw.getText());
        objs.flag1 = Integer.valueOf(textFlag1.getText());
        objs.flag2 = Integer.valueOf(textFlag2.getText());
        objs.boundsMax = boundsMax;
        objs.boundsMin = boundsMin;
        objs.boundsSphere = boundsSphere;
    }

    private void setModel(ImgItem imgItem, Img img) {//}, String model, int imgID, int itemID) {
        textModel.setText(imgItem.getNameWithoutExtension());

        ReadFunctions rf = new ReadFunctions(img.getFileName());
        rf.seek(imgItem.getOffset());
        Model tmpMdl = new Model();
        tmpMdl.loadWDRSystem(rf.getByteReader(imgItem.getSize()), imgItem.getSize());
        rf.closeFile();
        boundsMin = tmpMdl.boundsMin.toVector3D();
        boundsMax = tmpMdl.boundsMax.toVector3D();
        boundsSphere = tmpMdl.center;
        boundsSphere.w = tmpMdl.boundsMax.x;
        labelBoundsMin.setText("Bounds Min: " + boundsMin);
        labelBoundsMax.setText("Bounds Max: " + boundsMax);
        labelSphere.setText("Bounds Sphere: " + boundsSphere);
        tmpMdl.reset();
    }

    private void setTexture(String texture) {
        textText.setText(texture);
    }

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void buttonModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonModelActionPerformed
        new Browser(fm, (imgItem, img) -> {
            setModel(imgItem, img);
            return Unit.INSTANCE;
        }, true);
    }//GEN-LAST:event_buttonModelActionPerformed

    private void buttonTextureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTextureActionPerformed
        new Browser(fm, (imgItem, img) -> {
            setTexture(imgItem.getNameWithoutExtension());
            return Unit.INSTANCE;
        }, false);
    }//GEN-LAST:event_buttonTextureActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonModel;
    private javax.swing.JButton buttonOk;
    private javax.swing.JButton buttonTexture;
    private GLCanvas gLCanvas1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelBoundsMax;
    private javax.swing.JLabel labelBoundsMin;
    private javax.swing.JLabel labelSphere;
    private javax.swing.JTextField textDraw;
    private javax.swing.JTextField textFlag1;
    private javax.swing.JTextField textFlag2;
    private javax.swing.JTextField textModel;
    private javax.swing.JTextField textText;
    private javax.swing.JTextField textWDD;
    // End of variables declaration//GEN-END:variables

}
