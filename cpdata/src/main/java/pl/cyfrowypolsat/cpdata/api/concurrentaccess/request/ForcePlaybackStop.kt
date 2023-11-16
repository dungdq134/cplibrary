package pl.cyfrowypolsat.cpdata.api.concurrentaccess.request

data class ForcePlaybackStop(val fromSystem: String?,
                             val action: String,
                             val reason: String?)