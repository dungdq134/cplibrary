package pl.cyfrowypolsat.cpdata.api.usercontent.request

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class AddToFavoritesParams(private val favorite: Favorite): JsonRPCParams()

data class Favorite(private val type: String,
                    private val value: String)