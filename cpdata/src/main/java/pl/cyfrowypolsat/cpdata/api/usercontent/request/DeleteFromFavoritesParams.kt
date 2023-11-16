package pl.cyfrowypolsat.cpdata.api.usercontent.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class DeleteFromFavoritesParams(private val favorite: Favorite): JsonRPCParams()