package pl.cyfrowypolsat.cpstats.core.model

data class PlayerContext(val placeType: String,
                         val placeValue: String? = null){
    companion object{
        val UNKNOWN = PlayerContext("unknown")
    }
}