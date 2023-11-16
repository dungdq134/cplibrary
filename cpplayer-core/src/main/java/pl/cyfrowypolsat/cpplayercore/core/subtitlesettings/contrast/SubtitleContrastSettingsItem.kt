package pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.contrast

data class SubtitleContrastSettingsItem(var selected: Boolean) {

    val id = hashCode() and 0xfffffff

    fun selectionChanged() { selected = !selected }
}