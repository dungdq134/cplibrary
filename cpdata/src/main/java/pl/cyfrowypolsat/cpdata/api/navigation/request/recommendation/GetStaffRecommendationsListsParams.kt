package pl.cyfrowypolsat.cpdata.api.navigation.request.recommendation

import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCParams

data class GetStaffRecommendationsListsParams(val place: Place) : JsonRPCParams()