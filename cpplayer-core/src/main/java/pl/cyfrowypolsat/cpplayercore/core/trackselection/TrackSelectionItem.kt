package pl.cyfrowypolsat.cpplayercore.core.trackselection

data class TrackSelectionItem(val name: String,
                              val trackType: Int,
                              val isDisabled: Boolean,
                              val rendererIndex: Int,
                              val groupIndex: Int,
                              val trackIndices: List<Int>,
                              val language: String? = null) {

    val id = hashCode() and 0xfffffff
}