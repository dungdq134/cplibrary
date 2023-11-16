package pl.cyfrowypolsat.cpstats.core.model

enum class AdvertBlockType {
    PREROLL,
    MIDROLL,
    POSTROLL;

    companion object {
        fun fromIndex(blockIndex: Int): AdvertBlockType {
            return if (blockIndex == -1) {
                POSTROLL
            } else if (blockIndex == 0) {
                PREROLL
            } else {
                MIDROLL
            }
        }
    }
}