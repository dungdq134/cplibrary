package pl.cyfrowypolsat.cpdata.api.navigation.request.media

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetMediaRelatedContentParams(val mediaId: String,
                                        val cpid: Int,
                                        val offset: Int = 0,
                                        val limit: Int = 20) : JsonRPCParams()