package pl.cyfrowypolsat.cpstats.core.model

data class SourceInfo(val id: String,
                      val accessMethod: String,
                      val fileFormat: String,
                      val drmType: String?)