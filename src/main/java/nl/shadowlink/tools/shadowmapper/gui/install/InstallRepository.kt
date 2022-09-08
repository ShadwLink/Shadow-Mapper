package nl.shadowlink.tools.shadowmapper.gui.install

import nl.shadowlink.tools.shadowlib.utils.GameType

internal class InstallRepository(
    private val installStorage: IniInstallStorage
) {

    private var installListener: ((installs: List<Install>) -> Unit)? = null
    private var installs: MutableList<Install> = mutableListOf()

    fun observeInstalls(listener: (installs: List<Install>) -> Unit) {
        installListener = listener
        loadInstalls()
    }

    private fun loadInstalls() {
        installs = installStorage.getInstalls().toMutableList()
        installListener?.invoke(installs)
    }

    fun getInstall(index: Int): Install? {
        return installs.getOrNull(index)
    }

    fun addInstall(name: String, path: String, type: GameType) {
        addInstall(Install(name.makeSafe(), name, path, type))
    }

    private fun addInstall(install: Install) {
        installs.add(install)
        installListener?.invoke(installs)
        installStorage.addInstall(install)
    }

    fun removeInstall(install: Install) {
        installs.remove(install)
        installListener?.invoke(installs)
        installStorage.removeInstall(install)
    }

    private companion object {

        private val illegalChars = arrayOf(" ", "[", "]")

        private fun String.makeSafe(): String {
            var value = this
            illegalChars.forEach { char -> value = value.replace(char, "") }
            return value.lowercase()
        }
    }
}
