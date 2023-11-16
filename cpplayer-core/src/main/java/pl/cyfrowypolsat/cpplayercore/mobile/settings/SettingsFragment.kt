package pl.cyfrowypolsat.cpplayercore.mobile.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.contrast.SubtitleContrastSettingsItem
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.contrast.SubtitleContrastSettingsManager
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize.SubtitleSizeSettingsItem
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize.SubtitleSizeSettingsManager
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize.SubtitleTextSizeType
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileFragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val binding by viewBinding(CpplCrMobileFragmentSettingsBinding::bind)
    private lateinit var onSubtitleSizeSettingsItemClicked: (item: SubtitleSizeSettingsItem) -> Unit
    private lateinit var onSubtitleContrastSettingsItemClicked: () -> Unit
    private lateinit var subtitleSizeSettingsManager : SubtitleSizeSettingsManager
    private lateinit var subtitleContrastSettingsManager: SubtitleContrastSettingsManager

    private val subtitleTextSizeTypes = listOf(SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_75_PERCENT,
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_100_PERCENT,
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_150_PERCENT,
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_200_PERCENT)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_mobile_fragment_settings, container, false)

    fun init(subtitleSizeSettingsManager: SubtitleSizeSettingsManager,
             subtitleContrastSettingsManager: SubtitleContrastSettingsManager,
             onSubtitleSizeSettingsItemClicked: (item: SubtitleSizeSettingsItem) -> Unit,
             onSubtitleContrastSettingsItemClicked: () -> Unit) {
        this.subtitleSizeSettingsManager = subtitleSizeSettingsManager
        this.subtitleContrastSettingsManager = subtitleContrastSettingsManager
        this.onSubtitleSizeSettingsItemClicked = onSubtitleSizeSettingsItemClicked
        this.onSubtitleContrastSettingsItemClicked = onSubtitleContrastSettingsItemClicked
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.subtitlesCloseButton.setOnClickListener { finishFragment() }
        setupSubtitleSettingsView()
    }

    private fun setupSubtitleSettingsView() {
        val subtitleSizeItems = subtitleSizeSettingsManager.getSubtitleSizeSettingsItems(subtitleTextSizeTypes)
        val subtitleSizeViewItems = mapToSubtitleSizeViewItems(subtitleSizeItems, binding.subtitlesSettingsView)
        val subtitleSizeSelectedItem = subtitleSizeSettingsManager.findSelectedSubtitleSizeSettingsItem(subtitleSizeItems)

        val subtitleContrastItems = subtitleContrastSettingsManager.getSubtitleContrastSettingsItems()
        val subtitleContrastViewItems = mapToSubtitleContrastViewItems(subtitleContrastItems, binding.subtitlesSettingsView)


        binding.subtitlesSettingsView.setup(subtitleSizeViewItems, subtitleContrastViewItems)

        subtitleSizeSelectedItem?.let { binding.subtitlesSettingsView.setSelectedItem(it.id) }
    }

    private fun mapToSubtitleSizeViewItems(items: List<SubtitleSizeSettingsItem>,
                                           SubtitleSettingsView: SubtitleSettingsView): List<SubtitleSizeSettingsItemView> {
        return items.map {
            val view = SubtitleSizeSettingsItemView(requireActivity())
            view.setOnClickListener {
                val subtitleSettingsItem = (view as? SubtitleSizeSettingsItemView)?.subtitleSizeSettingsItem
                        ?: return@setOnClickListener
                subtitleSizeSettingsManager.subtitleSizeSettingsSelected(subtitleSettingsItem)
                SubtitleSettingsView.setSelectedItem(subtitleSettingsItem.id)
                onSubtitleSizeSettingsItemClicked(subtitleSettingsItem)
            }
            view.subtitleSizeSettingsItem = it
            view.id = it.id
            view
        }
    }

    private fun mapToSubtitleContrastViewItems(items: List<SubtitleContrastSettingsItem>,
                                               SubtitleSettingsView: SubtitleSettingsView): List<SubtitleContrastSettingsItemView> {
        return items.map {
            val view = SubtitleContrastSettingsItemView(requireActivity())
            view.setOnClickListener {
                val subtitleSettingsItem = (view as? SubtitleContrastSettingsItemView)?.subtitleContrastSettingsItem
                        ?: return@setOnClickListener
                subtitleSettingsItem.selectionChanged()
                binding.subtitlesSettingsView.switchSelectionChanged(subtitleSettingsItem.id, subtitleSettingsItem.selected)
                subtitleContrastSettingsManager.subtitleContrastSettingsChanged(subtitleSettingsItem.selected)
                onSubtitleContrastSettingsItemClicked()
            }
            view.subtitleContrastSettingsItem = it
            view.id = it.id
            view
        }
    }

    private fun finishFragment() {
        parentFragmentManager.commit { remove(this@SettingsFragment) }
    }

}