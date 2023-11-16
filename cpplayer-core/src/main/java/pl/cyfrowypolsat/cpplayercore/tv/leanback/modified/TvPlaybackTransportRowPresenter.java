package pl.cyfrowypolsat.cpplayercore.tv.leanback.modified;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.PlaybackSeekDataProvider;
import androidx.leanback.widget.PlaybackSeekUi;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.ThumbsBar;

import androidx.media3.ui.DefaultTimeBar;

import java.util.Arrays;
import java.util.List;

import pl.cyfrowypolsat.cpcommon.domain.model.enums.MediaBadgeType;
import pl.cyfrowypolsat.cpcommon.presentation.ratingsystem.MediaBadgesView;
import pl.cyfrowypolsat.cpcommon.presentation.utils.AgeGroupImageUtilsKt;
import pl.cyfrowypolsat.cpplayercore.R;
import pl.cyfrowypolsat.cpplayercore.core.extensions.StringBuilderKt;
import pl.cyfrowypolsat.cpplayercore.tv.TvLeanbackPlayerAdapter;
import pl.cyfrowypolsat.cpplayercore.tv.leanback.unmodified.ControlBarPresenter;
import pl.cyfrowypolsat.cpplayercore.tv.leanback.unmodified.PlaybackTransportRowView;

/*
 * This is copy of androidx.leanback.widget.PlaybackTransportRowPresenter
 * with minor changes in order to adjust ui behaviour.
 */
public class TvPlaybackTransportRowPresenter extends PlaybackRowPresenter {

    static class BoundData extends ControlBarPresenter.BoundData {
        TvPlaybackTransportRowPresenter.ViewHolder mRowViewHolder;
    }

    /**
     * A ViewHolder for the PlaybackControlsRow supporting seek UI.
     */
    public class ViewHolder extends PlaybackRowPresenter.ViewHolder implements PlaybackSeekUi {

        final int SEEK_INTERVAL_MS = 15000;

        final ViewGroup mControlsDock;
        final ViewGroup mSecondaryControlsDock;
        final TextView mMediaTitle;
        final ImageView mAgeRestriction;
        final MediaBadgesView mMediaBadges;
        final TextView mTotalTime;
        final TextView mCurrentTime;
        final TextView mLiveCurrentTime;
        final TextView isAtLiveEdgeIndicator;
        final Button mWatchLiveButton;
        final ImageButton mBackButton;
        final DefaultTimeBar mTimeBar;
        final ThumbsBar mThumbsBar;
        long mTotalTimeInMs = Long.MIN_VALUE;
        long mCurrentTimeInMs = Long.MIN_VALUE;
        long mSecondaryProgressInMs;
        final StringBuilder mTempBuilder = new StringBuilder();
        ControlBarPresenter.ViewHolder mControlsVh;
        ControlBarPresenter.ViewHolder mSecondaryControlsVh;
        TvPlaybackTransportRowPresenter.BoundData mControlsBoundData = new TvPlaybackTransportRowPresenter.BoundData();
        TvPlaybackTransportRowPresenter.BoundData mSecondaryBoundData = new TvPlaybackTransportRowPresenter.BoundData();
        Presenter.ViewHolder mSelectedViewHolder;
        Object mSelectedItem;
        PlaybackControlsRow.PlayPauseAction mPlayPauseAction;
        int mThumbHeroIndex = -1;

        Client mSeekClient;
        boolean mInSeek;
        PlaybackSeekDataProvider mSeekDataProvider;
        long[] mPositions;
        int mPositionsLength;

        final PlaybackControlsRow.OnPlaybackProgressCallback mListener =
                new PlaybackControlsRow.OnPlaybackProgressCallback() {
                    @Override
                    public void onCurrentPositionChanged(PlaybackControlsRow row, long ms) {
                        setCurrentPosition(ms);
                    }

                    @Override
                    public void onDurationChanged(PlaybackControlsRow row, long ms) {
                        setTotalTime(ms);
                    }

                    @Override
                    public void onBufferedPositionChanged(PlaybackControlsRow row, long ms) {
                        setBufferedPosition(ms);
                    }
                };

