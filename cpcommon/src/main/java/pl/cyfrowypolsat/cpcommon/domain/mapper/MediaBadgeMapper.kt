package pl.cyfrowypolsat.cpcommon.domain.mapper

import pl.cyfrowypolsat.cpcommon.domain.model.enums.MediaBadgeType
import javax.inject.Inject

class MediaBadgeMapper
@Inject constructor() {

    fun map(underageClassification: List<String>? = listOf(),
            accessibilityFeatures: List<String>? = listOf()): List<MediaBadgeType> {
        val ucBadges = underageClassification?.mapNotNull {
            when (it) {
                "P" -> MediaBadgeType.UNDERAGE_CLASSIFICATION_VIOLENCE
                "N" -> MediaBadgeType.UNDERAGE_CLASSIFICATION_DRUGS
                "S" -> MediaBadgeType.UNDERAGE_CLASSIFICATION_SEX
                "W" -> MediaBadgeType.UNDERAGE_CLASSIFICATION_VULGAR_LANGUAGE
                else -> null
            }
        }

        val afBadges = accessibilityFeatures?.mapNotNull {
            when (it) {
                "AD" -> MediaBadgeType.ACCESSIBILITY_FEATURES_AUDIO_DESCRIPTION
                "N" -> MediaBadgeType.ACCESSIBILITY_FEATURES_SUBTITLES
                "JM" -> MediaBadgeType.ACCESSIBILITY_FEATURES_SIGN_LANGUAGE
                else -> null
            }
        }

        return ucBadges.orEmpty() + afBadges.orEmpty()
    }

}