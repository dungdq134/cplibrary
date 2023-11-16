package pl.cyfrowypolsat.cpcommon.domain.model.enums

enum class DisplayPlaceType(val type: String) {
    STANDARD("standard"),
    BACKGROUND("background");

    companion object {
        val DEFAULT = STANDARD

        fun getFromString(value: String?): DisplayPlaceType {
            return when (value) {
                BACKGROUND.type -> BACKGROUND
                STANDARD.type -> STANDARD
                else -> DEFAULT
            }
        }
    }
}