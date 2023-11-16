package pl.cyfrowypolsat.cpplayercore.mobile.seealso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.core.seealso.isMovie
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileFragmentSeeAlsoBinding
import pl.cyfrowypolsat.cpplayercore.mobile.seealso.adapter.BaseSeeAlsoItemViewHolder
import pl.cyfrowypolsat.cpplayercore.mobile.seealso.adapter.SeeAlsoCollectionAdapter
import pl.cyfrowypolsat.cpplayercore.mobile.seealso.adapter.SeeAlsoItemClickListener

class SeeAlsoFragment : Fragment() {

    companion object {
        private const val AUTOPLAY_ITEM_POSITION = 0
    }

    private val binding by viewBinding(CpplCrMobileFragmentSeeAlsoBinding::bind)
    
    private var seeAlsoCollectionAdapter: SeeAlsoCollectionAdapter? = null

    private lateinit var seeAlsoItemClickListener: SeeAlsoItemClickListener
    private var seeAlsoItems: List<SeeAlsoItem>? = null

    fun init(seeAlsoItems: List<SeeAlsoItem>,
             listener: SeeAlsoItemClickListener) {
        this.seeAlsoItems = seeAlsoItems
        this.seeAlsoItemClickListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_mobile_fragment_see_also, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onStop() {
        stopAutoPlay()
        super.onStop()
    }

    private fun setupRecyclerView() {
        val seeAlsoItems = this.seeAlsoItems ?: return
        if (seeAlsoCollectionAdapter == null) {
            seeAlsoCollectionAdapter = SeeAlsoCollectionAdapter(showPosters = seeAlsoItems.isMovie(),
                    seeAlsoItemClickListener = seeAlsoItemClickListener,
                    autoPlayItemPosition = AUTOPLAY_ITEM_POSITION)
        }
        binding.seeAlsoRecyclerView.adapter = seeAlsoCollectionAdapter

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        linearLayoutManager.isSmoothScrollbarEnabled = false
        linearLayoutManager.isItemPrefetchEnabled = true
        linearLayoutManager.initialPrefetchItemCount = 5

        binding.seeAlsoRecyclerView.layoutManager = linearLayoutManager
        binding.seeAlsoRecyclerView.setItemViewCacheSize(20)

        binding.seeAlsoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                stopAutoPlay()
            }
        })

        seeAlsoCollectionAdapter?.seeAlsoItems = seeAlsoItems.toMutableList()
    }

    private fun stopAutoPlay() {
        val firstChild = binding.seeAlsoRecyclerView.findViewHolderForAdapterPosition(AUTOPLAY_ITEM_POSITION)
        if (firstChild is BaseSeeAlsoItemViewHolder) {
            firstChild.hideAutoPlayProgress()
        }
    }

}