package nl.shadowlink.tools.shadowlib.img

import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.Utils
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

/**
 * @author Shadow-Link
 */
class ImgV1 : ImgLoader {

    override fun load(path: Path): Img {
        val items = ArrayList<ImgItem>()

        val rf = ReadFunctions("${path.nameWithoutExtension}.dir")

        while (rf.moreToRead() != 0) {
            val itemOffset = rf.readInt() * 2048
            val itemSize = rf.readInt() * 2048
            val itemName = rf.readNullTerminatedString(24)
            val itemType = Utils.getFileType(itemName)
            val item = ImgItem(itemName)
            item.offset = itemOffset
            item.size = itemSize
            item.type = itemType
            items.add(item)
        }

        return Img(items = items, path = path)
    }
}
