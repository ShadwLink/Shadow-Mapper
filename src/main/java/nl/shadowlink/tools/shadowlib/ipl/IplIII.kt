package nl.shadowlink.tools.shadowlib.ipl

import nl.shadowlink.tools.shadowlib.utils.Constants
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class IplIII {
    private var input: BufferedReader? = null
    private var fileReader: FileReader? = null
    private var readItem = -1

    fun loadPlacement(ipl: Ipl) {
        if (openIPL(ipl.fileName)) {
            try {
                var line: String? = null
                while (input!!.readLine().also { line = it } != null) {
                    if (readItem == -1) {
                        if (line!!.startsWith("#")) {
                        } else if (line!!.startsWith("inst")) {
                            readItem = Constants.pINST
                        } else if (line!!.startsWith("cull")) {
                            readItem = Constants.pCULL
                        } else if (line!!.startsWith("path")) {
                            readItem = Constants.pPATH
                        } else if (line!!.startsWith("grge")) {
                            readItem = Constants.pGRGE
                        } else if (line!!.startsWith("enex")) {
                            readItem = Constants.pENEX
                        } else if (line!!.startsWith("pick")) {
                            readItem = Constants.pPICK
                        } else if (line!!.startsWith("jump")) {
                            readItem = Constants.pJUMP
                        } else if (line!!.startsWith("tcyc")) {
                            readItem = Constants.pTCYC
                        } else if (line!!.startsWith("auzo")) {
                            readItem = Constants.pAUZO
                        } else if (line!!.startsWith("mult")) {
                            readItem = Constants.pMULT
                        } else if (line!!.startsWith("cars")) {
                            readItem = Constants.pCARS
                        } else if (line!!.startsWith("occl")) {
                            readItem = Constants.pOCCL
                        } else if (line!!.startsWith("zone")) {
                            readItem = Constants.pZONE
                        }
                    } else {
                        if (line!!.startsWith("#")) {
                            // Commented line
                        } else if (line!!.startsWith("end")) {
                            readItem = -1
                        } else {
                            var item: IplItem?
                            when (readItem) {
                                Constants.pINST -> {
                                    item = Item_INST(ipl.gameType)
                                    ipl.itemsInst.add(item)
                                }

                                Constants.pAUZO -> item = Item_AUZO(ipl.gameType)
                                Constants.pCARS -> item = Item_CARS(ipl.gameType)
                                Constants.pCULL -> item = Item_CULL(ipl.gameType)
                                Constants.pENEX -> item = Item_ENEX(ipl.gameType)
                                Constants.pJUMP -> item = Item_JUMP(ipl.gameType)
                                Constants.pGRGE -> item = Item_GRGE(ipl.gameType)
                                Constants.pMULT -> item = Item_MULT(ipl.gameType)
                                Constants.pOCCL -> item = Item_OCCL(ipl.gameType)
                                Constants.pPATH -> item = Item_PATH(ipl.gameType)
                                Constants.pPICK -> item = Item_PICK(ipl.gameType)
                                Constants.pTCYC -> item = Item_TCYC(ipl.gameType)
                                Constants.pZONE -> item = Item_ZONE(ipl.gameType)
                                else -> throw IllegalArgumentException("Unknown type $readItem")
                            }
                            item.read(line!!)
                        }
                    }
                }
            } catch (ex: IOException) {
                Logger.getLogger(IplIII::class.java.name).log(Level.SEVERE, null, ex)
            }
            closeIPL()
        } else {
            throw IllegalStateException("Unable to open IPL file")
        }
        ipl.loaded = true
    }

    fun openIPL(fileName: String?): Boolean {
        try {
            fileReader = FileReader(fileName)
            input = BufferedReader(fileReader)
        } catch (ex: IOException) {
            return false
        }
        return true
    }

    fun closeIPL(): Boolean {
        try {
            input!!.close()
            fileReader!!.close()
        } catch (ex: IOException) {
            return false
        }
        return true
    }

    fun save(ipl: Ipl?) {}
}