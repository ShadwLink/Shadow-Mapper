package nl.shadowlink.tools.shadowlib.ide;

import nl.shadowlink.tools.shadowlib.utils.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shadow-Link
 */
public class IDE {
    private FileReader fileReader; //Reader
    private BufferedReader input; //Reader

    private String fileName; //name of the file
    private int gameType; //gametype ie: SA

    public boolean changed = false; //True when the file needs to be saved

    private int readItem = -1; //used to identify what type of section we are reading

    public ArrayList<Item_OBJS> items_objs = new ArrayList();
    public ArrayList<Item_TOBJ> items_tobj = new ArrayList();
    public ArrayList<Item_TREE> items_tree = new ArrayList();
    public ArrayList<Item_PATH> items_path = new ArrayList();
    public ArrayList<Item_ANIM> items_anim = new ArrayList();
    public ArrayList<Item_TANM> items_tanm = new ArrayList();
    public ArrayList<Item_MLO> items_mlo = new ArrayList();
    public ArrayList<Item_2DFX> items_2dfx = new ArrayList();
    public ArrayList<Item_AMAT> items_amat = new ArrayList();
    public ArrayList<Item_TXDP> items_txdp = new ArrayList();
    public ArrayList<Item_CARS> items_cars = new ArrayList();

    public IDE(String fileName, int gameType, boolean autoLoad) {
        this.fileName = fileName;
        this.gameType = gameType;
        if (autoLoad) loadIDE();
    }

    private boolean loadIDE() {
        if (openIDE()) {
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    if (readItem == -1) {
                        if (line.startsWith("#")) {
                            System.out.println("Commentaar: " + line);
                        } else if (line.startsWith("2dfx")) {
                            readItem = Constants.i2DFX;
                        } else if (line.startsWith("anim")) {
                            readItem = Constants.iANIM;
                        } else if (line.startsWith("cars")) {
                            readItem = Constants.iCARS;
                        } else if (line.startsWith("hier")) {
                            readItem = Constants.iHIER;
                        } else if (line.startsWith("mlo")) {
                            readItem = Constants.iMLO;
                        } else if (line.startsWith("objs")) {
                            readItem = Constants.iOBJS;
                        } else if (line.startsWith("path")) {
                            readItem = Constants.iPATH;
                        } else if (line.startsWith("peds")) {
                            readItem = Constants.iPEDS;
                        } else if (line.startsWith("tanm")) {
                            readItem = Constants.iTANM;
                        } else if (line.startsWith("tobj")) {
                            readItem = Constants.iTOBJ;
                        } else if (line.startsWith("tree")) {
                            readItem = Constants.iTREE;
                        } else if (line.startsWith("txdp")) {
                            readItem = Constants.iTXDP;
                        } else if (line.startsWith("weap")) {
                            readItem = Constants.iWEAP;
                        }
                        //Message.displayMsgHigh("Started reading item " + readItem);
                    } else {
                        if (line.startsWith("end")) {
                            //Message.displayMsgHigh("Item " + readItem + " ended");
                            readItem = -1;
                        } else if (line.startsWith("#")) {
                            System.out.println("Comment: " + line);
                        } else if (line.isEmpty()) {
                            System.out.println("Empty line");
                        } else {
                            IDE_Item item = null;
                            switch (readItem) {
                                case Constants.i2DFX:
                                    item = new Item_2DFX(gameType);
                                    break;
                                case Constants.iANIM:
                                    item = new Item_ANIM(gameType);
                                    break;
                                case Constants.iCARS:
                                    item = new Item_CARS(gameType);
                                    items_cars.add((Item_CARS) item);
                                    break;
                                case Constants.iHIER:
                                    item = new Item_HIER(gameType);
                                    break;
                                case Constants.iMLO:
                                    item = new Item_MLO(gameType);
                                    break;
                                case Constants.iOBJS:
                                    item = new Item_OBJS(gameType);
                                    items_objs.add((Item_OBJS) item);
                                    break;
                                case Constants.iPATH:
                                    item = new Item_PATH(gameType);
                                    break;
                                case Constants.iPEDS:
                                    item = new Item_PEDS(gameType);
                                    break;
                                case Constants.iTANM:
                                    item = new Item_TANM(gameType);
                                    break;
                                case Constants.iTOBJ:
                                    item = new Item_TOBJ(gameType);
                                    items_tobj.add((Item_TOBJ) item);
                                    break;
                                case Constants.iTREE:
                                    item = new Item_TREE(gameType);
                                    break;
                                case Constants.iTXDP:
                                    item = new Item_TXDP(gameType);
                                    items_txdp.add((Item_TXDP) item);
                                    break;
                                case Constants.iWEAP:
                                    item = new Item_WEAP(gameType);
                                    break;
                                default:
                                    //Message.displayMsgHigh("Unknown item " + line);
                            }
                            item.read(line);
                        }
                    }
                }
                closeIDE();
            } catch (IOException ex) {
                Logger.getLogger(IDE.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //Message.displayMsgHigh("Unable to open file: " + fileName);
        }
        return true;
    }

    public IDE_Item findItem(String name) {
        IDE_Item ret = null;
        if (items_objs.size() != 0) {
            int i = 0;
            Item_OBJS item = items_objs.get(i);
            while (!item.modelName.equals(name)) {
                if (i < items_objs.size() - 1) {
                    i++;
                    item = items_objs.get(i);
                } else {
                    break;
                }
            }
            if (item.modelName.equals(name)) {
                //Message.displayMsgSuper("<IDE " + fileName + ">Found file " + name + " at " + i);
                ret = items_objs.get(i);
            } else {
                //Message.displayMsgSuper("<IDE " + fileName + ">Unable to find file " + name);
            }
        }
        return ret;
    }

    public boolean openIDE() {
        try {
            fileReader = new FileReader(fileName);
            input = new BufferedReader(fileReader);
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public boolean closeIDE() {
        try {
            input.close();
            fileReader.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public void save() {
        try {
            FileWriter fileWriter = null;
            BufferedWriter output = null;
            fileWriter = new FileWriter(fileName);
            output = new BufferedWriter(fileWriter);
            output.write("objs");
            output.newLine();
            for (int i = 0; i < items_objs.size(); i++) {
                items_objs.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("tobj");
            output.newLine();
            for (int i = 0; i < items_tobj.size(); i++) {
                items_tobj.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("tree");
            output.newLine();
            for (int i = 0; i < items_tree.size(); i++) {
                items_tree.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("path");
            output.newLine();
            for (int i = 0; i < items_path.size(); i++) {
                items_path.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("anim");
            output.newLine();
            for (int i = 0; i < items_anim.size(); i++) {
                items_anim.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("tanm");
            output.newLine();
            for (int i = 0; i < items_tanm.size(); i++) {
                items_tanm.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("mlo");
            output.newLine();
            for (int i = 0; i < items_mlo.size(); i++) {
                items_mlo.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("2dfx");
            output.newLine();
            for (int i = 0; i < items_2dfx.size(); i++) {
                items_2dfx.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("amat");
            output.newLine();
            for (int i = 0; i < items_amat.size(); i++) {
                items_amat.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.write("txdp");
            output.newLine();
            for (int i = 0; i < items_txdp.size(); i++) {
                items_txdp.get(i).save(output);
            }
            output.write("end");
            output.newLine();
            output.flush();
            output.close();
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(IDE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
