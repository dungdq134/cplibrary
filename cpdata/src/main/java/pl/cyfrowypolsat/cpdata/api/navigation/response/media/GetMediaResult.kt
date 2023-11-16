package pl.cyfrowypolsat.cpdata.api.navigation.response.media

import pl.cyfrowypolsat.cpdata.api.common.model.GalleryItemResult
import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.common.model.ImageResult
import pl.cyfrowypolsat.cpdata.api.common.model.ProductIdParam
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CategoryResult
import java.util.*

data class GetMediaResult(val cpid: Int,
                          val id: String,
                          val title: String,
                          val mediaType: String,
                          val description: String,
                          val genres: List<String>,
                          val images: List<ImageResult>?,
                          val thumbnails: List<ImageSourceResult>?,
                          val posters: List<ImageSourceResult>?,
                          val gallery: List<GalleryItemResult>?,
                          val ageGroup: Int,
                          val underageClassification: List<String>?,
                          val accessibilityFeatures: List<String>?,
                          val vote: String,
                          val episodeNumber: Int?,
                          val episodeTitle: String?,
                          val shortTitle: String?,
                          val category: CategoryResult?,
                          val product: ProductIdParam?,
                          val platforms: List<String>?,
                          val licenseEndDate: Date?,
                          val licenseLocationText: String?,
                          val cinematographers: List<String>?,
                          val composers: List<String>?,
                          val screenwriters: List<String>?,
                          val directors: List<String>?,
                          val countries: List<String>?,
                          val originalTitle: String?,
                          val releaseYear: String?,
                          val duration: Int?,
                          val cast: List<String>?,
                          val sound: String?,
                          val publicationDate: Date?,
                          val trailers: List<MediaIdResult>?,
                          val allowDownload: Boolean?,
                          val timeShiftingDuration: Long?)