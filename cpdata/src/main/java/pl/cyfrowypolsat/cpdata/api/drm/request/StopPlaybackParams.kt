package pl.cyfrowypolsat.cpdata.api.drm.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class StopPlaybackParams(val licenseId: String,
                              val connection: ConnectionRequest) : JsonRPCParams()

data class ConnectionRequest(val id: String,
                             val serverId: String)
