package nl.shadowlink.tools.shadowlib.utils.encryption

import com.nikhaldimann.inieditor.IniEditor
import nl.shadowlink.tools.io.ReadFunctions
import nl.shadowlink.tools.shadowlib.utils.hashing.SHA1Hasher

class EncryptionKeyExtractor {

    private val versionData: List<Version>

    init {
        versionData = loadVersionData()
    }

    private fun loadVersionData(): List<Version> {
        return runCatching {
            val iniEditor = IniEditor()

            this::class.java.getResourceAsStream("/versions.ini")?.let {
                iniEditor.load(it)
            }

            return iniEditor.sectionNames().mapNotNull { sectionName ->
                iniEditor.getSectionMap(sectionName).run {
                    val name = get(INI_KEY_NAME)
                    val offset = get(INI_KEY_OFFSET)?.let { Integer.decode(it) }
                    if (name != null && offset != null) {
                        Version(name, offset)
                    } else {
                        null
                    }
                }
            }
        }.fold(
            onSuccess = { it },
            onFailure = { emptyList() }
        )
    }

    fun getKey(gameDir: String): ByteArray? {
        val rf = ReadFunctions(gameDir + "GTAIV.exe")
        val key = ByteArray(32)

        val version = versionData.firstOrNull { versionData ->
            rf.seek(versionData.offset)
            rf.readBytes(key)

            HASHED_KEY == SHA1Hasher.hash(key)
        }
        rf.closeFile()

        return if (version != null) key else null
    }

    data class Version(
        val name: String,
        val offset: Int
    )

    private companion object {
        private const val HASHED_KEY = "DEA375EF1E6EF2223A1221C2C575C47BF17EFA5E"

        private const val INI_KEY_NAME = "name"
        private const val INI_KEY_OFFSET = "offset"
    }
}