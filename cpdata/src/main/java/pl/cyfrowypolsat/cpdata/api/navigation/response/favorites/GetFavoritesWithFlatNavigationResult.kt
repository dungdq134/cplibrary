package pl.cyfrowypolsat.cpdata.api.navigation.response.favorites

import com.google.gson.annotations.SerializedName
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CpidObject
import pl.cyfrowypolsat.cpdata.api.navigation.response.common.FlatNavigationResult

data class GetFavoritesWithFlatNavigationResult(val offset: Int,
                                                val count: Int,
                                                val results: List<FavoriteResult>,
                                                val flatNavigation: FlatNavigationResult?)

data class FavoriteResult(val type: String,
                          val value: String,
                          @SerializedName("object") val favoriteObject: CpidObject?)