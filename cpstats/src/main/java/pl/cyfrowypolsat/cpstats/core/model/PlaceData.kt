package pl.cyfrowypolsat.cpstats.core.model

data class PlaceData(val type: String,
                     val value: String? = null){
    companion object{
        val UNKNOWN = PlaceData("unknown")
    }
}