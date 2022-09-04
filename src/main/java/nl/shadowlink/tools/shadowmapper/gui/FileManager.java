package nl.shadowlink.tools.shadowmapper.gui;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector3D;
import nl.shadowlink.tools.shadowlib.dat.GTA_DAT;
import nl.shadowlink.tools.shadowlib.ide.IDE;
import nl.shadowlink.tools.shadowlib.ide.Item_OBJS;
import nl.shadowlink.tools.shadowlib.img.IMG;
import nl.shadowlink.tools.shadowlib.ipl.IPL;
import nl.shadowlink.tools.shadowlib.ipl.Item_INST;
import nl.shadowlink.tools.shadowlib.utils.Constants;
import nl.shadowlink.tools.shadowlib.water.Water;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class FileManager extends Thread {
    private LoadingBar lb;

    public GTA_DAT gta_dat; //object of the gta.dat file
    public IPL[] ipls; //objects of the ipl files
    public IDE[] ides; //objects of the ide files
    public IMG[] imgs; //objects of the img files
    public Water[] waters; //objects of the water.dat files
    public IDE vehicles; //object of the vehicles.ide file

    public DefaultListModel modelIPL = new DefaultListModel(); //contains ipls
    public DefaultListModel modelIDE = new DefaultListModel(); //contains ides
    public DefaultListModel modelIPLItems = new DefaultListModel();
    public DefaultListModel modelIDEItems = new DefaultListModel();
    public DefaultComboBoxModel modelVehicles = new DefaultComboBoxModel();

    public int selType = -1;
    public int selParam1 = -1;
    public int selParam2 = -1;

    private byte[] key; //The encryption key
    private String gameDir;
    private Constants.GameType gameType;

    public FileManager(LoadingBar lb) {
        this.lb = lb;
    }

    public String getGameDir() {
        return gameDir;
    }

    public void setGameDir(String gameDir) {
        this.gameDir = gameDir;
    }

    public Constants.GameType getGameType() {
        return gameType;
    }

    public void setGameType(Constants.GameType gameType) {
        this.gameType = gameType;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public void addIMG(String file) {
        IMG tempIMG = new IMG(file, Constants.GameType.GTA_IV, null, false, true);
        tempIMG.setChanged(true);
        IMG[] tempIMGS = new IMG[imgs.length + 1];
        for (int i = 0; i < imgs.length; i++) {
            tempIMGS[i] = imgs[i];
        }
        tempIMGS[imgs.length] = tempIMG;
        imgs = tempIMGS;
        tempIMGS = null;
        tempIMG = null;
    }

    public void init() {
        gta_dat = new GTA_DAT(gameDir, gameType);
        vehicles = new IDE(gameDir + "common/data/vehicles.ide", 3, true);

        int itemsToLoad = gta_dat.ide.size() + gta_dat.img.size() + gta_dat.water.size() + gta_dat.ipl.size();

        lb.setLoadingBarMax(itemsToLoad);

        ides = new IDE[gta_dat.ide.size()];
        imgs = new IMG[gta_dat.img.size()];
        waters = new Water[gta_dat.water.size()];
        ArrayList<IPL> iplList = new ArrayList();

        //load IDE files from GTA.dat
        for (int i = 0; i < gta_dat.ide.size(); i++) {
            lb.setLabelText("<IDE> " + gta_dat.ide.get(i));
            ides[i] = new IDE(gameDir + gta_dat.ide.get(i), Finals.gIV, true);
            modelIDE.addElement(gta_dat.ide.get(i));
            lb.addOneToLoadingBar();
        }

        //load IMG files from GTA.dat
        for (int i = 0; i < gta_dat.img.size(); i++) {
            lb.setLabelText("<IMG> " + gta_dat.img.get(i));
            String line = gameDir + gta_dat.img.get(i);
            boolean containsProps = line.endsWith("1");
            line = line.substring(0, line.length() - 1);
            line = line + ".img";
            imgs[i] = new IMG(line, Constants.GameType.GTA_IV, key, true, containsProps);
            lb.addOneToLoadingBar();
        }

        //load WPL files from GTA.dat
        for (int i = 0; i < gta_dat.ipl.size(); i++) {
            lb.setLabelText("<IPL> " + gta_dat.ipl.get(i));
            IPL tempIPL = new IPL(gameDir + gta_dat.ipl.get(i), Finals.gIV, true);
            iplList.add(tempIPL);
            modelIPL.addElement(gta_dat.ipl.get(i));
            lb.addOneToLoadingBar();
        }

        //load water.dat files from gta.dat
        for (int i = 0; i < gta_dat.water.size(); i++) {
            lb.setLabelText("<WATER> " + gta_dat.ipl.get(i));
            waters[i] = new Water(gameDir + gta_dat.water.get(i), Finals.gIV);
            lb.addOneToLoadingBar();
        }


        //count total wpl files in imgs
        int imgWPLCount = 0;
        for (int i = 0; i < imgs.length; i++) {
            imgWPLCount += imgs[i].getWplCount();
        }
        lb.setLoadingBarValue(0);
        lb.setLoadingBarMax(imgWPLCount);

        //load WPL files from IMG files
        for (int i = 0; i < imgs.length; i++) {
            if (imgs[i].getWplCount() > 0) {
                ReadFunctions rf = new ReadFunctions(); //open the img file
                if (rf.openFile(imgs[i].getFileName())) {
                    for (int j = 0; j < imgs[i].getItems().size(); j++) {
                        if (imgs[i].getItems().get(j).getName().toLowerCase().endsWith(".wpl")) {
                            rf.seek(imgs[i].getItems().get(j).getOffset());
                            IPL tempIPL = new IPL(rf, Finals.gIV, true, imgs[i], imgs[i].getItems().get(j));
                            tempIPL.setFileName(imgs[i].getItems().get(j).getName());
                            lb.setLabelText("<WPL> " + imgs[i].getItems().get(j).getName());
                            iplList.add(tempIPL);
                            modelIPL.addElement(imgs[i].getItems().get(j).getName());
                            lb.addOneToLoadingBar();
                        }
                    }
                }
                rf.closeFile();
            }
        }

        //Put the wpl files into an array
        ipls = new IPL[iplList.size()];
        for (int i = 0; i < iplList.size(); i++) {
            iplList.get(i).lodWPL = i;
            ipls[i] = iplList.get(i);
        }

        //Put the vehicle names into a combobox model for later use
        for (int i = 0; i < vehicles.items_cars.size(); i++) {
            this.modelVehicles.addElement(vehicles.items_cars.get(i).modelName);
        }
        lb.setFinished();
    }

    public void save() {
        if (gta_dat.changed) {
            System.out.println("Saving gta.dat");
            gta_dat.save();
        }
        for (int i = 0; i < ides.length; i++) {
            if (ides[i].changed) {
                ides[i].save();
                ides[i].changed = false;
                System.out.println("Saving ide " + i);
            }
        }
        for (int i = 0; i < ipls.length; i++) {
            if (ipls[i].changed) {
                ipls[i].save();
                ipls[i].changed = false;
                System.out.println("Saving ipl " + i);
            }
        }
        for (int i = 0; i < imgs.length; i++) {
            if (imgs[i].isChanged()) {
                imgs[i].save();
                imgs[i].setChanged(false);
                System.out.println("Saving img " + i);
            }
        }
    }

    public DefaultListModel getSaveModel() {
        DefaultListModel saveModel = new DefaultListModel();

        if (gta_dat.changed) saveModel.addElement("gta.dat");

        System.out.println(ides.length);
        System.out.println(ipls.length);
        System.out.println(imgs.length);
        for (int i = 0; i < ides.length; i++) {
            if (ides[i].changed) {
                saveModel.addElement(ides[i].getFileName());
            }
        }
        for (int i = 0; i < ipls.length; i++) {
            if (ipls[i].changed) {
                saveModel.addElement(ipls[i].getFileName());
            }
        }
        for (int i = 0; i < imgs.length; i++) {
            if (imgs[i].isChanged()) {
                saveModel.addElement(imgs[i].getFileName());
            }
        }

        return saveModel;
    }

    public void addIPLItem(String name, int iplID, Vector3D pos) {
        Item_INST iplItem = new Item_INST(Finals.gIV);
        iplItem.name = name;
        iplItem.interior = 0;
        iplItem.lod = -1;
        iplItem.position.x = pos.x;
        iplItem.position.y = 0 - pos.z;
        iplItem.position.z = pos.y;
        ipls[iplID].items_inst.add(iplItem);
        ipls[iplID].changed = true;
        modelIPLItems.addElement(name);
        //addHashToIni(name);
        iplItem = null;
    }

    public int addIDEItem(Item_OBJS tmp, int ideID) {
        ides[ideID].items_objs.add(tmp);
        ides[ideID].changed = true;
        modelIDEItems.addElement(tmp.modelName);
        return ides[ideID].items_objs.size() - 1;
    }

    public void updateIDEItemList(int ideID, int type) {
        modelIDEItems.clear();
        switch (type) {
            case 0:
                for (int i = 0; i < ides[ideID].items_objs.size(); i++) {
                    modelIDEItems.addElement(ides[ideID].items_objs.get(i).modelName);
                }
                break;
        }
    }

    public void updateIPLItemList(int iplID, int type) {
        modelIPLItems.clear();
        switch (type) {
            case 0:
                for (int i = 0; i < ipls[iplID].items_inst.size(); i++) {
                    modelIPLItems.addElement(ipls[iplID].items_inst.get(i).name);
                }
                break;
            case 1:
                for (int i = 0; i < ipls[iplID].items_grge.size(); i++) {
                    modelIPLItems.addElement(ipls[iplID].items_grge.get(i).name);
                }
                break;
            case 2:
                for (int i = 0; i < ipls[iplID].items_cars.size(); i++) {
                    if (!ipls[iplID].items_cars.get(i).name.equals("")) {
                        modelIPLItems.addElement(ipls[iplID].items_cars.get(i).name);
                    } else {
                        modelIPLItems.addElement("Random");
                    }
                }
                break;
            case 3:
                for (int i = 0; i < ipls[iplID].items_cull.size(); i++) {
                    modelIPLItems.addElement(ipls[iplID].items_cull.get(i).name);
                }
                break;
            case 4:
                for (int i = 0; i < ipls[iplID].items_strbig.size(); i++) {
                    modelIPLItems.addElement(ipls[iplID].items_strbig.get(i).modelName);
                }
                break;
            case 5:
                for (int i = 0; i < ipls[iplID].items_lcul.size(); i++) {
                    modelIPLItems.addElement(ipls[iplID].items_lcul.get(i).name1);
                }
                break;
            case 6:
                for (int i = 0; i < ipls[iplID].items_zone.size(); i++) {
                    modelIPLItems.addElement(ipls[iplID].items_zone.get(i));
                }
                break;
            case 7:
                for (int i = 0; i < ipls[iplID].items_blok.size(); i++) {
                    modelIPLItems.addElement(ipls[iplID].items_blok.get(i));
                }
                break;
        }
    }

    public void addNewIDE(File file) {
        if (file != null) {
            if (file.exists()) {
                JOptionPane.showMessageDialog(null, "File already exists");
            } else {
                IDE tempIDE = new IDE(file.getAbsolutePath(), Finals.gIV, true);
                tempIDE.changed = true;
                IDE[] tempIDES = new IDE[ides.length + 1];
                for (int i = 0; i < ides.length; i++) {
                    tempIDES[i] = ides[i];
                }
                tempIDES[ides.length] = tempIDE;
                ides = tempIDES;
                modelIDE.addElement(file.getName());
                tempIDES = null;
                tempIDE = null;
            }
        }
    }

    public void addNewIPL(File file) {
        if (file != null) {
            if (file.exists()) {
                JOptionPane.showMessageDialog(null, "File already exists");
            } else {
                IPL tempIPL = new IPL(file.getAbsolutePath(), Finals.gIV, false);
                tempIPL.changed = true;
                IPL[] tempIPLS = new IPL[ipls.length + 1];
                for (int i = 0; i < ipls.length; i++) {
                    tempIPLS[i] = ipls[i];
                }
                tempIPLS[ipls.length] = tempIPL;
                ipls = tempIPLS;
                //checkList.setIPLS(tempIPLS);
                modelIPL.addElement(file.getName());
                tempIPLS = null;
                tempIPL = null;
                String fixedIplPath = file.getPath().toLowerCase().replace(gameDir.toLowerCase(), "");
                gta_dat.ipl.add(fixedIplPath);
                gta_dat.changed = true;
            }
        }
    }

    public void setSelection(int selType, int selParam1, int selParam2) {
        if (this.selType != -1) {
            switch (this.selType) {
                case Finals.pickMap:
                    ipls[this.selParam1].items_inst.get(this.selParam2).selected = false;
                    break;
                case Finals.pickWater:
                    waters[0].planes.get(this.selParam1).selected = false;
                    break;
                case Finals.pickCar:
                    ipls[this.selParam1].items_cars.get(this.selParam2).selected = false;
                    break;
                default:
                    System.out.println("--Something went wrong--");
                    System.out.println("SelType: " + selType);
                    System.out.println("SelParam1: " + selParam1);
                    System.out.println("SelParam2: " + selParam2);
            }
        }
        this.selType = selType;
        this.selParam1 = selParam1;
        this.selParam2 = selParam2;
        if (this.selType != -1) {
            switch (selType) {
                case Finals.pickMap:
                    ipls[selParam1].items_inst.get(selParam2).selected = true;
                    break;
                case Finals.pickWater:
                    waters[0].planes.get(selParam1).selected = true;
                    break;
                case Finals.pickCar:
                    ipls[selParam1].items_cars.get(selParam2).selected = true;
                    break;
                default:
                    System.out.println("--Something went wrong--");
                    System.out.println("SelType: " + selType);
                    System.out.println("SelParam1: " + selParam1);
                    System.out.println("SelParam2: " + selParam2);
            }
        }
    }

    public void run() {
        init();
    }
}
