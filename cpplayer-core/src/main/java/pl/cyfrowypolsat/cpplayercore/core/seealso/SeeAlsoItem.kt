package pl.cyfrowypolsat.cpplayercore.core.seealso

import pl.cyfrowypolsat.cpcommon.domain.model.enums.MediaBadgeType
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSource

data class SeeAlsoItem(val id: String,
                       val type: SeeAlsoType,
                       val title: String,
                       val titlePosition: ListElementTitlePosition,
                       val ageGroup: Int?,
                       val mediaBadges: List<MediaBadgeType>?,
                       val posters: List<ImageSource>,
                       val thumbnails: List<ImageSource>)

fun List<SeeAlsoItem>.isMovie(): Boolean {
    return getOrNull(0)?.type == SeeAlsoType.MOVIE
}

enum class SeeAlsoType {
    CATEGORY,
    MOVIE,
    VOD
}

enum class ListElementTitlePosition {
    BELOW,
    HIDDEN,
    ON
}