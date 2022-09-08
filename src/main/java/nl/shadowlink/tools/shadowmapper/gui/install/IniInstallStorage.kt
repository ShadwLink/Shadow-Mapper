package nl.shadowlink.tools.shadowmapper.gui.install

import com.nikhaldimann.inieditor.IniEditor
import nl.shadowlink.tools.shadowlib.utils.GameType
import java.io.File

internal class IniInstallStorage : InstallStorage {

    private val installsIni = IniEditor()

    init {
        if (settingsFile.exists()) {
            installsIni.load(settingsFile)
        }
    }

    override fun getInstalls(): List<Install> {
        return installsIni.sectionNames().mapNotNull { sectionName ->
            val installData = installsIni.getSectionMap(sectionName)
            val name = installData[INSTALL_NAME]
            val path = installData[INSTALL_PATH]
            val type = installData[INSTALL_TYPE]?.toGameType()

            if (name != null && path != null && type != null) {
                Install(id = sectionName, name = name, path = path, gameType = type)
            } else null
        }.toList()
    }

    override fun addInstall(install: Install) {
        with(installsIni) {
            addSection(install.id)
            set(install.id, INSTALL_NAME, install.name)
            set(install.id, INSTALL_PATH, install.path)
            set(install.id, INSTALL_TYPE, install.gameType.id)
        }
        installsIni.save(settingsFile)
    }

    override fun removeInstall(install: Install) {
        installsIni.removeSection(install.id)
        installsIni.save(settingsFile)
    }

    private companion object {
        private const val INSTALLS_INI = "installs.ini"
        private const val INSTALL_NAME = "name"
        private const val INSTALL_PATH = "path"
        private const val INSTALL_TYPE = "type"

        private val settingsFile = File(INSTALLS_INI)

        private fun String.toGameType(): GameType? = GameType.byId(this)
    }
}