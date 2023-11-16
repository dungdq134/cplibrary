package pl.cyfrowypolsat.cpcommon.domain.model.enums

enum class RollOverEffectType(val type: String) {
    STANDARD("standard"),
    FADE("fade"),
    SLIDE("slide");

    companion object {
        val DEFAULT = STANDARD

        fun getFromString(value: String?): RollOverEffectType {
            return when (value) {
                STANDARD.type -> STANDARD
                FADE.type -> FADE
                SLIDE.type -> SLIDE
                else -> DEFAULT
            }
        }
    }
}