package nl.shadowlink.tools.shadowlib.img

import java.io.File

interface ImgLoader {

    fun load(file: File): Img
}