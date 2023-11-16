package pl.cyfrowypolsat.cpcommon.presentation.ratingsystem

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import pl.cyfrowypolsat.cpcommon.databinding.CpcmnCommonViewMediaBadgesBinding
import pl.cyfrowypolsat.cpcommon.domain.model.enums.MediaBadgeType
import pl.cyfrowypolsat.cpcommon.presentation.extensions.adjustVisibleOrGone

class MediaBadgesView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = CpcmnCommonViewMediaBadgesBinding.inflate(LayoutInflater.from(context), this, true)

    fun setMediaBadges(mediaBadges: List<MediaBadgeType>?) {
        if (mediaBadges.isNullOrEmpty()) {
            visibility = View.GONE
            return
        }

        visibility = View.VISIBLE
        binding.underageClassificationViolence.adjustVisibleOrGone(mediaBadges.contains(MediaBadgeType.UNDERAGE_CLASSIFICATION_VIOLENCE))
        binding.underageClassificationDrugs.adjustVisibleOrGone(mediaBadges.contains(MediaBadgeType.UNDERAGE_CLASSIFICATION_DRUGS))
        binding.underageClassificationSex.adjustVisibleOrGone(mediaBadges.contains(MediaBadgeType.UNDERAGE_CLASSIFICATION_SEX))
        binding.underageClassificationVulgarLanguage.adjustVisibleOrGone(mediaBadges.contains(MediaBadgeType.UNDERAGE_CLASSIFICATION_VULGAR_LANGUAGE))

        binding.accessibilityFeatureAudioDescription.adjustVisibleOrGone(mediaBadges.contains(MediaBadgeType.ACCESSIBILITY_FEATURES_AUDIO_DESCRIPTION))
        binding.accessibilityFeatureSubtitles.adjustVisibleOrGone(mediaBadges.contains(MediaBadgeType.ACCESSIBILITY_FEATURES_SUBTITLES))
        binding.accessibilityFeatureSignLanguage.adjustVisibleOrGone(mediaBadges.contains(MediaBadgeType.ACCESSIBILITY_FEATURES_SIGN_LANGUAGE))
    }

}