        void updateProgressInSeek(boolean forward) {
            long newPos;
            long pos = mCurrentTimeInMs;
            if (mPositionsLength > 0) {
                int index = Arrays.binarySearch(mPositions, 0, mPositionsLength, pos);
                int thumbHeroIndex;
                if (forward) {
                    if (index >= 0) {
                        // found it, seek to neighbour key position at higher side
                        if (index < mPositionsLength - 1) {
                            newPos = mPositions[index + 1];
                            thumbHeroIndex = index + 1;
                        } else {
                            newPos = mTotalTimeInMs;
                            thumbHeroIndex = index;
                        }
                    } else {
                        // not found, seek to neighbour key position at higher side.
                        int insertIndex = -1 - index;
                        if (insertIndex <= mPositionsLength - 1) {
                            newPos = mPositions[insertIndex];
                            thumbHeroIndex = insertIndex;
                        } else {
                            newPos = mTotalTimeInMs;
                            thumbHeroIndex = insertIndex > 0 ? insertIndex - 1 : 0;
                        }
                    }
                } else {
                    if (index >= 0) {
                        // found it, seek to neighbour key position at lower side.
                        if (index > 0) {
                            newPos = mPositions[index - 1];
                            thumbHeroIndex = index - 1;
                        } else {
                            newPos = 0;
                            thumbHeroIndex = 0;
                        }
                    } else {
                        // not found, seek to neighbour key position at lower side.
                        int insertIndex = -1 - index;
                        if (insertIndex > 0) {
                            newPos = mPositions[insertIndex - 1];
                            thumbHeroIndex = insertIndex - 1;
                        } else {
                            newPos = 0;
                            thumbHeroIndex = 0;
                        }
                    }
                }
                updateThumbsInSeek(thumbHeroIndex, forward);
            } else {
                long interval = SEEK_INTERVAL_MS;
                newPos = pos + (forward ? interval : -interval);
                if (newPos > mTotalTimeInMs) {
                    newPos = mTotalTimeInMs;
                } else if (newPos < 0) {
                    newPos = 0;
                }
            }
            mTimeBar.setPosition(newPos);
            mSeekClient.onSeekPositionChanged(newPos);
        }

        void updateThumbsInSeek(int thumbHeroIndex, boolean forward) {
            if (mThumbHeroIndex == thumbHeroIndex) {
                return;
            }

            final int totalNum = mThumbsBar.getChildCount();
            if (totalNum < 0 || (totalNum & 1) == 0) {
                throw new RuntimeException();
            }
            final int heroChildIndex = totalNum / 2;
            final int start = Math.max(thumbHeroIndex - (totalNum / 2), 0);
            final int end = Math.min(thumbHeroIndex + (totalNum / 2), mPositionsLength - 1);
            final int newRequestStart;
            final int newRequestEnd;

            if (mThumbHeroIndex < 0) {
                // first time
                newRequestStart = start;
                newRequestEnd = end;
            } else {
                forward = thumbHeroIndex > mThumbHeroIndex;
                final int oldStart = Math.max(mThumbHeroIndex - (totalNum / 2), 0);
                final int oldEnd = Math.min(mThumbHeroIndex + (totalNum / 2),
                        mPositionsLength - 1);
                if (forward) {
                    newRequestStart = Math.max(oldEnd + 1, start);
                    newRequestEnd = end;
                    // overlapping area directly assign bitmap from previous result
                    for (int i = start; i <= newRequestStart - 1; i++) {
                        mThumbsBar.setThumbBitmap(heroChildIndex + (i - thumbHeroIndex),
                                mThumbsBar.getThumbBitmap(heroChildIndex + (i - mThumbHeroIndex)));
                    }
                } else {
                    newRequestEnd = Math.min(oldStart - 1, end);
                    newRequestStart = start;
                    // overlapping area directly assign bitmap from previous result in backward
                    for (int i = end; i >= newRequestEnd + 1; i--) {
                        mThumbsBar.setThumbBitmap(heroChildIndex + (i - thumbHeroIndex),
                                mThumbsBar.getThumbBitmap(heroChildIndex + (i - mThumbHeroIndex)));
                    }
                }
            }
            // processing new requests with mThumbHeroIndex updated
            mThumbHeroIndex = thumbHeroIndex;
            if (forward) {
                for (int i = newRequestStart; i <= newRequestEnd; i++) {
                    mSeekDataProvider.getThumbnail(i, mThumbResult);
                }
            } else {
                for (int i = newRequestEnd; i >= newRequestStart; i--) {
                    mSeekDataProvider.getThumbnail(i, mThumbResult);
                }
            }
            // set thumb bitmaps outside (start , end) to null
            for (int childIndex = 0; childIndex < heroChildIndex - mThumbHeroIndex + start;
                 childIndex++) {
                mThumbsBar.setThumbBitmap(childIndex, null);
            }
            for (int childIndex = heroChildIndex + end - mThumbHeroIndex + 1;
                 childIndex < totalNum; childIndex++) {
                mThumbsBar.setThumbBitmap(childIndex, null);
            }
        }

