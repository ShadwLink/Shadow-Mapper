package nl.shadowlink.tools.shadowlib.ipl;

import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.shadowlib.img.Img;
import nl.shadowlink.tools.shadowlib.img.ImgItem;
import nl.shadowlink.tools.shadowlib.utils.GameType;
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable;

import java.util.ArrayList;

/**
 * @author Shadow-Link
 */
public class IPL {
    public ReadFunctions rf = null;
    private String fileName = "";
    private GameType gameType;
    public boolean changed = false; // True when the file needs to be saved

    public boolean loaded = false;
    public boolean selected = false;
    public boolean itemsLoaded = false;

    public boolean isStream = false; // if it's a stream wpl
    public Img img = null; // the img it's in
    public ImgItem imgItem = null; // img item

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

    private String printName;

    public IPL(String fileName, HashTable hashTable, GameType gameType, boolean autoLoad) {
        this.fileName = fileName;
        this.gameType = gameType;
        System.out.println("Started loading: " + this.fileName);
        if (autoLoad)
            loadPlacement(hashTable);
    }

    public IPL(ReadFunctions rf, HashTable hashTable, GameType gameType, boolean autoLoad, Img img, ImgItem imgItem, String printName) {
        this.gameType = gameType;
        this.rf = rf;
        this.img = img;
        this.imgItem = imgItem;
        this.printName = printName;
        if (autoLoad)
            loadPlacement(hashTable);
    }

    private void loadPlacement(HashTable hashTable) {
        switch (gameType) {
            case GTA_IV:
                if (fileName.contains("common"))
                    new IPL_III_ERA().loadPlacement(this);
                else
                    new IplIV(hashTable).loadPlacement(this, printName);
                break;
            default:
                new IPL_III_ERA().loadPlacement(this);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void save(HashTable hashTable) {
        switch (gameType) {
            case GTA_IV:
                if (fileName.contains("common"))
                    new IPL_III_ERA().save(this);
                else
                    new IplIV(hashTable).save(this);
                break;
            default:
                new IPL_III_ERA().save(this);
        }
    }

}
