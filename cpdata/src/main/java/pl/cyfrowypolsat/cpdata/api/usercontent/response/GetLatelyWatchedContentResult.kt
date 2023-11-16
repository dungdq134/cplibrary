package pl.cyfrowypolsat.cpdata.api.usercontent.response

data class GetLatelyWatchedContentResult(val results: List<GetWatchedContentResult>,
                                         val offset: Int,
                                         val count: Int)