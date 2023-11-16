package pl.cyfrowypolsat.cpdata.api.auth.request.connect

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams
import pl.cyfrowypolsat.cpdata.api.common.model.AuthProvider

data class ConnectParams(val connectionData: ConnectionData) : JsonRPCParams()

data class ConnectionData(val authProvider: AuthProvider,
                          val fbAccessToken: String? = null,
                          val login: String? = null,
                          val password: String? = null,
                          val msisdn: String? = null)
