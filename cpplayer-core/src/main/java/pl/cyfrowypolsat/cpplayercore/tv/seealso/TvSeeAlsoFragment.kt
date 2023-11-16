package pl.cyfrowypolsat.cpplayercore.tv.seealso

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import androidx.fragment.app.Fragment
import androidx.leanback.widget.*
import androidx.leanback.widget.BaseGridView.FOCUS_SCROLL_ITEM
import pl.cyfrowypolsat.cpcommon.core.extensions.dp
import pl.cyfrowypolsat.cpcommon.presentation.extensions.setPaddingRight
import pl.cyfrowypolsat.cpcommon.presentation.extensions.viewBinding
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.core.seealso.isMovie
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvFragmentSeeAlsoBinding
import pl.cyfrowypolsat.cpplayercore.tv.seealso.adapter.OnAutoPlayProgressEndListener
import pl.cyfrowypolsat.cpplayercore.tv.seealso.adapter.TvSeeAlsoCardView
import pl.cyfrowypolsat.cpplayercore.tv.seealso.adapter.TvSeeAlsoCardViewPresenter
import pl.cyfrowypolsat.cpplayercore.tv.seealso.adapter.TvSeeAlsoKeyListener
import kotlin.math.ceil

class TvSeeAlsoFragment : Fragment(), OnItemViewSelectedListener, OnAutoPlayProgressEndListener {

    companion object {
        private const val ITEM_VIEW_SELECTED_LISTENER_DELAY_IN_MILLIS = 200L
        private const val AUTOPLAY_ITEM_POSITION = 0
    }

    private val binding by viewBinding(CpplCrTvFragmentSeeAlsoBinding::bind)

    private lateinit var gridPresenter: VerticalGridPresenter
    private lateinit var gridViewHolder: VerticalGridPresenter.ViewHolder

    private lateinit var gridAdapter: ArrayObjectAdapter

    private lateinit var seeAlsoItemClickListener: (SeeAlsoItem) -> Unit
    private lateinit var seeAlsoItemAutoPlayListener: (SeeAlsoItem) -> Unit
    private lateinit var onFocusAboveContent: () -> Unit
    private lateinit var onFocusBelowContent: () -> Unit
    private lateinit var onCloseListener: () -> Unit
    private lateinit var seeAlsoItems: List<SeeAlsoItem>

    private val itemSelectedHandler = Handler()
    private var seeAlsoAutoPlayInitialized = false

    fun init(seeAlsoItems: List<SeeAlsoItem>,
             seeAlsoItemClickListener: (seeAlsoItem: SeeAlsoItem) -> Unit,
             seeAlsoItemAutoPlayListener: (seeAlsoItem: SeeAlsoItem) -> Unit,
             onFocusAboveContent: () -> Unit,
             onFocusBelowContent: () -> Unit,
             onCloseListener: () -> Unit) {
        this.seeAlsoItemClickListener = seeAlsoItemClickListener
        this.seeAlsoItemAutoPlayListener = seeAlsoItemAutoPlayListener
        this.seeAlsoItems = seeAlsoItems
        this.onFocusAboveContent = onFocusAboveContent
        this.onFocusBelowContent = onFocusBelowContent
        this.onCloseListener = onCloseListener
    }

    override fun onItemSelected(itemViewHolder: Presenter.ViewHolder,
                                item: Any?,
                                rowViewHolder: RowPresenter.ViewHolder?,
                                row: Row?) {
        itemSelectedHandler.removeCallbacksAndMessages(null)
        itemSelectedHandler.postDelayed({
            if (!isAdded) return@postDelayed
            if (item is SeeAlsoItem) {
                if (seeAlsoAutoPlayInitialized) {
                    stopSeeAlsoAutoPlay()
                }
                seeAlsoAutoPlayInitialized = true
                setGridItemKeyListener(itemViewHolder, item)
            }
        }, ITEM_VIEW_SELECTED_LISTENER_DELAY_IN_MILLIS)
    }

    override fun onAutoPlayProgressEnd(seeAlsoItem: SeeAlsoItem) {
        seeAlsoItemAutoPlayListener(seeAlsoItem)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.cppl_cr_tv_fragment_see_also, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGridHeight()
        setupGridPresenter()
        setupGridAdapter()
        setupGridItems()
    }

    override fun onDestroyView() {
        stopSeeAlsoAutoPlay()
        super.onDestroyView()
    }

    private fun setGridHeight() {
        if (seeAlsoItems.isNotEmpty()) {
            if (seeAlsoItems.isMovie()) {
                binding.tvSeeAlsoGridLayout.layoutParams.height = requireContext().resources.getDimensionPixelSize(R.dimen.cppl_cr_tv_see_also_list_poster_height)
            } else {
                binding.tvSeeAlsoGridLayout.layoutParams.height = requireContext().resources.getDimensionPixelSize(R.dimen.cppl_cr_tv_see_also_list_thumbnail_height)
            }
        }
    }

    private fun getNumberOfColumns(): Int {
        return if (seeAlsoItems.isNotEmpty()) {
            if (seeAlsoItems.isMovie()) {
                seeAlsoItems.size
            } else {
                ceil(seeAlsoItems.size.toDouble() / 2).toInt()
            }
        } else {
            0
        }
    }

