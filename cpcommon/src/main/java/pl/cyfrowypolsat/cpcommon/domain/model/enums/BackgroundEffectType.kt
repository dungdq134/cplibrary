package pl.cyfrowypolsat.cpcommon.domain.model.enums

enum class BackgroundEffectType(val type: String) {
    TRANSPARENT("transparent"),
    NOT_TRANSPARENT("not_transparent");

    companion object {
        val DEFAULT = NOT_TRANSPARENT;

        fun getFromString(value: String?): BackgroundEffectType {
            return when (value) {
                TRANSPARENT.type -> TRANSPARENT
                NOT_TRANSPARENT.type -> NOT_TRANSPARENT
                else -> DEFAULT
            }
        }
    }
}