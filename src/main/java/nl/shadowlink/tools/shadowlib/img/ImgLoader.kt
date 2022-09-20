package nl.shadowlink.tools.shadowlib.img

import java.nio.file.Path

interface ImgLoader {

    fun load(path: Path): Img
}