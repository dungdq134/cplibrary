package pl.cyfrowypolsat.cpplayercore.mobile.trackselection

import android.os.Bundle
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
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileFragmentTrackSelectionBinding

class TrackSelectionFragment : Fragment() {

    private val binding by viewBinding(CpplCrMobileFragmentTrackSelectionBinding::bind)
    
    private lateinit var trackSelectionManager: TrackSelectionManager
    private lateinit var trackTypes: List<Int>


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_mobile_fragment_track_selection, container, false)


    fun init(trackSelectionManager: TrackSelectionManager, trackTypes: List<Int>) {
        this.trackSelectionManager = trackSelectionManager
        this.trackTypes = trackTypes
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trackSelectionCloseButton.setOnClickListener { finishFragment() }
        trackTypes.forEach {
            when (it) {
                C.TRACK_TYPE_AUDIO -> setupAudioSelectionView()
                C.TRACK_TYPE_TEXT -> setupSubtitleSelectionView()
            }
        }
    }

    private fun setupAudioSelectionView() {
        val audioItems = trackSelectionManager.getAudioSelectionItems()
        binding.trackSelectionAudioView.visibility = View.VISIBLE
        val audioViewItems = mapToViewItems(audioItems, binding.trackSelectionAudioView)
        val selectedIndex = trackSelectionManager.findSelectedAudioItemIndex(audioItems) ?: 0
        binding.trackSelectionAudioView.setup(getString(R.string.cppl_cr_audio), audioViewItems)
        audioItems.getOrNull(selectedIndex)?.let { binding.trackSelectionAudioView.setSelectedItem(it.id) }
    }

    private fun setupSubtitleSelectionView() {
        val subtitleItems = trackSelectionManager.getSubtitleSelectionItems()
        binding.trackSelectionSubtitleView.visibility = View.VISIBLE
        val subtitleViewItems = mapToViewItems(subtitleItems, binding.trackSelectionSubtitleView)
        val selectedIndex = trackSelectionManager.findSelectedSubtitleItemIndex(subtitleItems) ?: 0
        binding.trackSelectionSubtitleView.setup(getString(R.string.cppl_cr_subtitles), subtitleViewItems)
        subtitleItems.getOrNull(selectedIndex)?.let { binding.trackSelectionSubtitleView.setSelectedItem(it.id) }
    }

    private fun mapToViewItems(items: List<TrackSelectionItem>,
                               trackSelectionView: TrackSelectionView): List<TrackSelectionItemView> {
        return items.map {
            val view = TrackSelectionItemView(requireActivity())
            view.setOnClickListener {
                val trackSelectionItem = (view as? TrackSelectionItemView)?.trackSelectionItem
                        ?: return@setOnClickListener
                trackSelectionManager.trackItemSelected(trackSelectionItem)
                trackSelectionView.setSelectedItem(trackSelectionItem.id)
            }
            view.trackSelectionItem = it
            view.id = it.id
            view
        }
    }

    private fun finishFragment() {
        parentFragmentManager.commit { remove(this@TrackSelectionFragment) }
    }

}