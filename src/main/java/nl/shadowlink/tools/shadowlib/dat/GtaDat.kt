package nl.shadowlink.tools.shadowlib.dat

import java.io.*
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Shadow-Link
 */
class GtaDat(
    path: String,
    private val gameDir: String
) {
    private val fileName: String

    @JvmField
    var changed = false

    @JvmField
    val img = mutableListOf<String>()

    @JvmField
    val ipl = mutableListOf<String>()

    @JvmField
    val ide = mutableListOf<String>()
    val imgList = mutableListOf<String>()

    @JvmField
    val water = mutableListOf<String>()
    val colFile = mutableListOf<String>()
    val splash = mutableListOf<String>()

    init {
        fileName = path
        loadGtaDat()
    }

    private fun loadGtaDat() {
        val input = BufferedReader(FileReader(fileName))
        try {
            var line: String?
            while (input.readLine().also { line = it } != null) {
                if (!line!!.startsWith("#") && !line!!.isEmpty()) {
                    val split = line!!.split(" ").dropLastWhile { it.isEmpty() }
                    val filePath = split[1]
                        .replace("platform:", "pc")
                        .replace("common:", "common")
                        .replace("IPL", "WPL")
                    when (split[0]) {
                        "IMG" -> img.add(filePath)
                        "IDE" -> ide.add(filePath)
                        "IPL" -> ipl.add(filePath)
                        "IMGLIST" -> imgList.add(filePath)
                        "WATER" -> water.add(filePath)
                        "SPLASH" -> splash.add(filePath)
                        "COLFILE" -> colFile.add(filePath)
                    }
                }
            }
        } catch (ex: IOException) {
            Logger.getLogger(GtaDat::class.java.name).log(Level.SEVERE, null, ex)
        }
        input.close()
        loadImagesFromIMGLIST()
    }

    private fun loadImagesFromIMGLIST() {
        for (imgTexts in imgList.indices) {
            try {
                println("Loading img text: " + gameDir + imgList[imgTexts])
                val inputImgText = BufferedReader(FileReader(gameDir + imgList[imgTexts]))
                var line: String?
                while (inputImgText.readLine().also { line = it } != null) {
                    line?.let { imgLine ->
                        if (imgLine.startsWith("platformimg:")) {
                            imgLine.replace("platformimg:", "pc")
                                .replace("\t", "")
                                .also { img.add(it) }
                        }
                    }
                }
                inputImgText.close()
            } catch (ex: IOException) {
                Logger.getLogger(GtaDat::class.java.name).log(Level.SEVERE, null, ex)
            }
        }
    }

    fun save() {
        try {
            val output = BufferedWriter(FileWriter(fileName))
            with(output) {
                write("# gta.dat generated by Shadow-Mapper")
                newLine()
                newLine()
                write("#")
                newLine()
                write("# Imglist")
                newLine()
                write("#")
                newLine()
                newLine()
                for (i in imgList.indices) {
                    write(
                        "IMGLIST " + imgList[i]
                            .replaceFirst("pc", "platform:")
                            .replaceFirst("common", "common:")
                    )
                    newLine()
                }
                newLine()
                write("#")
                newLine()
                write("# Water")
                newLine()
                write("#")
                newLine()
                newLine()
                for (i in water.indices) {
                    write(
                        "WATER " + water[i]
                            .replaceFirst("pc", "platform:")
                            .replaceFirst("common", "common:")
                    )
                    newLine()
                }
                newLine()
                write("#")
                newLine()
                write("# Object types")
                newLine()
                write("#")
                newLine()
                newLine()
                for (i in ide.indices) {
                    write(
                        "IDE " + ide[i]
                            .replaceFirst("pc", "platform:")
                            .replaceFirst("common", "common:")
                    )
                    newLine()
                }
                newLine()
                write("#")
                newLine()
                write("# IPL")
                newLine()
                write("#")
                newLine()
                newLine()
                for (i in ipl.indices) {
                    write(
                        "IPL " + ipl[i]
                            .replaceFirst("pc", "platform:")
                            .replaceFirst("common", "common:")
                            .replace("wpl", "ipl")
                    )
                    newLine()
                }
                flush()
                close()
            }
        } catch (ex: IOException) {
            println("Error $ex")
        }
    }
}