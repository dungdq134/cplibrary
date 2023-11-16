package pl.cyfrowypolsat.cpdata.api.drm.response

//Reporting
data class Reporting(var redevents: RedEvents?)

data class RedEvents(val sellModel: String)