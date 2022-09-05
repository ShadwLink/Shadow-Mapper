package nl.shadowlink.tools.shadowlib.ipl;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.shadowlib.img.IMG;
import nl.shadowlink.tools.shadowlib.img.IMG_Item;
import nl.shadowlink.tools.shadowlib.utils.Constants;
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class IPL {
    public ReadFunctions rf = null;
    private String fileName = "";
    private int gameType;
    public boolean changed = false; // True when the file needs to be saved

    public boolean loaded = false;
    public boolean selected = false;
    public boolean itemsLoaded = false;

    public boolean stream = false; // if it's a stream wpl
    public IMG img = null; // the img it's in
    public IMG_Item imgItem = null; // img item

    public int lodWPL = -1;

    public ArrayList<Item_AUZO> items_auzo = new ArrayList();
    public ArrayList<Item_CARS> items_cars = new ArrayList();
    public ArrayList<Item_CULL> items_cull = new ArrayList();
    public ArrayList<Item_ENEX> items_enex = new ArrayList();
    public ArrayList<Item_GRGE> items_grge = new ArrayList();
    public ArrayList<Item_INST> items_inst = new ArrayList();
    public ArrayList<Item_JUMP> items_jump = new ArrayList();
    public ArrayList<Item_MULT> items_mult = new ArrayList();
    public ArrayList<Item_OCCL> items_occl = new ArrayList();
    public ArrayList<Item_PATH> items_path = new ArrayList();
    public ArrayList<Item_PICK> items_pick = new ArrayList();
    public ArrayList<Item_TCYC> items_tcyc = new ArrayList();
    public ArrayList<Item_STRBIG> items_strbig = new ArrayList();
    public ArrayList<Item_LCUL> items_lcul = new ArrayList();
    public ArrayList<Item_ZONE> items_zone = new ArrayList();
    public ArrayList<Item_BLOK> items_blok = new ArrayList();

    public IPL(String fileName, HashTable hashTable, int gameType, boolean autoLoad) {
        this.fileName = fileName;
        this.gameType = gameType;
        System.out.println("Started loading: " + this.fileName);
        if (autoLoad)
            loadPlacement(hashTable);
    }

    public IPL(ReadFunctions rf, HashTable hashTable, int gameType, boolean autoLoad, IMG img, IMG_Item imgItem) {
        this.gameType = gameType;
        this.rf = rf;
        this.img = img;
        this.imgItem = imgItem;
        if (autoLoad)
            loadPlacement(hashTable);
    }

    private boolean loadPlacement(HashTable hashTable) {
        switch (gameType) {
            case Constants.gIV:
                if (fileName.contains("common"))
                    new IPL_III_ERA().loadPlacement(this);
                else
                    new IPL_IV(hashTable).loadPlacement(this);
                break;
            default:
                new IPL_III_ERA().loadPlacement(this);
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

    public void save(HashTable hashTable) {
        switch (gameType) {
            case Constants.gIV:
                if (fileName.contains("common"))
                    new IPL_III_ERA().save(this);
                else
                    new IPL_IV(hashTable).save(this);
                break;
            default:
                new IPL_III_ERA().save(this);
        }
    }

}