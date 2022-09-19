package nl.shadowlink.tools.shadowmapper

sealed class CommandResult {
    object Success : CommandResult()
    data class Failed(
        val error: String
    ) : CommandResult()
}
