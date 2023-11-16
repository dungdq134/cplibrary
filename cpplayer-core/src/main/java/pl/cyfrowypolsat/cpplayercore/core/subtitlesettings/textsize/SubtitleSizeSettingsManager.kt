package pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize

import android.content.Context
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.sharedprefs.PlayerSettingsSharedPrefs


class SubtitleSizeSettingsManager(context: Context,
                                  private val notResizedTextSize: Float) {

    private val sharedPrefs = PlayerSettingsSharedPrefs(context)
    private val subtitleTextSize75PercentName = context.getString(R.string.cppl_cr_subtitle_text_size_75_percent_name)
    private val subtitleTextSize100PercentName = context.getString(R.string.cppl_cr_subtitle_text_size_100_percent_name)
    private val subtitleTextSize150PercentName = context.getString(R.string.cppl_cr_subtitle_text_size_150_percent_name)
    private val subtitleTextSize200PercentName = context.getString(R.string.cppl_cr_subtitle_text_size_200_percent_name)

    fun getSubtitleSizeSettingsItems(subtitleTextSizeTypes: List<SubtitleTextSizeType>): List<SubtitleSizeSettingsItem> {
        return subtitleTextSizeTypes.map { getSubtitleSizeSettingsItem(it) }
    }

    private fun getSubtitleSizeSettingsItem(type: SubtitleTextSizeType): SubtitleSizeSettingsItem {
        return when (type) {
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_75_PERCENT -> SubtitleSizeSettingsItem(subtitleTextSize75PercentName, type, notResizedTextSize * 0.75f)
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_100_PERCENT -> SubtitleSizeSettingsItem(subtitleTextSize100PercentName, type, notResizedTextSize)
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_150_PERCENT -> SubtitleSizeSettingsItem(subtitleTextSize150PercentName, type, notResizedTextSize * 1.5f)
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_200_PERCENT -> SubtitleSizeSettingsItem(subtitleTextSize200PercentName, type, notResizedTextSize * 2f)
        }
    }

    fun subtitleSizeSettingsSelected(item: SubtitleSizeSettingsItem) {
        sharedPrefs.subtitleTextSizeType = item.textSizeType.type
    }

    fun findSelectedSubtitleSizeSettingsItem(items: List<SubtitleSizeSettingsItem>): SubtitleSizeSettingsItem? {
        return items.firstOrNull { it.textSizeType.type == sharedPrefs.subtitleTextSizeType }
                ?: items.firstOrNull { it.textSizeType == SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_100_PERCENT }
    }

    fun getSelectedSubtitleTextSize(): Float {
        val selectedTextSizeType = SubtitleTextSizeType.getFromType(sharedPrefs.subtitleTextSizeType)
        return getSubtitleSizeSettingsItem(selectedTextSizeType).textSize
    }
}