package pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize

enum class SubtitleTextSizeType(val type: Int) {
    SUBTITLE_TEXT_SIZE_75_PERCENT(1),
    SUBTITLE_TEXT_SIZE_100_PERCENT(2),
    SUBTITLE_TEXT_SIZE_150_PERCENT(3),
    SUBTITLE_TEXT_SIZE_200_PERCENT(4);

    companion object {
        fun getFromType(type: Int?): SubtitleTextSizeType {
            return when(type) {
                SUBTITLE_TEXT_SIZE_75_PERCENT.type -> SUBTITLE_TEXT_SIZE_75_PERCENT
                SUBTITLE_TEXT_SIZE_150_PERCENT.type -> SUBTITLE_TEXT_SIZE_150_PERCENT
                SUBTITLE_TEXT_SIZE_200_PERCENT.type -> SUBTITLE_TEXT_SIZE_200_PERCENT
                else -> SUBTITLE_TEXT_SIZE_100_PERCENT
            }
        }
    }
}