    private fun setupGridPresenter() {
        gridPresenter = createVerticalGridPresenter()
        gridPresenter.numberOfColumns = getNumberOfColumns()
        gridViewHolder = gridPresenter.onCreateViewHolder(binding.tvSeeAlsoGridLayout)
        setGridAlignment(verticalGridView = gridViewHolder.view as VerticalGridView,
                verticalSpacing = requireActivity().resources.getDimensionPixelSize(R.dimen.cppl_cr_tv_see_also_item_vertical_spacing),
                windowAlignmentOffset = requireActivity().resources.getDimensionPixelOffset(R.dimen.cppl_cr_tv_see_also_window_alignment_offset))
        setupSmoothScrolling(gridViewHolder.view as VerticalGridView)
        binding.tvSeeAlsoGridLayout.addView(gridViewHolder.view)
    }

    private fun setupSmoothScrolling(verticalGridView: VerticalGridView) {
        verticalGridView.smoothScrollSpeedFactor = 10f
        verticalGridView.smoothScrollMaxPendingMoves = 0
        verticalGridView.smoothScrollByBehavior = object : BaseGridView.SmoothScrollByBehavior {
            override fun configSmoothScrollByInterpolator(dx: Int,
                                                          dy: Int): Interpolator? {
                return AccelerateInterpolator()
            }

            override fun configSmoothScrollByDuration(dx: Int,
                                                      dy: Int): Int {
                return 250
            }

        }
        verticalGridView.setHasFixedSize(true)
        verticalGridView.setItemViewCacheSize(20)
    }

    private fun setGridAlignment(verticalGridView: VerticalGridView,
                                 verticalSpacing: Int,
                                 windowAlignmentOffset: Int) {
        verticalGridView.verticalSpacing = verticalSpacing
        verticalGridView.itemAlignmentOffset = 0
        verticalGridView.itemAlignmentOffsetPercent = VerticalGridView.ITEM_ALIGN_OFFSET_PERCENT_DISABLED
        verticalGridView.isItemAlignmentOffsetWithPadding = true
        verticalGridView.windowAlignmentOffset = windowAlignmentOffset
        verticalGridView.windowAlignmentOffsetPercent = VerticalGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED
        verticalGridView.windowAlignment = VerticalGridView.WINDOW_ALIGN_NO_EDGE

        Handler(Looper.getMainLooper()).post { verticalGridView.focusScrollStrategy = FOCUS_SCROLL_ITEM }

        verticalGridView.setPaddingRight(20.dp)
    }

    private fun setupGridAdapter() {
        gridAdapter = ArrayObjectAdapter(createGridPresenter())
        gridPresenter.onBindViewHolder(gridViewHolder, gridAdapter)
    }

    private fun createGridPresenter(): Presenter {
        return TvSeeAlsoCardViewPresenter(requireActivity(), this, getItemWithAutoPlay())
    }

    private fun createVerticalGridPresenter(): VerticalGridPresenter {
        return VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM, false)
    }

    private fun getItemWithAutoPlay(): SeeAlsoItem? {
        return seeAlsoItems.getOrNull(AUTOPLAY_ITEM_POSITION)
    }

    private fun setupGridItems() {
        if (seeAlsoItems.isNullOrEmpty()) {
            onCloseListener()
        }
        gridAdapter.setItems(seeAlsoItems, object : DiffCallback<SeeAlsoItem>() {

            override fun areContentsTheSame(oldItem: SeeAlsoItem,
                                            newItem: SeeAlsoItem): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: SeeAlsoItem,
                                         newItem: SeeAlsoItem): Boolean {
                return oldItem.id == newItem.id
            }
        })
        gridPresenter.setOnItemViewClickedListener { _, item, _, _ ->
            if (item is SeeAlsoItem) {
                seeAlsoItemClickListener(item)
            }
        }
        gridPresenter.onItemViewSelectedListener = this
        gridViewHolder.gridView.requestFocus()
    }

    private fun setGridItemKeyListener(itemViewHolder: Presenter.ViewHolder, item: Any) {
        val selectedItemIndex = gridAdapter.indexOf(item)
        itemViewHolder.let {
            when {
                seeAlsoItems.size <= getNumberOfColumns() -> { // only one row of items
                    it.view.setOnKeyListener(TvSeeAlsoKeyListener.UpOrDownKeyListener({
                        stopSeeAlsoAutoPlay()
                        onFocusAboveContent()
                    }, {
                        stopSeeAlsoAutoPlay()
                        onFocusBelowContent()
                    }))
                }
                selectedItemIndex < getNumberOfColumns() -> { // item in first row
                    it.view.setOnKeyListener(TvSeeAlsoKeyListener.UpKeyListener {
                        stopSeeAlsoAutoPlay()
                        onFocusAboveContent()
                    })
                }
                selectedItemIndex >= ((seeAlsoItems.size - 1) / getNumberOfColumns()) * getNumberOfColumns() -> { // item is in last row
                    it.view.setOnKeyListener(TvSeeAlsoKeyListener.DownKeyListener {
                        stopSeeAlsoAutoPlay()
                        onFocusBelowContent()
                    })
                }
                else -> {
                    it.view.setOnKeyListener(TvSeeAlsoKeyListener.NonBlockingOnKeyListener())
                }
            }
        }
    }

    private fun stopSeeAlsoAutoPlay() {
        val seeAlsoCardView = gridViewHolder.gridView.getChildAt(AUTOPLAY_ITEM_POSITION)
        if (seeAlsoCardView is TvSeeAlsoCardView) {
            seeAlsoCardView.stopAutoPlayProgress()
        }
    }
}