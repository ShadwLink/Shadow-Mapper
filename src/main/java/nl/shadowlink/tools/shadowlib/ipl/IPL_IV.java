package nl.shadowlink.tools.shadowlib.ipl;

import com.nikhaldimann.inieditor.IniEditor;
import nl.shadowlink.tools.io.ReadFunctions;
import nl.shadowlink.tools.io.WriteFunctions;

import java.io.IOException;

/**
 * @author Shadow-Link
 */
public class IPL_IV {
    private int version; // always 3
    private int inst; // Number of instances
    private int unused1; // unused
    private int grge; // number of garages
    private int cars; // number of cars
    private int cull; // number of culls
    private int unused2; // unused
    private int unused3; // unsued
    private int unused4; // unused
    private int strbig; // number of strbig
    private int lcul; // number of lod cull
    private int zone; // number of zones
    private int unused5; // unused
    private int unused6; // unused
    private int unused7; // unused
    private int unused8; // unused
    private int blok; // number of bloks

    public void loadPlacement(IPL wpl) {
        System.out.println("Loading.. bin wpl");
        ReadFunctions rf;
        if (wpl.rf == null) {
            rf = new ReadFunctions();
            rf.openFile(wpl.getFileName());
        } else {
            wpl.stream = true;
            rf = wpl.rf;
        }

        readHeader(rf);

        IniEditor ini = new IniEditor();
        try {
            ini.load("hashes.ini");
        } catch (IOException ex) {
            System.out.println("Unable to open INI");
        }

        for (int i = 0; i < inst; i++) {
            Item_INST item = new Item_INST(wpl.getGameType());
            item.read(rf, ini);
            wpl.items_inst.add(item);
        }
        for (int i = 0; i < grge; i++) {
            Item_GRGE item = new Item_GRGE(wpl.getGameType());
            item.read(rf, ini);
            wpl.items_grge.add(item);
        }
        for (int i = 0; i < cars; i++) {
            Item_CARS item = new Item_CARS(wpl.getGameType());
            item.read(rf, ini);
            wpl.items_cars.add(item);
        }
        for (int i = 0; i < cull; i++) {
            Item_CULL item = new Item_CULL(wpl.getGameType());
            item.read(rf, ini);
            wpl.items_cull.add(item);
        }
        for (int i = 0; i < strbig; i++) {
            Item_STRBIG item = new Item_STRBIG(wpl.getGameType());
            item.read(rf);
            wpl.items_strbig.add(item);
        }
        for (int i = 0; i < lcul; i++) {
            Item_LCUL item = new Item_LCUL(wpl.getGameType());
            item.read(rf, ini);
            wpl.items_lcul.add(item);
        }
        for (int i = 0; i < zone; i++) {
            Item_ZONE item = new Item_ZONE(wpl.getGameType());
            item.read(rf, ini);
            wpl.items_zone.add(item);
        }
        for (int i = 0; i < blok; i++) {
            Item_BLOK item = new Item_BLOK(wpl.getGameType());
            item.read(rf, ini);
            wpl.items_blok.add(item);
        }

        ini = null;

        if (wpl.rf == null) {
            rf.closeFile();
        }
        wpl.loaded = true;
    }

    public void readHeader(ReadFunctions rf) {
        version = rf.readInt(); // always 3
        inst = rf.readInt(); // Number of instances
        unused1 = rf.readInt(); // unused
        grge = rf.readInt(); // number of garages
        cars = rf.readInt(); // number of cars
        cull = rf.readInt(); // number of culls
        unused2 = rf.readInt(); // unused
        unused3 = rf.readInt(); // unsued
        unused4 = rf.readInt(); // unused
        strbig = rf.readInt(); // number of strbig
        lcul = rf.readInt(); // number of lod cull
        zone = rf.readInt(); // number of zones
        unused5 = rf.readInt(); // unused
        unused6 = rf.readInt(); // unused
        unused7 = rf.readInt(); // unused
        unused8 = rf.readInt(); // unused
        blok = rf.readInt(); // number of bloks
        // System.out.println(version);
        // System.out.println(inst);
        /* System.out.println(unused1); System.out.println(grge); System.out.println(cars); System.out.println(cull);
         * System.out.println(unused2); System.out.println(unused3); System.out.println(unused4);
         * System.out.println(strbig); System.out.println(lcul); System.out.println(zone); System.out.println(unused5);
         * System.out.println(unused6); System.out.println(unused7); System.out.println(unused8);
         * System.out.println(blok); */
        // //Message.displayMsgHigh
    }

    public void writeHeader(WriteFunctions wf, IPL wpl) {
        wf.write(3);
        wf.write(wpl.items_inst.size());
        wf.write(0);
        wf.write(wpl.items_grge.size());
        wf.write(wpl.items_cars.size());
        wf.write(wpl.items_cull.size());
        wf.write(0);
        wf.write(0);
        wf.write(0);
        wf.write(strbig); // temp
        wf.write(lcul); // temp
        wf.write(zone); // temp
        wf.write(0);
        wf.write(0);
        wf.write(0);
        wf.write(0);
        wf.write(blok); // temp
    }

    public void save(IPL wpl) {
        WriteFunctions wf = new WriteFunctions();
        if (wpl.stream) {
            System.out.println("Saving Stream WPL");
            wf.openFile(wpl.img.getFileName());
            wf.gotoEnd();
            wpl.imgItem.setOffset(wf.getFileSize());
        } else {
            wf.openFile(wpl.getFileName());
        }

        writeHeader(wf, wpl);

        for (int i = 0; i < wpl.items_inst.size(); i++) {
            wpl.items_inst.get(i).write(wf);
        }
        for (int i = 0; i < wpl.items_grge.size(); i++) {
            wpl.items_grge.get(i).write(wf);
        }
        for (int i = 0; i < wpl.items_cars.size(); i++) {
            wpl.items_cars.get(i).write(wf);
        }

        if (wpl.stream) {
            wpl.imgItem.setSize(wf.getFileSize() - wpl.imgItem.getOffset());
            wpl.img.setChanged(true);
        }

        wf.closeFile();
    }

}
