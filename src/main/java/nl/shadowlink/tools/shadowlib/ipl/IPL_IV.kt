package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowmapper.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
class IPL_IV(
    private val hashTable: HashTable
) {
    private var version // always 3
            = 0
    private var inst // Number of instances
            = 0
    private var unused1 // unused
            = 0
    private var grge // number of garages
            = 0
    private var cars // number of cars
            = 0
    private var cull // number of culls
            = 0
    private var unused2 // unused
            = 0
    private var unused3 // unsued
            = 0
    private var unused4 // unused
            = 0
    private var strbig // number of strbig
            = 0
    private var lcul // number of lod cull
            = 0
    private var zone // number of zones
            = 0
    private var unused5 // unused
            = 0
    private var unused6 // unused
            = 0
    private var unused7 // unused
            = 0
    private var unused8 // unused
            = 0
    private var blok // number of bloks
            = 0

    fun loadPlacement(wpl: IPL, printName: String) {
        println("Loading.. bin wpl $printName")
        val rf: ReadFunctions
        if (wpl.rf == null) {
            rf = ReadFunctions(wpl.fileName)
        } else {
            wpl.stream = true
            rf = wpl.rf
        }
        readHeader(rf)

        for (i in 0 until inst) {
            val item = Item_INST(wpl.gameType)
            item.read(rf, hashTable)
            wpl.items_inst.add(item)
        }
        for (i in 0 until grge) {
            val item = Item_GRGE(wpl.gameType)
            item.read(rf, hashTable)
            wpl.items_grge.add(item)
        }
        for (i in 0 until cars) {
            val item = Item_CARS(wpl.gameType)
            item.read(rf, hashTable)
            wpl.items_cars.add(item)
        }
        for (i in 0 until cull) {
            val item = Item_CULL(wpl.gameType)
            item.read(rf, hashTable)
            wpl.items_cull.add(item)
        }
        for (i in 0 until strbig) {
            val item = Item_STRBIG(wpl.gameType)
            item.read(rf)
            wpl.items_strbig.add(item)
        }
        for (i in 0 until lcul) {
            val item = Item_LCUL(wpl.gameType)
            item.read(rf, hashTable)
            wpl.items_lcul.add(item)
        }
        for (i in 0 until zone) {
            val item = Item_ZONE(wpl.gameType)
            item.read(rf, hashTable)
            wpl.items_zone.add(item)
        }
        for (i in 0 until blok) {
            val item = Item_BLOK(wpl.gameType)
            item.read(rf, hashTable)
            wpl.items_blok.add(item)
        }
        if (wpl.rf == null) {
            rf.closeFile()
        }
        wpl.loaded = true
    }

    fun readHeader(rf: ReadFunctions) {
        version = rf.readInt() // always 3
        inst = rf.readInt() // Number of instances
        unused1 = rf.readInt() // unused
        grge = rf.readInt() // number of garages
        cars = rf.readInt() // number of cars
        cull = rf.readInt() // number of culls
        unused2 = rf.readInt() // unused
        unused3 = rf.readInt() // unsued
        unused4 = rf.readInt() // unused
        strbig = rf.readInt() // number of strbig
        lcul = rf.readInt() // number of lod cull
        zone = rf.readInt() // number of zones
        unused5 = rf.readInt() // unused
        unused6 = rf.readInt() // unused
        unused7 = rf.readInt() // unused
        unused8 = rf.readInt() // unused
        blok = rf.readInt() // number of bloks
        // System.out.println(version);
        // System.out.println(inst);
        /* System.out.println(unused1); System.out.println(grge); System.out.println(cars); System.out.println(cull);
         * System.out.println(unused2); System.out.println(unused3); System.out.println(unused4);
         * System.out.println(strbig); System.out.println(lcul); System.out.println(zone); System.out.println(unused5);
         * System.out.println(unused6); System.out.println(unused7); System.out.println(unused8);
         * System.out.println(blok); */
        // //Message.displayMsgHigh
    }

    fun writeHeader(wf: WriteFunctions, wpl: IPL) {
        wf.write(3)
        wf.write(wpl.items_inst.size)
        wf.write(0)
        wf.write(wpl.items_grge.size)
        wf.write(wpl.items_cars.size)
        wf.write(wpl.items_cull.size)
        wf.write(0)
        wf.write(0)
        wf.write(0)
        wf.write(strbig) // temp
        wf.write(lcul) // temp
        wf.write(zone) // temp
        wf.write(0)
        wf.write(0)
        wf.write(0)
        wf.write(0)
        wf.write(blok) // temp
    }

    fun save(wpl: IPL) {
        val fileName = if (wpl.stream) {
            wpl.img.fileName
        } else {
            wpl.fileName
        }
        val wf = WriteFunctions(fileName!!)
        if (wpl.stream) {
            println("Saving Stream WPL")
            wf.gotoEnd()
            wpl.imgItem.offset = wf.fileSize
        }
        writeHeader(wf, wpl)
        for (i in wpl.items_inst.indices) {
            wpl.items_inst[i].write(wf)
        }
        for (i in wpl.items_grge.indices) {
            wpl.items_grge[i].write(wf)
        }
        for (i in wpl.items_cars.indices) {
            wpl.items_cars[i].write(wf)
        }
        if (wpl.stream) {
            wpl.imgItem.size = wf.fileSize - wpl.imgItem.offset
            wpl.img.isChanged = true
        }
        wf.closeFile()
    }
}