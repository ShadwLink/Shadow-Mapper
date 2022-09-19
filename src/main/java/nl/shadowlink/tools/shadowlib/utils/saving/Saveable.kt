package nl.shadowlink.tools.shadowlib.utils.saving

interface Saveable {

    val isSaveRequired: Boolean

    fun setSaveRequired()

    fun setSaveRequired(isSaveRequired: Boolean)
}

class SaveableFile : Saveable {

    private var _isSaveRequired: Boolean = false
    override val isSaveRequired: Boolean
        get() = _isSaveRequired

    override fun setSaveRequired() {
        _isSaveRequired = true
    }

    override fun setSaveRequired(isSaveRequired: Boolean) {
        _isSaveRequired = isSaveRequired
    }
}
