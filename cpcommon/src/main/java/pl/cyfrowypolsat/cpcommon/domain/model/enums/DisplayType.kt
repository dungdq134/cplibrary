package pl.cyfrowypolsat.cpcommon.domain.model.enums

enum class DisplayType(val type: String) {
    STANDARD("standard"),
    BACKGROUND("background"),
    ROLLOVER("rollOver"),
    HOVER("hover");

    companion object {
        val DEFAULT = STANDARD

        fun getFromString(value: String?): DisplayType {
            return when (value) {
                STANDARD.type -> STANDARD
                BACKGROUND.type -> BACKGROUND
                ROLLOVER.type -> ROLLOVER
                HOVER.type -> HOVER
                else -> DEFAULT
            }
        }
    }
}