package pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize

data class SubtitleSizeSettingsItem(val name: String,
                                    val textSizeType: SubtitleTextSizeType,
                                    val textSize: Float) {

    val id = hashCode() and 0xfffffff
}