package pl.cyfrowypolsat.cpstats.core.model

enum class OverlayType {
    TERAVOLT,
    UNKNOWN;

    companion object {
        fun getFromString(value: String): OverlayType {
            return when (value) {
                "teravolt" -> TERAVOLT
                else -> UNKNOWN
            }
        }
    }
}