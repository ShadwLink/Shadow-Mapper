package nl.shadowlink.tools.shadowmapper.gui;

import com.nikhaldimann.inieditor.IniEditor;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.Vector3D;
import nl.shadowlink.tools.shadowlib.dat.GtaDat;
import nl.shadowlink.tools.shadowlib.ide.IDE;
import nl.shadowlink.tools.shadowlib.ide.Item_OBJS;
import nl.shadowlink.tools.shadowlib.img.Img;
import nl.shadowlink.tools.shadowlib.ipl.IPL;
import nl.shadowlink.tools.shadowlib.ipl.Item_INST;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowlib.water.Water;
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable;
import nl.shadowlink.tools.shadowmapper.utils.hashing.OneAtATimeHasher;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * @author Shadow-Link
 */
public class FileManager extends Thread {
    private HashTable hashTable = new HashTable(new OneAtATimeHasher());

    private LoadingBar lb;

    public GtaDat gtaDat; //object of the gta.dat file
    public IPL[] ipls; //objects of the ipl files
    public IDE[] ides; //objects of the ide files
    public Img[] imgs; //objects of the img files
    public Water[] waters; //objects of the water.dat files

    public DefaultListModel modelIPL = new DefaultListModel(); //contains ipls
    public DefaultListModel modelIDE = new DefaultListModel(); //contains ides
    public DefaultListModel modelIPLItems = new DefaultListModel();
    public DefaultListModel modelIDEItems = new DefaultListModel();
    public DefaultComboBoxModel modelVehicles = new DefaultComboBoxModel();

    public int selType = -1;
    public int selParam1 = -1;
    public int selParam2 = -1;

    private final byte[] key;
    private String gameDir;
    private GameType gameType;

    public FileManager(LoadingBar lb, byte[] key) {
        this.lb = lb;
        this.key = key;
    }

    public String getGameDir() {
        return gameDir;
    }

    public void setGameDir(String gameDir) {
        this.gameDir = gameDir;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void addIMG(String file) {
        Img tempImg = new Img(file, GameType.GTA_IV, key, false, true);
        tempImg.setChanged(true);
        Img[] tempImgs = new Img[imgs.length + 1];
        for (int i = 0; i < imgs.length; i++) {
            tempImgs[i] = imgs[i];
        }
        tempImgs[imgs.length] = tempImg;
        imgs = tempImgs;
    }

    public void init() {
        GtaDat default_dat = new GtaDat(gameDir + "common/data/default.dat", gameDir);
        gtaDat = new GtaDat(gameDir + "common/data/gta.dat", gameDir);

        int itemsToLoad = gtaDat.ide.size() + gtaDat.img.size() + gtaDat.water.size() + gtaDat.ipl.size();

        lb.setLoadingBarMax(itemsToLoad);

        ides = new IDE[gtaDat.ide.size()];
        imgs = new Img[gtaDat.img.size()];
        waters = new Water[gtaDat.water.size()];
        ArrayList<IPL> iplList = new ArrayList();

        // Load default.dat
        default_dat.ide.forEach(ideName -> {
            IDE ide = new IDE(gameDir + ideName, gameType, true);
            ide.items_objs.forEach(item -> hashTable.add(item.modelName));
            ide.items_tobj.forEach(item -> hashTable.add(item.modelName));
            ide.items_cars.forEach(item -> hashTable.add(item.modelName));
            ide.items_anim.forEach(item -> hashTable.add(item.modelName));
            ide.items_2dfx.forEach(item -> hashTable.add(item.name));
            ide.items_tanm.forEach(item -> hashTable.add(item.modelName));

        });

        // load IDE files from GTA.dat
        for (int i = 0; i < gtaDat.ide.size(); i++) {
            lb.setLabelText("<IDE> " + gtaDat.ide.get(i));
            ides[i] = new IDE(gameDir + gtaDat.ide.get(i), gameType, true);
            ides[i].items_objs.forEach(item -> hashTable.add(item.modelName));
            ides[i].items_tobj.forEach(item -> hashTable.add(item.modelName));
            ides[i].items_cars.forEach(item -> hashTable.add(item.modelName));
            ides[i].items_anim.forEach(item -> hashTable.add(item.modelName));
            ides[i].items_2dfx.forEach(item -> hashTable.add(item.name));
            ides[i].items_tanm.forEach(item -> hashTable.add(item.modelName));

            modelIDE.addElement(gtaDat.ide.get(i));
            lb.addOneToLoadingBar();
        }

        loadHashesFromIni();
        System.out.println("Missed hash count " + hashTable.getMissedHashCount());

        //load IMG files from GTA.dat
        for (int i = 0; i < gtaDat.img.size(); i++) {
            lb.setLabelText("<IMG> " + gtaDat.img.get(i));
            String line = gameDir + gtaDat.img.get(i);
            boolean containsProps = line.endsWith("1");
            line = line.substring(0, line.length() - 1);
            line = line + ".img";
            imgs[i] = new Img(line, GameType.GTA_IV, key, true, containsProps);
            lb.addOneToLoadingBar();
        }

        //load WPL files from GTA.dat
        for (int i = 0; i < gtaDat.ipl.size(); i++) {
            lb.setLabelText("<IPL> " + gtaDat.ipl.get(i));
            IPL tempIPL = new IPL(gameDir + gtaDat.ipl.get(i), hashTable, gameType, true);
            iplList.add(tempIPL);
            modelIPL.addElement(gtaDat.ipl.get(i));
            lb.addOneToLoadingBar();
        }

        //load water.dat files from gta.dat
        for (int i = 0; i < gtaDat.water.size(); i++) {
            lb.setLabelText("<WATER> " + gtaDat.ipl.get(i));
            waters[i] = new Water(gameDir + gtaDat.water.get(i), gameType);
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
                ReadFunctions rf = new ReadFunctions(imgs[i].getFileName());
                for (int j = 0; j < imgs[i].getItems().size(); j++) {
                    if (imgs[i].getItems().get(j).getName().toLowerCase().endsWith(".wpl")) {
                        rf.seek(imgs[i].getItems().get(j).getOffset());
                        IPL tempIPL = new IPL(rf, hashTable, gameType, true, imgs[i], imgs[i].getItems().get(j), imgs[i].getItems().get(j).getName());
                        tempIPL.setFileName(imgs[i].getItems().get(j).getName());
                        lb.setLabelText("<WPL> " + imgs[i].getItems().get(j).getName());
                        iplList.add(tempIPL);
                        modelIPL.addElement(imgs[i].getItems().get(j).getName());
                        lb.addOneToLoadingBar();
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

        lb.setFinished();
    }

    private void loadHashesFromIni() {
        IniEditor hashesIni = new IniEditor();
        try {
            hashesIni.load(new File(FileManager.class.getResource("/hashes.ini").getPath()));

            BiConsumer<String, String> hashConsumer = (hash, value) -> hashTable.add(Long.valueOf(hash), value);
            hashesIni.getSectionMap("cars").forEach(hashConsumer);
            hashesIni.getSectionMap("hashes").forEach(hashConsumer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        if (gtaDat.changed) {
            System.out.println("Saving gta.dat");
            gtaDat.save();
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
                ipls[i].save(hashTable);
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

        if (gtaDat.changed) saveModel.addElement("gta.dat");

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
        Item_INST iplItem = new Item_INST(gameType);
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
                IDE tempIDE = new IDE(file.getAbsolutePath(), gameType, true);
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
                IPL tempIPL = new IPL(file.getAbsolutePath(), hashTable, gameType, false);
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
                gtaDat.ipl.add(fixedIplPath);
                gtaDat.changed = true;
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
