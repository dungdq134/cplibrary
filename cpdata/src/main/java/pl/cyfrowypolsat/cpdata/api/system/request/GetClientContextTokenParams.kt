package pl.cyfrowypolsat.cpdata.api.system.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId

data class GetClientContextTokenParams(private val deviceId: DeviceId) : JsonRPCParams()