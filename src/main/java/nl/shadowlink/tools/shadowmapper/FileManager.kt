package nl.shadowlink.tools.shadowmapper

import com.nikhaldimann.inieditor.IniEditor
import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.io.Vector3D
import nl.shadowlink.tools.shadowlib.dat.GtaDat
import nl.shadowlink.tools.shadowlib.ide.*
import nl.shadowlink.tools.shadowlib.img.Img
import nl.shadowlink.tools.shadowlib.img.ImgLoaderFactory
import nl.shadowlink.tools.shadowlib.ipl.Ipl
import nl.shadowlink.tools.shadowlib.ipl.Item_INST
import nl.shadowlink.tools.shadowlib.texturedic.TextureDic
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable
import nl.shadowlink.tools.shadowlib.utils.hashing.OneAtATimeHasher
import nl.shadowlink.tools.shadowlib.water.Water
import nl.shadowlink.tools.shadowmapper.gui.PickingType
import nl.shadowlink.tools.shadowmapper.gui.install.Install
import java.io.File
import java.io.IOException
import java.nio.file.Path
import java.util.*
import java.util.function.Consumer
import javax.swing.DefaultComboBoxModel
import javax.swing.DefaultListModel
import javax.swing.JOptionPane
import kotlin.io.path.Path

/**
 * @author Shadow-Link
 */
class FileManager : Thread() {
    private val hashTable = HashTable(OneAtATimeHasher())
    private var statusCallbacks: LoadingStatusCallbacks? = null

    @JvmField
    var gtaDat: GtaDat? = null

    @JvmField
    var ipls = mutableListOf<Ipl>()

    @JvmField
    var ides = mutableListOf<IDE>()

    @JvmField
    var imgs = mutableListOf<Img>()

    @JvmField
    var waters = mutableListOf<Water>()

    @JvmField
    var modelIPL = DefaultListModel<String>()

    @JvmField
    var modelIDE = DefaultListModel<String>()

    @JvmField
    var modelIPLItems = DefaultListModel<String>()

    @JvmField
    var modelIDEItems = DefaultListModel<String>()

    @JvmField
    var modelVehicles = DefaultComboBoxModel<String>()

    @JvmField
    var selType = -1

    @JvmField
    var selParam1 = -1

    @JvmField
    var selParam2 = -1

    private var key: ByteArray? = null
    private var gameDir: String? = null
    private var gameType: GameType? = null

    val gamePath: Path
        get() = Path(requireNotNull(gameDir))

    fun startLoading(statusCallbacks: LoadingStatusCallbacks?, install: Install, key: ByteArray) {
        this.statusCallbacks = statusCallbacks
        gameDir = install.path
        gameType = install.gameType
        this.key = key
        start()
    }

    /**
     * Should only be used for testing
     */
    fun setInstall(install: Install) {
        gameDir = install.path
        gameType = install.gameType
    }

    private fun startLoading() {
        val imgLoader = ImgLoaderFactory.getImgLoader(gameType!!, key!!)
        val defaultDat = GtaDat(gameDir + "common/data/default.dat", gameDir!!)
        val gtaDat = GtaDat(gameDir + "common/data/gta.dat", gameDir!!)

        val itemsToLoad = defaultDat.ide.size + defaultDat.img.size + defaultDat.water.size + defaultDat.ipl.size +
                gtaDat.ide.size + gtaDat.img.size + gtaDat.water.size + gtaDat.ipl.size

        statusCallbacks?.onStartLoading(itemsToLoad)

        defaultDat.ide.forEach(Consumer { idePath: String ->
            val ide = IDE("$gameDir$idePath", gameType!!, true)
            hashTable.addHashes(ide)
            ides.add(ide)

            modelIDE.addElement(idePath)
        })

        gtaDat.ide.forEach { idePath ->
            statusCallbacks?.onLoadingStatusChanged("<IDE> $idePath")

            val ide = IDE("$gameDir$idePath", gameType!!, true)
            hashTable.addHashes(ide)
            ides.add(ide)

            modelIDE.addElement(idePath)

            statusCallbacks?.onLoadingValueIncreased()
        }

        loadHashesFromIni()
        println("Missed hash count " + hashTable.missedHashCount)

        gtaDat.img.forEach { imgPath ->
            statusCallbacks?.onLoadingStatusChanged("<IMG> $imgPath")

            // TODO: Clean this mess up
            var line = gameDir + imgPath
            val containsProps = line.endsWith("1")
            line = line.substring(0, line.length - 1)
            line = "$line.img"

            imgs.add(imgLoader.load(Path(line)))

            statusCallbacks?.onLoadingValueIncreased()
        }

        gtaDat.ipl.forEach { iplPath ->
            statusCallbacks?.onLoadingStatusChanged("<IPL> $iplPath")
            ipls.add(Ipl("$gameDir$iplPath", hashTable, gameType!!, true))
            modelIPL.addElement(iplPath)
            statusCallbacks?.onLoadingValueIncreased()
        }

        gtaDat.water.forEach { waterPath ->
            statusCallbacks?.onLoadingStatusChanged("<WATER> $waterPath")
            waters.add(Water(gameDir + waterPath, gameType!!))
            statusCallbacks?.onLoadingValueIncreased()
        }

        val imgWPLCount = imgs.sumOf { img -> img.getItemOfTypeCount(".wpl") }

        statusCallbacks?.onStartLoadingWpl(imgWPLCount)
        imgs.forEach { img ->
            val rf = ReadFunctions(img.path)
            img.getItemsOfType(".wpl")
                .forEach { wplEntry ->
                    rf.seek(wplEntry.offset)
                    val ipl = Ipl(rf, hashTable, gameType!!, true, img, wplEntry, wplEntry.name).apply {
                        fileName = wplEntry.name
                    }
                    ipls.add(ipl)

                    statusCallbacks?.onLoadingStatusChanged("<WPL> ${wplEntry.name}")
                    modelIPL.addElement(wplEntry.name)
                    statusCallbacks?.onLoadingValueIncreased()
                }
            rf.closeFile()
        }

        // TODO: Figure out what this used to do?
//        for (i in iplList.indices) {
//            iplList[i].lodWPL = i
//            ipls[i] = iplList[i]
//        }

        statusCallbacks?.onLoadingFinished()
    }

