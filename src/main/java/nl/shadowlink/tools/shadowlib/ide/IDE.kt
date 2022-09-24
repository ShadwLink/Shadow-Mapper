package nl.shadowlink.tools.shadowlib.ide

import nl.shadowlink.tools.shadowlib.utils.Constants
import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.*
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class IDE(
    var fileName: String,
    var gameType: GameType,
    autoLoad: Boolean
) {
    private var fileReader: FileReader? = null
    private var input: BufferedReader? = null
    var changed = false
    private var readItem = -1 //used to identify what type of section we are reading
    val itemObjs = mutableListOf<ItemObject>()
    val itemTobj = mutableListOf<ItemTimedObject>()
    val itemTree = mutableListOf<ItemTree>()
    val itemPath = mutableListOf<ItemPath>()
    val itemAnim = mutableListOf<ItemAnimated>()
    val itemTAnm = mutableListOf<ItemTimedAnimated>()
    val itemMlo = mutableListOf<ItemMlo>()
    val item2dfx = mutableListOf<Item2DFX>()
    val itemsAMat = mutableListOf<ItemAnimatedMaterial>()
    val itemTxdp = mutableListOf<ItemTxdPack>()
    val itemCars = mutableListOf<ItemCars>()

    init {
        if (autoLoad) loadIDE()
    }

    private fun loadIDE(): Boolean {
        if (openIDE()) {
            try {
                var line: String? = null
                while (input!!.readLine().also { line = it } != null) {
                    if (readItem == -1) {
                        if (line!!.startsWith("#")) {
                            println("Commentaar: $line")
                        } else if (line!!.startsWith("2dfx")) {
                            readItem = Constants.i2DFX
                        } else if (line!!.startsWith("anim")) {
                            readItem = Constants.iANIM
                        } else if (line!!.startsWith("cars")) {
                            readItem = Constants.iCARS
                        } else if (line!!.startsWith("hier")) {
                            readItem = Constants.iHIER
                        } else if (line!!.startsWith("mlo")) {
                            readItem = Constants.iMLO
                        } else if (line!!.startsWith("objs")) {
                            readItem = Constants.iOBJS
                        } else if (line!!.startsWith("path")) {
                            readItem = Constants.iPATH
                        } else if (line!!.startsWith("peds")) {
                            readItem = Constants.iPEDS
                        } else if (line!!.startsWith("tanm")) {
                            readItem = Constants.iTANM
                        } else if (line!!.startsWith("tobj")) {
                            readItem = Constants.iTOBJ
                        } else if (line!!.startsWith("tree")) {
                            readItem = Constants.iTREE
                        } else if (line!!.startsWith("txdp")) {
                            readItem = Constants.iTXDP
                        } else if (line!!.startsWith("weap")) {
                            readItem = Constants.iWEAP
                        }
                        //Message.displayMsgHigh("Started reading item " + readItem);
                    } else {
                        if (line!!.startsWith("end")) {
                            //Message.displayMsgHigh("Item " + readItem + " ended");
                            readItem = -1
                        } else if (line!!.startsWith("#")) {
                            println("Comment: $line")
                        } else if (line!!.isEmpty()) {
                            println("Empty line")
                        } else {
                            var item: IdeItem? = null
                            when (readItem) {
                                Constants.i2DFX -> item = Item2DFX(gameType)
                                Constants.iANIM -> item = ItemAnimated(gameType)
                                Constants.iCARS -> {
                                    item = ItemCars(gameType)
                                    itemCars.add(item)
                                }

                                Constants.iHIER -> item = ItemHier(gameType)
                                Constants.iMLO -> item = ItemMlo(gameType)
                                Constants.iOBJS -> {
                                    item = ItemObject(gameType)
                                    itemObjs.add(item)
                                }

                                Constants.iPATH -> item = ItemPath(gameType)
                                Constants.iPEDS -> item = ItemPeds(gameType)
                                Constants.iTANM -> item = ItemTimedAnimated(gameType)
                                Constants.iTOBJ -> {
                                    item = ItemTimedObject(gameType)
                                    itemTobj.add(item)
                                }

                                Constants.iTREE -> item = ItemTree(gameType)
                                Constants.iTXDP -> {
                                    item = ItemTxdPack(gameType)
                                    itemTxdp.add(item)
                                }

                                Constants.iWEAP -> item = ItemWeapon(gameType)
                                else -> {}
                            }
                            item!!.read(line!!)
                        }
                    }
                }
                closeIDE()
            } catch (ex: IOException) {
                Logger.getLogger(IDE::class.java.name).log(Level.SEVERE, null, ex)
            }
        } else {
            //Message.displayMsgHigh("Unable to open file: " + fileName);
        }
        return true
    }

    fun findItem(name: String): IdeItem? {
        var ret: IdeItem? = null
        if (itemObjs.size != 0) {
            var i = 0
            var item = itemObjs[i]
            while (item.modelName != name) {
                item = if (i < itemObjs.size - 1) {
                    i++
                    itemObjs[i]
                } else {
                    break
                }
            }
            if (item.modelName == name) {
                //Message.displayMsgSuper("<IDE " + fileName + ">Found file " + name + " at " + i);
                ret = itemObjs[i]
            } else {
                //Message.displayMsgSuper("<IDE " + fileName + ">Unable to find file " + name);
            }
        }
        return ret
    }

    fun openIDE(): Boolean {
        try {
            fileReader = FileReader(fileName)
            input = BufferedReader(fileReader)
        } catch (ex: IOException) {
            return false
        }
        return true
    }

    fun closeIDE(): Boolean {
        try {
            input!!.close()
            fileReader!!.close()
        } catch (ex: IOException) {
            return false
        }
        return true
    }

    fun save() {
        try {
            var fileWriter: FileWriter? = null
            var output: BufferedWriter? = null
            fileWriter = FileWriter(fileName)
            output = BufferedWriter(fileWriter)
            output.write("objs")
            output.newLine()
            itemObjs.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("tobj")
            output.newLine()
            itemTobj.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("tree")
            output.newLine()
            itemTree.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("path")
            output.newLine()
            itemPath.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("anim")
            output.newLine()
            itemAnim.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("tanm")
            output.newLine()
            itemTAnm.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("mlo")
            output.newLine()
            itemMlo.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("2dfx")
            output.newLine()
            item2dfx.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("amat")
            output.newLine()
            itemsAMat.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.write("txdp")
            output.newLine()
            itemTxdp.forEach { it.save(output) }
            output.write("end")
            output.newLine()
            output.flush()
            output.close()
            fileWriter.close()
        } catch (ex: IOException) {
            Logger.getLogger(IDE::class.java.name).log(Level.SEVERE, null, ex)
        }
    }
}