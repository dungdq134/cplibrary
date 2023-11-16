package pl.cyfrowypolsat.cpstats.core.model

data class IpData(val ip: String,
                  val continent: String,
                  val country: String,
                  val isEu: Boolean,
                  val isVpn: Boolean,
                  val isp: String)