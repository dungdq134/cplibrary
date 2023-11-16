package pl.cyfrowypolsat.cpplayercore.tv.settings

import android.os.Bundle
import android.os.Handler
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
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvFragmentSettingsBinding

class TvSettingsFragment : Fragment(), View.OnClickListener {

    private val binding by viewBinding(CpplCrTvFragmentSettingsBinding::bind)
    private lateinit var onSubtitleSizeSettingsItemClicked: (item: SubtitleSizeSettingsItem) -> Unit
    private lateinit var onSubtitleContrastSettingsItemClicked: () -> Unit
    private lateinit var subtitleSizeSettingsManager : SubtitleSizeSettingsManager
    private lateinit var subtitleContrastSettingsManager: SubtitleContrastSettingsManager
    private lateinit var itemViewOnKeyListener : TvSettingsItemViewOnKeyListener

    private val subtitleTextSizeTypes = listOf(SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_75_PERCENT,
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_100_PERCENT,
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_150_PERCENT,
            SubtitleTextSizeType.SUBTITLE_TEXT_SIZE_200_PERCENT)


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_tv_fragment_settings, container, false)


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
        itemViewOnKeyListener = TvSettingsItemViewOnKeyListener(
                itemViewClasses = listOf(TvSubtitleSizeSettingsItemView::class, TvSubtitleContrastSettingsItemView::class),
                itemViewContainer = binding.tvSubtitleSettingsLayout,
                onFinish = { finishFragment() })
        binding.tvSubtitleSettingsOkButton.setOnKeyListener(itemViewOnKeyListener)
        binding.tvSubtitleSettingsOkButton.setOnClickListener { finishFragment() }
        setupSubtitleSettingsView()
    }


    // Subtitle
    private fun setupSubtitleSettingsView() {
        val subtitleSizeItems = subtitleSizeSettingsManager.getSubtitleSizeSettingsItems(subtitleTextSizeTypes)
        val subtitleSizeViewItems = mapToSubtitleSizeViewItems(subtitleSizeItems)
        val subtitleSizeSelectedItem = subtitleSizeSettingsManager.findSelectedSubtitleSizeSettingsItem(subtitleSizeItems)

        val subtitleContrastItems = subtitleContrastSettingsManager.getSubtitleContrastSettingsItems()
        val subtitleContrastViewItems = mapToSubtitleContrastViewItems(subtitleContrastItems)

        Handler().post {
            if (!isAdded) return@post
            binding.tvSubtitleSettingsView.setup(subtitleSizeViewItems, subtitleContrastViewItems)
            subtitleSizeSelectedItem?.let {
                binding.tvSubtitleSettingsView.setSelectedItem(subtitleSizeSelectedItem.id)
                binding.tvSubtitleSettingsView.findViewById<TvSubtitleSizeSettingsItemView>(subtitleSizeSelectedItem.id).requestFocus()
            }
        }
    }


    private fun mapToSubtitleSizeViewItems(items: List<SubtitleSizeSettingsItem>): List<TvSubtitleSizeSettingsItemView> {
        return items.map {
            val view = TvSubtitleSizeSettingsItemView(requireActivity())
            view.setOnClickListener(this)
            view.setOnKeyListener(itemViewOnKeyListener)
            view.subtitleSizeSettingsItem = it
            view.id = it.id
            view
        }
    }

    private fun mapToSubtitleContrastViewItems(items: List<SubtitleContrastSettingsItem>): List<TvSubtitleContrastSettingsItemView> {
        return items.map {
            val view = TvSubtitleContrastSettingsItemView(requireActivity())
            view.setOnClickListener(this)
            view.setOnKeyListener(itemViewOnKeyListener)
            view.subtitleContrastSettingsItem = it
            view.id = it.id
            view
        }
    }

    override fun onClick(v: View?) {
        if (v is TvSubtitleSizeSettingsItemView) {
            val subtitleSettingsItem = v.subtitleSizeSettingsItem ?: return
            binding.tvSubtitleSettingsView.setSelectedItem(subtitleSettingsItem.id)
            subtitleSizeSettingsManager.subtitleSizeSettingsSelected(subtitleSettingsItem)
            onSubtitleSizeSettingsItemClicked(subtitleSettingsItem)
        } else if (v is TvSubtitleContrastSettingsItemView) {
            v.subtitleContrastSettingsItem?.let { it.selectionChanged() }
            val subtitleSettingsItem = v.subtitleContrastSettingsItem ?: return
            binding.tvSubtitleSettingsView.switchSelectionChanged(subtitleSettingsItem.id, subtitleSettingsItem.selected)
            subtitleContrastSettingsManager.subtitleContrastSettingsChanged(subtitleSettingsItem.selected)
            onSubtitleContrastSettingsItemClicked()
        }
    }

    private fun finishFragment() {
        parentFragmentManager.commit { remove(this@TvSettingsFragment) }
    }

}