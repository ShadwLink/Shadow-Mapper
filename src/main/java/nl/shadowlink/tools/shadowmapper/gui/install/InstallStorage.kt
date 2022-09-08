package nl.shadowlink.tools.shadowmapper.gui.install

internal interface InstallStorage {

    fun getInstalls(): List<Install>

    fun addInstall(install: Install)

    fun removeInstall(install: Install)
}