package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.WriteFunctions
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
class IplIV(
    private val hashTable: HashTable
) {
    private var version = 0
    private var inst = 0
    private var unused1 = 0
    private var grge = 0
    private var cars = 0
    private var cull = 0
    private var unused2 = 0
    private var unused3 = 0
    private var unused4 = 0
    private var strbig = 0
    private var lcul = 0
    private var zone = 0
    private var unused5 = 0
    private var unused6 = 0
    private var unused7 = 0
    private var unused8 = 0
    private var blok = 0

    fun loadPlacement(wpl: Ipl, printName: String) {
        println("Loading.. bin wpl $printName")
        val rf: ReadFunctions
        if (wpl.rf == null) {
            rf = ReadFunctions(wpl.fileName)
        } else {
            wpl.isStream = true
            rf = wpl.rf!!
        }
        readHeader(rf)

        for (i in 0 until inst) {
            val item = Item_INST(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsInst.add(item)
        }
        for (i in 0 until grge) {
            val item = Item_GRGE(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsGrge.add(item)
        }
        for (i in 0 until cars) {
            val item = Item_CARS(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsCars.add(item)
        }
        for (i in 0 until cull) {
            val item = Item_CULL(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsCull.add(item)
        }
        for (i in 0 until strbig) {
            val item = Item_STRBIG(wpl.gameType)
            item.read(rf)
            wpl.itemsStrBig.add(item)
        }
        for (i in 0 until lcul) {
            val item = Item_LCUL(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsLCul.add(item)
        }
        for (i in 0 until zone) {
            val item = Item_ZONE(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsZone.add(item)
        }
        for (i in 0 until blok) {
            val item = Item_BLOK(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsBlok.add(item)
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
    }

    private fun writeHeader(wf: WriteFunctions, wpl: Ipl) {
        wf.write(3)
        wf.write(wpl.itemsInst.size)
        wf.write(0)
        wf.write(wpl.itemsGrge.size)
        wf.write(wpl.itemsCars.size)
        wf.write(wpl.itemsCull.size)
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

    fun save(wpl: Ipl) {
        val fileName = if (wpl.isStream) {
            wpl.img!!.fileName
        } else {
            wpl.fileName
        }
        val wf = WriteFunctions(fileName)
        if (wpl.isStream) {
            println("Saving Stream WPL")
            wf.gotoEnd()
            wpl.imgItem!!.offset = wf.fileSize
        }
        writeHeader(wf, wpl)
        wpl.itemsInst.forEach { it.write(wf) }
        wpl.itemsGrge.forEach { it.write(wf) }
        wpl.itemsCars.forEach { it.write(wf) }
        if (wpl.isStream) {
            wpl.imgItem!!.size = wf.fileSize - wpl.imgItem!!.offset
            wpl.img!!.setSaveRequired()
        }
        wf.closeFile()
    }
}