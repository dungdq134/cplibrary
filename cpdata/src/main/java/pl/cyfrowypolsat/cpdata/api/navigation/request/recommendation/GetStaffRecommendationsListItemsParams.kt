package pl.cyfrowypolsat.cpdata.api.navigation.request.recommendation

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetStaffRecommendationsListItemsParams(val staffRecommendationsListId: String,
                                                  val place: Place,
                                                  val offset: Int = 0,
                                                  val limit: Int = 50) : JsonRPCParams()