        PlaybackSeekDataProvider.ResultCallback mThumbResult =
                new PlaybackSeekDataProvider.ResultCallback() {
                    @Override
                    public void onThumbnailLoaded(Bitmap bitmap, int index) {
                        int childIndex = index - (mThumbHeroIndex - mThumbsBar.getChildCount() / 2);
                        if (childIndex < 0 || childIndex >= mThumbsBar.getChildCount()) {
                            return;
                        }
                        mThumbsBar.setThumbBitmap(childIndex, bitmap);
                    }
                };

        boolean onForward() {
            if (!startSeek()) {
                return false;
            }
            updateProgressInSeek(true);
            return true;
        }

        boolean onBackward() {
            if (!startSeek()) {
                return false;
            }
            updateProgressInSeek(false);
            return true;
        }

        /**
         * Constructor of ViewHolder of PlaybackTransportRowPresenter
         *
         * @param rootView Root view of the ViewHolder.
         */
        public ViewHolder(View rootView) {
            super(rootView);
            mMediaTitle = rootView.findViewById(R.id.media_title);
            mAgeRestriction = rootView.findViewById(R.id.age_restriction);
            mMediaBadges = rootView.findViewById(R.id.media_badges);
            mCurrentTime = (TextView) rootView.findViewById(R.id.current_time);
            mLiveCurrentTime = rootView.findViewById(R.id.live_current_time);
            isAtLiveEdgeIndicator = rootView.findViewById(R.id.live_is_at_live_edge_indicator);
            mWatchLiveButton = rootView.findViewById(R.id.live_watch_live_button);
            mBackButton = rootView.findViewById(R.id.back_button);
            mTotalTime = (TextView) rootView.findViewById(R.id.total_time);
            mTimeBar = (DefaultTimeBar) rootView.findViewById(R.id.playback_progress);

            if (mWatchLiveButton != null) {
                mWatchLiveButton.setOnClickListener(mWatchLiveButtonClickedListener);
            }

            if (mBackButton != null) {
                mBackButton.setOnClickListener(mBackButtonClickedListener);
            }

            mTimeBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onProgressBarClicked(TvPlaybackTransportRowPresenter.ViewHolder.this);
                }
            });
            mTimeBar.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    // when in seek only allow this keys
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_UP:
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            // eat DPAD UP/DOWN in seek mode
                            return mInSeek;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                        case KeyEvent.KEYCODE_MINUS:
                        case KeyEvent.KEYCODE_MEDIA_REWIND:
                            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                onBackward();
                            } else if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                                stopSeek(false);
                            }
                            return true;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                        case KeyEvent.KEYCODE_PLUS:
                        case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                onForward();
                            } else if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                                stopSeek(false);
                            }
                            return true;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (!mInSeek) {
                                return false;
                            }
                            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                                stopSeek(false);
                            }
                            return true;
                        case KeyEvent.KEYCODE_BACK:
                        case KeyEvent.KEYCODE_ESCAPE:
                            if (!mInSeek) {
                                return false;
                            }
                            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                                // SeekBar does not support cancel in accessibility mode, so always
                                // "confirm" if accessibility is on.
                                stopSeek(Build.VERSION.SDK_INT >= 21
                                        ? !mTimeBar.isAccessibilityFocused() : true);
                            }
                            return true;
                    }
                    return false;
                }
            });
            mControlsDock = (ViewGroup) rootView.findViewById(R.id.controls_dock);
            mSecondaryControlsDock =
                    (ViewGroup) rootView.findViewById(R.id.secondary_controls_dock);
            mThumbsBar = (ThumbsBar) rootView.findViewById(R.id.thumbs_row);
        }

        @Override
        public void setPlaybackSeekUiClient(Client client) {
            mSeekClient = client;
        }

        boolean startSeek() {
            if (mInSeek) {
                return true;
            }
            if (mSeekClient == null || !mSeekClient.isSeekEnabled()
                    || mTotalTimeInMs <= 0) {
                return false;
            }
            mInSeek = true;
            mSeekClient.onSeekStarted();
            mSeekDataProvider = mSeekClient.getPlaybackSeekDataProvider();
            mPositions = mSeekDataProvider != null ? mSeekDataProvider.getSeekPositions() : null;
            if (mPositions != null) {
                int pos = Arrays.binarySearch(mPositions, mTotalTimeInMs);
                if (pos >= 0) {
                    mPositionsLength = pos + 1;
                } else {
                    mPositionsLength = -1 - pos;
                }
            } else {
                mPositionsLength = 0;
            }

            /*
            mControlsVh.view.setVisibility(View.GONE);
            mSecondaryControlsVh.view.setVisibility(View.INVISIBLE);
            mThumbsBar.setVisibility(View.VISIBLE);
            */
            return true;
        }

        void stopSeek(boolean cancelled) {
            if (!mInSeek) {
                return;
            }
            mInSeek = false;
            mSeekClient.onSeekFinished(cancelled);
            if (mSeekDataProvider != null) {
                mSeekDataProvider.reset();
            }
            mThumbHeroIndex = -1;
            mThumbsBar.clearThumbBitmaps();
            mSeekDataProvider = null;
            mPositions = null;
            mPositionsLength = 0;

            /*
            mControlsVh.view.setVisibility(View.VISIBLE);
            mSecondaryControlsVh.view.setVisibility(View.VISIBLE);
            mThumbsBar.setVisibility(View.INVISIBLE);
            */
        }

        @SuppressWarnings("unchecked")
        void dispatchItemSelection() {
            if (!isSelected()) {
                return;
            }
            if (mSelectedViewHolder == null) {
                if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(null, null,
                            TvPlaybackTransportRowPresenter.ViewHolder.this, getRow());
                }
            } else {
                if (getOnItemViewSelectedListener() != null) {
                    getOnItemViewSelectedListener().onItemSelected(mSelectedViewHolder,
                            mSelectedItem, TvPlaybackTransportRowPresenter.ViewHolder.this, getRow());
                }
            }
        }


        Presenter getPresenter(boolean primary) {
            ObjectAdapter adapter = primary
                    ? ((PlaybackControlsRow) getRow()).getPrimaryActionsAdapter()
                    : ((PlaybackControlsRow) getRow()).getSecondaryActionsAdapter();
            if (adapter == null) {
                return null;
            }
            if (adapter.getPresenterSelector() instanceof TvControlButtonPresenterSelector) {
                TvControlButtonPresenterSelector selector = (TvControlButtonPresenterSelector) adapter.getPresenterSelector();
                if (primary) {
                    return selector.getPrimaryPresenter();
                } else {
                    return selector.getSecondaryPresenter();
                }
            }
            return adapter.getPresenter(adapter.size() > 0 ? adapter.get(0) : null);
        }

        protected void onSetTotalTime(long totalTime) {
            if (!mIsLive && mTotalTime != null) {
                mTotalTime.setText(StringBuilderKt.getPlayerTime(mTempBuilder, totalTime));
            }
            if (mTimeBar != null) {
                mTimeBar.setDuration(totalTime);
                mTimeBar.setAdGroupTimesMs(mPlayerAdapter.getAdGroupTimesMs(),
                        mPlayerAdapter.getPlayedAdGroups(), mPlayerAdapter.getAdGroupCount());
            }
        }

        void setTotalTime(long totalTimeMs) {
            if (mTotalTimeInMs != totalTimeMs) {
                mTotalTimeInMs = totalTimeMs;
                onSetTotalTime(mTotalTimeInMs);
                onSetCurrentPositionLabel(mCurrentTimeInMs);
                onSetWatchLiveButton();
            }
        }

        protected void onSetCurrentPositionLabel(long currentTimeMs) {
            if (mIsLive) {
                if (mIsAtLiveEdge) {
                    if (mLiveCurrentTime != null) {
                        mLiveCurrentTime.setVisibility(View.INVISIBLE);
                    }
                    if (isAtLiveEdgeIndicator != null) {
                        isAtLiveEdgeIndicator.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mLiveCurrentTime != null) {
                        long liveTime = mTotalTimeInMs - currentTimeMs;
                        mLiveCurrentTime.setText(StringBuilderKt.getLivePlayerTime(mTempBuilder, liveTime));
                        mLiveCurrentTime.setVisibility(View.VISIBLE);
                    }
                    if (isAtLiveEdgeIndicator != null) {
                        isAtLiveEdgeIndicator.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                if (mCurrentTime != null) {
                    mCurrentTime.setText(StringBuilderKt.getPlayerTime(mTempBuilder, currentTimeMs));
                }
            }
        }

        private void onSetWatchLiveButton() {
            if (mIsLive) {
                if (mIsAtLiveEdge) {
                    if (mWatchLiveButton != null) {
                        mWatchLiveButton.setVisibility(View.GONE);
                    }
                } else {
                    if (mWatchLiveButton != null) {
                        mWatchLiveButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        void setCurrentPosition(long currentTimeMs) {
            if (currentTimeMs != mCurrentTimeInMs) {
                mCurrentTimeInMs = currentTimeMs;
                onSetCurrentPositionLabel(currentTimeMs);
                onSetWatchLiveButton();
            }
            if (!mInSeek) {
                long currentPosition = 0;
                if (mTotalTimeInMs > 0) {
                    currentPosition = mCurrentTimeInMs;
                    double ratio = (double) mCurrentTimeInMs / mTotalTimeInMs;
                    if (ratio > 1) currentPosition = mTotalTimeInMs;
                }
                mTimeBar.setPosition(currentPosition);
            }
        }

        void setBufferedPosition(long progressMs) {
            mSecondaryProgressInMs = progressMs;
            // Solve the progress bar by using ratio
            double ratio = (double) progressMs / mTotalTimeInMs;
            if (ratio > 1) ratio = 1;
            double progressRatio = ratio * Integer.MAX_VALUE;   // Could safely cast to int
        }

    }

    boolean mIsLive;
    boolean mIsAtLiveEdge = true;
    String mMediaTitle = "";
    Integer mAgeRestriction = null;
    List<MediaBadgeType> mMediaBadges = null;
    int mControlsPaddingBottom;
    float mDefaultSeekIncrement = 0.01f;
    int mProgressColor = Color.TRANSPARENT;
    int mSecondaryProgressColor = Color.TRANSPARENT;
    boolean mProgressColorSet;
    boolean mSecondaryProgressColorSet;
    ControlBarPresenter mPlaybackControlsPresenter;
    ControlBarPresenter mSecondaryControlsPresenter;
    OnActionClickedListener mOnActionClickedListener;
    View.OnClickListener mWatchLiveButtonClickedListener;
    View.OnClickListener mBackButtonClickedListener;
    TvLeanbackPlayerAdapter mPlayerAdapter;

    View primaryActionsView;
    View secondaryActionsView;

    private final ControlBarPresenter.OnControlSelectedListener mOnControlSelectedListener =
            new ControlBarPresenter.OnControlSelectedListener() {
                @Override
                public void onControlSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                              ControlBarPresenter.BoundData data) {
                    TvPlaybackTransportRowPresenter.ViewHolder vh = ((TvPlaybackTransportRowPresenter.BoundData) data).mRowViewHolder;
                    if (vh.mSelectedViewHolder != itemViewHolder || vh.mSelectedItem != item) {
                        vh.mSelectedViewHolder = itemViewHolder;
                        vh.mSelectedItem = item;
                        vh.dispatchItemSelection();
                    }
                }
            };

    private final ControlBarPresenter.OnControlClickedListener mOnControlClickedListener =
            new ControlBarPresenter.OnControlClickedListener() {
                @Override
                @SuppressWarnings("unchecked")
                public void onControlClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                             ControlBarPresenter.BoundData data) {
                    TvPlaybackTransportRowPresenter.ViewHolder vh = ((TvPlaybackTransportRowPresenter.BoundData) data).mRowViewHolder;
                    if (vh.getOnItemViewClickedListener() != null) {
                        vh.getOnItemViewClickedListener().onItemClicked(itemViewHolder, item,
                                vh, vh.getRow());
                    }
                    if (mOnActionClickedListener != null && item instanceof Action) {
                        mOnActionClickedListener.onActionClicked((Action) item);
                    }
                }
            };

    public TvPlaybackTransportRowPresenter(boolean isLive, String mediaTitle, Integer ageRestriction,
                                           List<MediaBadgeType> mediaBadges,
                                           int controlsPaddingBottom, TvLeanbackPlayerAdapter playerAdapter) {
        mIsLive = isLive;
        mMediaTitle = mediaTitle;
        mAgeRestriction = ageRestriction;
        mMediaBadges = mediaBadges;
        mControlsPaddingBottom = controlsPaddingBottom;
        mPlayerAdapter = playerAdapter;
        setHeaderPresenter(null);
        setSelectEffectEnabled(false);

        mPlaybackControlsPresenter = new ControlBarPresenter(R.layout.lb_control_bar);
        mPlaybackControlsPresenter.setDefaultFocusToMiddle(false);
        mSecondaryControlsPresenter = new ControlBarPresenter(R.layout.lb_control_bar);
        mSecondaryControlsPresenter.setDefaultFocusToMiddle(false);

        mPlaybackControlsPresenter.setOnControlSelectedListener(mOnControlSelectedListener);
        mSecondaryControlsPresenter.setOnControlSelectedListener(mOnControlSelectedListener);
        mPlaybackControlsPresenter.setOnControlClickedListener(mOnControlClickedListener);
        mSecondaryControlsPresenter.setOnControlClickedListener(mOnControlClickedListener);
    }

    public void setWatchLiveButtonClickedListener(View.OnClickListener listener) {
        mWatchLiveButtonClickedListener = listener;
    }

    public void setBackButtonClickedListener(View.OnClickListener listener) {
        mBackButtonClickedListener = listener;
    }

    /**
     * Sets the listener for {@link Action} click events.
     */
    public void setOnActionClickedListener(OnActionClickedListener listener) {
        mOnActionClickedListener = listener;
    }

    /**
     * Returns the listener for {@link Action} click events.
     */
    public OnActionClickedListener getOnActionClickedListener() {
        return mOnActionClickedListener;
    }

    /**
     * Sets the primary color for the progress bar.  If not set, a default from
     * the theme will be used.
     */
    public void setProgressColor(@ColorInt int color) {
        mProgressColor = color;
        mProgressColorSet = true;
    }

    /**
     * Returns the primary color for the progress bar.  If no color was set, transparent
     * is returned.
     */
    @ColorInt
    public int getProgressColor() {
        return mProgressColor;
    }

    /**
     * Sets the secondary color for the progress bar.  If not set, a default from
     * the theme {@link R.attr#playbackProgressSecondaryColor} will be used.
     *
     * @param color Color used to draw secondary progress.
     */
    public void setSecondaryProgressColor(@ColorInt int color) {
        mSecondaryProgressColor = color;
        mSecondaryProgressColorSet = true;
    }

    /**
     * Returns the secondary color for the progress bar.  If no color was set, transparent
     * is returned.
     */
    @ColorInt
    public int getSecondaryProgressColor() {
        return mSecondaryProgressColor;
    }

    @Override
    public void onReappear(RowPresenter.ViewHolder rowViewHolder) {
        TvPlaybackTransportRowPresenter.ViewHolder vh = (TvPlaybackTransportRowPresenter.ViewHolder) rowViewHolder;
        if (vh.view.hasFocus()) {
            vh.mTimeBar.requestFocus();
        }
    }


    @Override
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        int layout = R.layout.cppl_cr_tv_playback_transport_controls_row;
        if (mIsLive) {
            layout = R.layout.cppl_cr_tv_playback_transport_controls_row_live;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        setRowLayoutParams(v);
        TvPlaybackTransportRowPresenter.ViewHolder vh = new TvPlaybackTransportRowPresenter.ViewHolder(v);
        initRow(vh, v.getContext());
        primaryActionsView = v.findViewById(R.id.controls_dock);
        secondaryActionsView = v.findViewById(R.id.secondary_controls_dock);
        return vh;
    }

    private void setRowLayoutParams(View view) {
        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = Math.min(size.y, size.x);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height - mControlsPaddingBottom);
        view.setLayoutParams(lp);
    }

    private void initRow(final TvPlaybackTransportRowPresenter.ViewHolder vh, Context context) {
        vh.mControlsVh = (ControlBarPresenter.ViewHolder) mPlaybackControlsPresenter
                .onCreateViewHolder(vh.mControlsDock);
        vh.mControlsDock.addView(vh.mControlsVh.view);

        vh.mSecondaryControlsVh = (ControlBarPresenter.ViewHolder) mSecondaryControlsPresenter
                .onCreateViewHolder(vh.mSecondaryControlsDock);
        vh.mSecondaryControlsDock.addView(vh.mSecondaryControlsVh.view);
        ((PlaybackTransportRowView) vh.view.findViewById(R.id.transport_row))
                .setOnUnhandledKeyListener(new PlaybackTransportRowView.OnUnhandledKeyListener() {
                    @Override
                    public boolean onUnhandledKey(KeyEvent event) {
                        if (vh.getOnKeyListener() != null) {
                            return vh.getOnKeyListener().onKey(vh.view, event.getKeyCode(), event);
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);

        TvPlaybackTransportRowPresenter.ViewHolder vh = (TvPlaybackTransportRowPresenter.ViewHolder) holder;
        PlaybackControlsRow row = (PlaybackControlsRow) vh.getRow();

        vh.mControlsBoundData.adapter = row.getPrimaryActionsAdapter();
        vh.mControlsBoundData.presenter = vh.getPresenter(true);
        vh.mControlsBoundData.mRowViewHolder = vh;
        mPlaybackControlsPresenter.onBindViewHolder(vh.mControlsVh, vh.mControlsBoundData);

        vh.mSecondaryBoundData.adapter = row.getSecondaryActionsAdapter();
        vh.mSecondaryBoundData.presenter = vh.getPresenter(false);
        vh.mSecondaryBoundData.mRowViewHolder = vh;
        mSecondaryControlsPresenter.onBindViewHolder(vh.mSecondaryControlsVh,
                vh.mSecondaryBoundData);

        vh.setTotalTime(row.getDuration());
        vh.setCurrentPosition(row.getCurrentPosition());
        vh.setBufferedPosition(row.getBufferedPosition());
        row.setOnPlaybackProgressChangedListener(vh.mListener);

        vh.mMediaTitle.setText(mMediaTitle);
        vh.mAgeRestriction.setImageDrawable(AgeGroupImageUtilsKt.getAgeGroupDrawable(vh.mAgeRestriction.getContext(), mAgeRestriction));
        vh.mMediaBadges.setMediaBadges(mMediaBadges);
    }

    public void setIsAtLiveEdge(boolean isAtLiveEdge) {
        mIsAtLiveEdge = isAtLiveEdge;
    }

    @Override
    protected void onUnbindRowViewHolder(RowPresenter.ViewHolder holder) {
        TvPlaybackTransportRowPresenter.ViewHolder vh = (TvPlaybackTransportRowPresenter.ViewHolder) holder;
        PlaybackControlsRow row = (PlaybackControlsRow) vh.getRow();

        mPlaybackControlsPresenter.onUnbindViewHolder(vh.mControlsVh);
        mSecondaryControlsPresenter.onUnbindViewHolder(vh.mSecondaryControlsVh);
        row.setOnPlaybackProgressChangedListener(null);

        super.onUnbindRowViewHolder(holder);
    }

    /**
     * Client of progress bar is clicked, default implementation delegate click to
     * PlayPauseAction.
     *
     * @param vh ViewHolder of PlaybackTransportRowPresenter
     */
    @SuppressWarnings("unchecked")
    protected void onProgressBarClicked(TvPlaybackTransportRowPresenter.ViewHolder vh) {
        if (vh != null) {
            if (vh.mPlayPauseAction == null) {
                vh.mPlayPauseAction = new PlaybackControlsRow.PlayPauseAction(vh.view.getContext());
            }
            if (vh.getOnItemViewClickedListener() != null) {
                vh.getOnItemViewClickedListener().onItemClicked(vh, vh.mPlayPauseAction,
                        vh, vh.getRow());
            }
            if (mOnActionClickedListener != null) {
                mOnActionClickedListener.onActionClicked(vh.mPlayPauseAction);
            }
        }
    }

    /**
     * Set default seek increment if {@link PlaybackSeekDataProvider} is null.
     *
     * @param ratio float value between 0(inclusive) and 1(inclusive).
     */
    public void setDefaultSeekIncrement(float ratio) {
        mDefaultSeekIncrement = ratio;
    }

    /**
     * Get default seek increment if {@link PlaybackSeekDataProvider} is null.
     *
     * @return float value between 0(inclusive) and 1(inclusive).
     */
    public float getDefaultSeekIncrement() {
        return mDefaultSeekIncrement;
    }

    @Override
    protected void onRowViewSelected(RowPresenter.ViewHolder vh, boolean selected) {
        super.onRowViewSelected(vh, selected);
        if (selected) {
            ((TvPlaybackTransportRowPresenter.ViewHolder) vh).dispatchItemSelection();
        }
    }

    public void focusPrimaryActions() {
        if (primaryActionsView != null) {
            primaryActionsView.requestFocus();
        }
    }

    public void focusSecondaryActions() {
        if (secondaryActionsView != null) {
            secondaryActionsView.requestFocus();
        }
    }

    public void updateMediaTitle(String mediaTitle) {
        mMediaTitle = mediaTitle;
    }

}
