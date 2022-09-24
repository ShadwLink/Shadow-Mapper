package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.img.Img
import nl.shadowlink.tools.shadowlib.img.ImgItem
import nl.shadowlink.tools.shadowlib.utils.GameType
import nl.shadowlink.tools.shadowlib.utils.hashing.HashTable

/**
 * @author Shadow-Link
 */
class Ipl {
    var rf: ReadFunctions? = null
    var fileName = ""
    var gameType: GameType
    var changed = false // True when the file needs to be saved

    @JvmField
    var loaded = false

    @JvmField
    var selected = false
    var itemsLoaded = false
    var isStream = false // if it's a stream wpl
    var img: Img? = null // the img it's in
    var imgItem: ImgItem? = null // img item
    var lodWPL = -1
    val itemsAuzo = mutableListOf<Item_AUZO>()
    val itemsCars = mutableListOf<Item_CARS>()
    val itemsCull = mutableListOf<Item_CULL>()
    val itemsEnex = mutableListOf<Item_ENEX>()
    val itemsGrge = mutableListOf<Item_GRGE>()
    val itemsInst = mutableListOf<Item_INST>()
    val itemsJump = mutableListOf<Item_JUMP>()
    val itemsMult = mutableListOf<Item_MULT>()
    val itemsOccl = mutableListOf<Item_OCCL>()
    val itemsPath = mutableListOf<Item_PATH>()
    val itemsPick = mutableListOf<Item_PICK>()
    val itemsTCyc = mutableListOf<Item_TCYC>()
    val itemsStrBig = mutableListOf<Item_STRBIG>()
    val itemsLCul = mutableListOf<Item_LCUL>()
    val itemsZone = mutableListOf<Item_ZONE>()
    val itemsBlok = mutableListOf<Item_BLOK>()
    private var printName: String? = null

    constructor(fileName: String, hashTable: HashTable, gameType: GameType, autoLoad: Boolean) {
        this.fileName = fileName
        this.gameType = gameType
        println("Started loading: " + this.fileName)
        if (autoLoad) loadPlacement(hashTable)
    }

    constructor(
        rf: ReadFunctions?,
        hashTable: HashTable,
        gameType: GameType,
        autoLoad: Boolean,
        img: Img?,
        imgItem: ImgItem?,
        printName: String?
    ) {
        this.gameType = gameType
        this.rf = rf
        this.img = img
        this.imgItem = imgItem
        this.printName = printName
        if (autoLoad) loadPlacement(hashTable)
    }

    private fun loadPlacement(hashTable: HashTable) {
        when (gameType) {
            GameType.GTA_IV -> if (fileName.contains("common")) IplIII().loadPlacement(this) else IplIV(hashTable).loadPlacement(
                this,
                printName!!
            )

            else -> IplIII().loadPlacement(this)
        }
    }

    fun save(hashTable: HashTable) {
        when (gameType) {
            GameType.GTA_IV -> if (fileName.contains("common")) {
                IplIII().save(this)
            } else {
                IplIV(hashTable).save(this)
            }

            else -> IplIII().save(this)
        }
    }
}