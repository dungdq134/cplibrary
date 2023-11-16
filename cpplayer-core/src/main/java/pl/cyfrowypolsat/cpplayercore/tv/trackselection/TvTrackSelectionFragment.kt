package pl.cyfrowypolsat.cpplayercore.tv.trackselection

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.media3.common.C
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.trackselection.TrackSelectionItem
import pl.cyfrowypolsat.cpplayercore.core.trackselection.TrackSelectionManager
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvFragmentTrackSelectionBinding
import pl.cyfrowypolsat.cpplayercore.tv.settings.TvSettingsItemViewOnKeyListener

class TvTrackSelectionFragment : Fragment(), View.OnClickListener {

    private val binding by viewBinding(CpplCrTvFragmentTrackSelectionBinding::bind)
    private lateinit var trackSelectionManager: TrackSelectionManager
    private lateinit var trackTypes: List<Int>
    private lateinit var itemViewOnKeyListener : TvSettingsItemViewOnKeyListener


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_tv_fragment_track_selection, container, false)


    fun init(trackSelectionManager: TrackSelectionManager, trackTypes: List<Int>) {
        this.trackSelectionManager = trackSelectionManager
        this.trackTypes = trackTypes
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemViewOnKeyListener = TvSettingsItemViewOnKeyListener(listOf(TvTrackSelectionItemView::class), binding.tvTrackSelectionLayout) {
            finishFragment()
        }

        trackTypes.forEach {
            when (it) {
                C.TRACK_TYPE_AUDIO -> setupAudioSelectionView(trackSelectionManager.getAudioSelectionItems())
                C.TRACK_TYPE_TEXT -> setupSubtitleSelectionView(trackSelectionManager.getSubtitleSelectionItems())
            }
        }
        binding.tvTrackSelectionOkButton.setOnKeyListener(itemViewOnKeyListener)
        binding.tvTrackSelectionOkButton.setOnClickListener { finishFragment() }
    }


    // Audio
    private fun setupAudioSelectionView(audioItems: List<TrackSelectionItem>) {
        binding.tvAudioSelectionView.visibility = View.VISIBLE
        val audioViewItems = mapToViewItems(audioItems)

        val selectedIndex = trackSelectionManager.findSelectedAudioItemIndex(audioItems) ?: 0
        Handler().post {
            if (!isAdded) return@post
            binding.tvAudioSelectionView.setup(getString(R.string.cppl_cr_audio), audioViewItems)
            audioItems.getOrNull(selectedIndex)?.let { binding.tvAudioSelectionView.setSelectedItem(it.id) }
            audioViewItems.getOrNull(selectedIndex)?.requestFocus()
        }
    }


    // Subtitle
    private fun setupSubtitleSelectionView(subtitleItems: List<TrackSelectionItem>) {
        binding.tvSubtitleSelectionView.visibility = View.VISIBLE
        val subtitleViewItems = mapToViewItems(subtitleItems)

        val selectedIndex = trackSelectionManager.findSelectedSubtitleItemIndex(subtitleItems) ?: 0
        Handler().post {
            if (!isAdded) return@post
            binding.tvSubtitleSelectionView.setup(getString(R.string.cppl_cr_subtitles), subtitleViewItems)
            subtitleItems.getOrNull(selectedIndex)?.let { binding.tvSubtitleSelectionView.setSelectedItem(it.id) }
            subtitleViewItems.getOrNull(selectedIndex)?.requestFocus()
        }
    }


    private fun mapToViewItems(items: List<TrackSelectionItem>): List<TvTrackSelectionItemView> {
        return items.map {
            val view = TvTrackSelectionItemView(requireActivity())
            view.setOnClickListener(this)
            view.setOnKeyListener(itemViewOnKeyListener)
            view.trackSelectionItem = it
            view.id = it.id
            view
        }
    }

    override fun onClick(v: View?) {
        if (v is TvTrackSelectionItemView) {
            val trackSelectionItem = v.trackSelectionItem ?: return
            trackSelectionManager.trackItemSelected(trackSelectionItem)
            when (trackSelectionItem.trackType) {
                C.TRACK_TYPE_AUDIO -> binding.tvAudioSelectionView.setSelectedItem(trackSelectionItem.id)
                C.TRACK_TYPE_TEXT -> binding.tvSubtitleSelectionView.setSelectedItem(trackSelectionItem.id)
            }
        }
    }

    private fun finishFragment() {
        parentFragmentManager.commit { remove(this@TvTrackSelectionFragment) }
    }

}