    private fun HashTable.addHashes(ide: IDE) {
        ide.itemObjs.forEach { item: ItemObject -> add(item.modelName) }
        ide.itemTobj.forEach { item: ItemTimedObject -> add(item.modelName) }
        ide.itemCars.forEach { item: ItemCars -> add(item.modelName) }
        ide.itemAnim.forEach { item: ItemAnimated -> add(item.modelName) }
        ide.item2dfx.forEach { item: Item2DFX -> add(item.name) }
        ide.itemTAnm.forEach { item: ItemTimedAnimated -> add(item.modelName) }
    }

    private fun loadHashesFromIni() {
        val hashesIni = IniEditor()
        try {
            hashesIni.load(File(FileManager::class.java.getResource("/hashes.ini")!!.path))
            hashesIni.getSectionMap("cars").forEach { (hash, value) -> hashTable.add(hash.toLong(), value) }
            hashesIni.getSectionMap("hashes").forEach { (hash, value) -> hashTable.add(hash.toLong(), value) }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun save() {
        if (gtaDat?.changed == true) {
            println("Saving gta.dat")
            gtaDat?.save()
        }
        ides.forEach { ide ->
            if (ide.isSaveRequired) {
                ide.save()
                ide.setSaveRequired(false)
                println("Saving ide ${ide.fileName}")
            }
        }
        ipls.forEach { ipl ->
            if (ipl.changed) {
                ipl.save(hashTable)
                ipl.changed = false
                println("Saving ipl ${ipl.fileName}")
            }
        }
        imgs.forEach { img ->
            if (img.isSaveRequired) {
                img.save()
                img.setSaveRequired(false)
            }
        }
    }

    val saveModel: DefaultListModel<String>
        get() {
            val saveModel = DefaultListModel<String>()
            if (gtaDat?.changed == true) saveModel.addElement("gta.dat")
            saveModel.addAll(ides.filter { it.isSaveRequired }.map { it.fileName })
            saveModel.addAll(ipls.filter { it.changed }.map { it.fileName })
            saveModel.addAll(imgs.filter { it.isSaveRequired }.map { it.fileName })
            return saveModel
        }

    fun addIPLItem(name: String, iplID: Int, pos: Vector3D) {
        val iplItem = Item_INST(gameType!!)
        iplItem.name = name
        iplItem.interior = 0
        iplItem.lod = -1
        iplItem.position.x = pos.x
        iplItem.position.y = 0 - pos.z
        iplItem.position.z = pos.y
        ipls[iplID].itemsInst.add(iplItem)
        ipls[iplID].changed = true
        modelIPLItems.addElement(name)
    }

    fun addIDEItem(tmp: ItemObject, ideID: Int): Int {
        ides[ideID].itemObjs.add(tmp)
        ides[ideID].setSaveRequired()
        modelIDEItems.addElement(tmp.modelName)
        return ides[ideID].itemObjs.size - 1
    }

    fun updateIDEItemList(ideID: Int, type: Int) {
        modelIDEItems.clear()
        when (type) {
            0 -> {
                var i = 0
                while (i < ides[ideID].itemObjs.size) {
                    modelIDEItems.addElement(ides[ideID].itemObjs[i].modelName)
                    i++
                }
            }
        }
    }

    fun updateIPLItemList(iplID: Int, type: Int) {
        modelIPLItems.clear()
        when (type) {
            0 -> {
                var i = 0
                while (i < ipls[iplID].itemsInst.size) {
                    modelIPLItems.addElement(ipls[iplID].itemsInst[i].name)
                    i++
                }
            }

            1 -> {
                var i = 0
                while (i < ipls[iplID].itemsGrge.size) {
                    modelIPLItems.addElement(ipls[iplID].itemsGrge[i].name)
                    i++
                }
            }

            2 -> {
                var i = 0
                while (i < ipls[iplID].itemsCars.size) {
                    if (ipls[iplID].itemsCars[i].name != "") {
                        modelIPLItems.addElement(ipls[iplID].itemsCars[i].name)
                    } else {
                        modelIPLItems.addElement("Random")
                    }
                    i++
                }
            }

            3 -> {
                var i = 0
                while (i < ipls[iplID].itemsCull.size) {
                    modelIPLItems.addElement(ipls[iplID].itemsCull[i].name)
                    i++
                }
            }

            4 -> {
                var i = 0
                while (i < ipls[iplID].itemsStrBig.size) {
                    modelIPLItems.addElement(ipls[iplID].itemsStrBig[i].modelName)
                    i++
                }
            }

            5 -> {
                var i = 0
                while (i < ipls[iplID].itemsLCul.size) {
                    modelIPLItems.addElement(ipls[iplID].itemsLCul[i].name1)
                    i++
                }
            }

            6 -> {
                var i = 0
                while (i < ipls[iplID].itemsZone.size) {
                    modelIPLItems.addElement(ipls[iplID].itemsZone[i].toString())
                    i++
                }
            }

            7 -> {
                var i = 0
                while (i < ipls[iplID].itemsBlok.size) {
                    modelIPLItems.addElement(ipls[iplID].itemsBlok[i].toString())
                    i++
                }
            }
        }
    }

    fun addNewIDE(file: File?) {
        if (file != null) {
            if (file.exists()) {
                JOptionPane.showMessageDialog(null, "File already exists")
            } else {
                val ide = IDE(file.absolutePath, gameType!!, true).apply {
                    setSaveRequired()
                }
                ides.add(ide)
                modelIDE.addElement(file.name)
            }
        }
    }

    fun addNewIPL(file: File?) {
        if (file != null) {
            if (file.exists()) {
                JOptionPane.showMessageDialog(null, "File already exists")
            } else {
                val ipl = Ipl(file.absolutePath, hashTable, gameType!!, false).apply {
                    changed = true
                }
                ipls.add(ipl)

                modelIPL.addElement(file.name)
                val fixedIplPath = file.path.lowercase(Locale.getDefault()).replace(
                    gameDir!!.lowercase(Locale.getDefault()), ""
                )
                gtaDat!!.ipl.add(fixedIplPath)
                gtaDat!!.changed = true
            }
        }
    }

    fun setSelection(selType: Int, selParam1: Int, selParam2: Int) {
        if (this.selType != -1) {
            when (this.selType) {
                PickingType.map -> ipls[this.selParam1].itemsInst[this.selParam2].selected = false
                PickingType.water -> waters[0].planes[this.selParam1].selected = false
                PickingType.car -> ipls[this.selParam1].itemsCars[this.selParam2].selected = false
                else -> {
                    println("--Something went wrong--")
                    println("SelType: $selType")
                    println("SelParam1: $selParam1")
                    println("SelParam2: $selParam2")
                }
            }
        }
        this.selType = selType
        this.selParam1 = selParam1
        this.selParam2 = selParam2
        if (this.selType != -1) {
            when (selType) {
                PickingType.map -> ipls[selParam1].itemsInst[selParam2].selected = true
                PickingType.water -> waters[0].planes[selParam1].selected = true
                PickingType.car -> ipls[selParam1].itemsCars[selParam2].selected = true
                else -> {
                    println("--Something went wrong--")
                    println("SelType: $selType")
                    println("SelParam1: $selParam1")
                    println("SelParam2: $selParam2")
                }
            }
        }
    }

    fun loadWaterTexture(): TextureDic {
        TODO("Fix this")
//        return TextureDic("$gameDir/pc/textures/water.wtd", null, GameType.GTA_IV, 23655)
    }

    fun addNewImg(path: Path): CommandResult {
        if (!path.startsWith(gamePath)) return CommandResult.Failed("IMG should be inside the games folder")

        imgs.add(Img.createNewImg(path))

        return CommandResult.Success
    }

    override fun run() {
        startLoading()
    }
}

interface LoadingStatusCallbacks {
    fun onStartLoadingWpl(wplCount: Int)
    fun onStartLoading(fileCount: Int)
    fun onLoadingStatusChanged(status: String)
    fun onLoadingValueIncreased()
    fun onLoadingFinished()
}