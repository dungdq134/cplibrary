package pl.cyfrowypolsat.cpcommon.domain.model.enums

enum class AppThemeType(val type: String) {
    LIGHT("light"),
    DARK("dark");

    companion object {
        val DEFAULT = DARK

        fun getFromString(value: String?): AppThemeType {
            return when (value) {
                LIGHT.type -> LIGHT
                DARK.type -> DARK
                else -> DEFAULT
            }
        }
        fun getFromBoolean(isDark: Boolean) = if (isDark) DARK else LIGHT
    }
}