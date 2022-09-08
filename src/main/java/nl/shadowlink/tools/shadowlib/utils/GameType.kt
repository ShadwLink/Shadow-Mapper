package nl.shadowlink.tools.shadowlib.utils

enum class GameType(
    val id: String,
    val gameName: String,
    val executableName: String,
) {
    GTA_III("III", "GTA III", "gta3.exe"),
    GTA_VC("VC", "GTA VC", "gta-vc.exe"),
    GTA_SA("SA", "GTA SA", "gta-sa.exe"),
    GTA_IV("IV", "GTA IV", "GTAIV.exe"),
    GTA_V("V", "GTA V", "GTA5.exe");

    companion object {
        fun byId(id: String): GameType? {
            return when (id) {
                "III" -> GTA_III
                "VC" -> GTA_VC
                "SA" -> GTA_SA
                "IV" -> GTA_IV
                "V" -> GTA_V
                else -> null
            }
        }
    }
}