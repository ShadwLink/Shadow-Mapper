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
    fun loadPlacement(wpl: Ipl, printName: String) {
        println("Loading.. bin wpl $printName")
        val rf: ReadFunctions
        if (wpl.rf == null) {
            rf = ReadFunctions(wpl.fileName)
        } else {
            wpl.isStream = true
            rf = wpl.rf!!
        }

        val version = rf.readInt() // always 3
        val instanceCount = rf.readInt() // Number of instances
        val unused1 = rf.readInt() // unused
        val garageCount = rf.readInt() // number of garages
        val carCount = rf.readInt() // number of cars
        val cullCount = rf.readInt() // number of culls
        val unused2 = rf.readInt() // unused
        val unused3 = rf.readInt() // unused
        val unused4 = rf.readInt() // unused
        val strBigCount = rf.readInt() // number of strbig
        val lodCullCount = rf.readInt() // number of lod cull
        val zoneCount = rf.readInt() // number of zones
        val unused5 = rf.readInt() // unused
        val unused6 = rf.readInt() // unused
        val unused7 = rf.readInt() // unused
        val unused8 = rf.readInt() // unused
        val blokCount = rf.readInt() // number of bloks

        for (i in 0 until instanceCount) {
            val item = Item_INST(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsInst.add(item)
        }
        for (i in 0 until garageCount) {
            val item = Item_GRGE(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsGrge.add(item)
        }
        for (i in 0 until carCount) {
            val item = Item_CARS(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsCars.add(item)
        }
        for (i in 0 until cullCount) {
            val item = Item_CULL(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsCull.add(item)
        }
        for (i in 0 until strBigCount) {
            val item = Item_STRBIG(wpl.gameType)
            item.read(rf)
            wpl.itemsStrBig.add(item)
        }
        for (i in 0 until lodCullCount) {
            val item = Item_LCUL(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsLCul.add(item)
        }
        for (i in 0 until zoneCount) {
            val item = Item_ZONE(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsZone.add(item)
        }
        for (i in 0 until blokCount) {
            val item = Item_BLOK(wpl.gameType)
            item.read(rf, hashTable)
            wpl.itemsBlok.add(item)
        }
        if (wpl.rf == null) {
            rf.closeFile()
        }
        wpl.loaded = true
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
        wf.write(wpl.itemsStrBig.size)
        wf.write(wpl.itemsLCul.size)
        wf.write(wpl.itemsZone.size)
        wf.write(0)
        wf.write(0)
        wf.write(0)
        wf.write(0)
        wf.write(wpl.itemsBlok.size)
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