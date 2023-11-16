package pl.cyfrowypolsat.cpplayercore.tv

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.appcompat.content.res.AppCompatResources
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.*
import androidx.leanback.widget.PlaybackControlsRow.PlayPauseAction.INDEX_PAUSE
import androidx.leanback.widget.PlaybackControlsRow.PlayPauseAction.INDEX_PLAY
import pl.cyfrowypolsat.cpcommon.domain.model.enums.MediaBadgeType
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.tv.leanback.modified.TvControlButtonPresenterSelector
import pl.cyfrowypolsat.cpplayercore.tv.leanback.modified.TvPlaybackTransportRowPresenter

interface TvPlayerGlueClickedListener {
    fun actionClicked(action: Action)
    fun watchLiveClicked()
    fun backClicked()
}

class TvPlayerGlue constructor(private val activity: Activity,
                               private var mediaTitle: String,
                               private val ageGroup: Int? = null,
                               private val mediaBadges: List<MediaBadgeType>? = null,
                               private val isLive: Boolean,
                               private var primaryActions: List<Action>,
                               private var secondaryActions: List<Action>,
                               private val playerGlueClickedListener: TvPlayerGlueClickedListener,
                               private val playerControlsPaddingBottom: Int,
                               private val leanbackPlayerAdapter: TvLeanbackPlayerAdapter)
    : PlaybackTransportControlGlue<TvLeanbackPlayerAdapter>(activity, leanbackPlayerAdapter) {

    private var secondaryActionsAdapter: ArrayObjectAdapter? = null
    private var primaryActionsAdapter: ArrayObjectAdapter? = null
    private var tvPlaybackTransportRowPresenter: TvPlaybackTransportRowPresenter? = null

    init {
        isSeekEnabled = true
        enableSmoothSeeking()
    }

    private fun enableSmoothSeeking() {
        seekProvider = object : PlaybackSeekDataProvider() {}
    }

    fun updateSecondaryActions(newActions: List<Action>) {
        secondaryActionsAdapter?.setItems(newActions, null)
        secondaryActions = newActions
    }

    fun updatePrimaryActions(newActions: List<Action>) {
        // Remove actions
        for (action in primaryActions) {
            if (!newActions.contains(action)) {
                primaryActionsAdapter?.remove(action)
            }
        }

        // Add new actions
        for (action in newActions) {
            if (!primaryActions.contains(action)) {
                primaryActionsAdapter?.add(action)
            }
        }

        primaryActions = newActions
    }

    fun setIsAtLiveEdge(isAtLiveEdge: Boolean) {
        tvPlaybackTransportRowPresenter?.setIsAtLiveEdge(isAtLiveEdge)
        onMetadataChanged()
    }

    fun updateMediaTitle(mediaTitle: String) {
        if (this.mediaTitle == mediaTitle) return
        this.mediaTitle = mediaTitle
        tvPlaybackTransportRowPresenter?.updateMediaTitle(mediaTitle)
        if (isPlaying) {
            onMetadataChanged()
        }
    }

    override fun onCreateRowPresenter(): PlaybackRowPresenter? {
        tvPlaybackTransportRowPresenter = object : TvPlaybackTransportRowPresenter(isLive, mediaTitle,
                ageGroup, mediaBadges, playerControlsPaddingBottom, leanbackPlayerAdapter) {
            override fun onBindRowViewHolder(vh: RowPresenter.ViewHolder,
                                             item: Any) {
                super.onBindRowViewHolder(vh, item)
                vh.onKeyListener = this@TvPlayerGlue
            }

            override fun onUnbindRowViewHolder(vh: RowPresenter.ViewHolder) {
                super.onUnbindRowViewHolder(vh)
                vh.onKeyListener = null
            }
        }
        tvPlaybackTransportRowPresenter?.setWatchLiveButtonClickedListener {
            playerGlueClickedListener.watchLiveClicked()
        }
        tvPlaybackTransportRowPresenter?.setBackButtonClickedListener {
            playerGlueClickedListener.backClicked()
        }
        return tvPlaybackTransportRowPresenter
    }

    override fun onCreatePrimaryActions(adapter: ArrayObjectAdapter) {
        super.onCreatePrimaryActions(adapter)
        primaryActions.forEach { adapter.add(it) }
        primaryActionsAdapter = adapter
    }

    fun changePlayPauseIcons(playbackEnded: Boolean) {
        if (primaryActionsAdapter?.get(0) is PlaybackControlsRow.PlayPauseAction) {
            val playPauseAction = primaryActionsAdapter?.get(0) as PlaybackControlsRow.MultiAction
            Handler(Looper.getMainLooper()).post {
                if (playbackEnded) {
                    playPauseAction.setDrawables(arrayOf(
                            AppCompatResources.getDrawable(context, R.drawable.cppl_cr_common_ic_start_over),
                            AppCompatResources.getDrawable(context, R.drawable.cppl_cr_common_ic_pause)))
                } else {
                    playPauseAction.setDrawables(arrayOf(
                            AppCompatResources.getDrawable(context, R.drawable.cppl_cr_common_ic_play),
                            AppCompatResources.getDrawable(context, R.drawable.cppl_cr_common_ic_pause)))
                }
                playPauseAction.index = if (isPlaying) {
                    INDEX_PAUSE
                } else {
                    INDEX_PLAY
                }
                primaryActionsAdapter?.notifyItemRangeChanged(0, 1)
            }
        }
    }

    fun focusPrimaryActions() {
        tvPlaybackTransportRowPresenter?.focusPrimaryActions()
    }

    fun focusSecondaryActions() {
        tvPlaybackTransportRowPresenter?.focusSecondaryActions()
    }

    override fun onCreateSecondaryActions(adapter: ArrayObjectAdapter) {
        super.onCreateSecondaryActions(adapter)
        secondaryActions.forEach { adapter.add(it) }
        secondaryActionsAdapter = adapter
    }

    override fun onActionClicked(action: Action?) {
        action?.let {
            if (primaryActions.contains(it) || secondaryActions.contains(it)) {
                playerGlueClickedListener.actionClicked(it)
                return
            }
        }
        super.onActionClicked(action)
    }

    override fun setControlsRow(controlsRow: PlaybackControlsRow?) {
        super.setControlsRow(controlsRow)
        if (controlsRow?.primaryActionsAdapter?.presenterSelector !is TvControlButtonPresenterSelector) {
            val adapter = ArrayObjectAdapter(TvControlButtonPresenterSelector())
            onCreatePrimaryActions(adapter)
            controlsRow?.primaryActionsAdapter = adapter
        }
        if (controlsRow?.secondaryActionsAdapter?.presenterSelector !is TvControlButtonPresenterSelector) {
            val adapter = ArrayObjectAdapter(TvControlButtonPresenterSelector())
            onCreateSecondaryActions(adapter)
            controlsRow?.secondaryActionsAdapter = adapter
        }
    }

}