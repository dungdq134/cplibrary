package pl.cyfrowypolsat.cpplayercore.core.trackselection

import androidx.media3.common.C
import androidx.media3.common.Format
import java.util.*

class TrackNameProvider {

    companion object {
        private const val LOCALE_PL = "pl"

        fun getAudioName(format: Format): String {
            return buildLabelOrLanguageString(format)
        }

        fun getSubtitleName(format: Format): String {
            return format.language ?: ""
        }

        private fun buildLabelOrLanguageString(format: Format): String {
            val label = format.label
            return if (label.isNullOrBlank()) {
                val language = buildLanguageString(format)
                if (language.isNullOrBlank()) {
                    ""
                } else {
                    language
                }
            } else {
                label
            }
        }

        private fun buildLanguageString(format: Format): String? {
            val language = format.language
            if (language.isNullOrBlank() || C.LANGUAGE_UNDETERMINED == language) {
                return ""
            }
            val locale = Locale.forLanguageTag(language)
            return locale.getDisplayLanguage(Locale(LOCALE_PL))
        }

    }
}