package pl.cyfrowypolsat.cpdata.api.drm.response

data class GetUserPlaybackResult(val connection: ConnectionResult,
                                 val playerData: PlayerDataResult,
                                 val device: DeviceResult?)

data class ConnectionResult(val id: String,
                            val serverId: String)

data class PlayerDataResult(val licenseId: String)

data class DeviceResult(val name: String?)