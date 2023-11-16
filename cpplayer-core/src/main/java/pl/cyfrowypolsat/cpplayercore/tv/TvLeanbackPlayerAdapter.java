package pl.cyfrowypolsat.cpplayercore.tv;

import android.content.Context;
import android.os.Handler;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.Nullable;
import androidx.leanback.media.PlaybackGlueHost;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.media.SurfaceHolderGlueHost;

import androidx.media3.common.C;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.Timeline;
import androidx.media3.common.ErrorMessageProvider;
import androidx.media3.common.util.Util;
import androidx.media3.common.VideoSize;

import java.util.Arrays;

import pl.cyfrowypolsat.cpplayercore.R;
import pl.cyfrowypolsat.cpplayercore.core.extensions.PlayerKt;



/** This class is copy of com.google.android.exoplayer2.ext.leanback.LeanbackPlayerAdapter with following changes:
 * 1. overwritten getDuration method
 * 2. updateAdBreakData() in onTimelineChanged()
 * */
public final class TvLeanbackPlayerAdapter extends PlayerAdapter implements Runnable {

    private final Context context;
    private final Player player;
    private final Handler handler;
    private final PlayerListener playerListener;
    private final int updatePeriodMs;

    @Nullable private ErrorMessageProvider<? super PlaybackException> errorMessageProvider;
    @Nullable private SurfaceHolderGlueHost surfaceHolderGlueHost;
    private boolean hasSurface;
    private boolean lastNotifiedPreparedState;

    /**
     * Builds an instance. Note that the {@code PlayerAdapter} does not manage the lifecycle of the
     * {@link Player} instance. The caller remains responsible for releasing the player when it's no
     * longer required.
     *
     * @param context The current {@link Context} (activity).
     * @param player The {@link Player} being used.
     * @param updatePeriodMs The delay between player control updates, in milliseconds.
     */
    public TvLeanbackPlayerAdapter(Context context, Player player, final int updatePeriodMs) {
        this.context = context;
        this.player = player;
        this.updatePeriodMs = updatePeriodMs;
        handler = Util.createHandlerForCurrentOrMainLooper();
        playerListener = new PlayerListener();
    }

    /**
     * Sets the optional {@link ErrorMessageProvider}.
     *
     * @param errorMessageProvider The {@link ErrorMessageProvider}.
     */
    public void setErrorMessageProvider(
            @Nullable ErrorMessageProvider<? super PlaybackException> errorMessageProvider) {
        this.errorMessageProvider = errorMessageProvider;
    }

    // PlayerAdapter implementation.

    @Override
    public void onAttachedToHost(PlaybackGlueHost host) {
        if (host instanceof SurfaceHolderGlueHost) {
            surfaceHolderGlueHost = ((SurfaceHolderGlueHost) host);
            surfaceHolderGlueHost.setSurfaceHolderCallback(playerListener);
        }
        notifyStateChanged();
        player.addListener(playerListener);
    }

    @Override
    public void onDetachedFromHost() {
        player.removeListener(playerListener);
        if (surfaceHolderGlueHost != null) {
            removeSurfaceHolderCallback(surfaceHolderGlueHost);
            surfaceHolderGlueHost = null;
        }
        hasSurface = false;
        Callback callback = getCallback();
        callback.onBufferingStateChanged(this, false);
        callback.onPlayStateChanged(this);
        maybeNotifyPreparedStateChanged(callback);
    }

    @Override
    public void setProgressUpdatingEnabled(boolean enabled) {
        handler.removeCallbacks(this);
        if (enabled) {
            handler.post(this);
        }
    }

    @Override
    public boolean isPlaying() {
        int playbackState = player.getPlaybackState();
        return playbackState != Player.STATE_IDLE
                && playbackState != Player.STATE_ENDED
                && player.getPlayWhenReady();
    }

    @Override
    public long getDuration() {
        return PlayerKt.getDisplayedDuration(player);
    }

    @Override
    public long getCurrentPosition() {
        return player.getPlaybackState() == Player.STATE_IDLE ? -1 : player.getCurrentPosition();
    }

    @Override
    public void play() {
        if (player.getPlaybackState() == Player.STATE_IDLE) {
            player.prepare();
        } else if (player.getPlaybackState() == Player.STATE_ENDED) {
            player.seekToDefaultPosition(player.getCurrentMediaItemIndex());
        }
        if (player.isCommandAvailable(Player.COMMAND_PLAY_PAUSE)) {
            player.play();
            getCallback().onPlayStateChanged(this);
        }
    }

    @Override
    public void pause() {
        if (player.isCommandAvailable(Player.COMMAND_PLAY_PAUSE)) {
            player.pause();
            getCallback().onPlayStateChanged(this);
        }
    }

    @Override
    public void seekTo(long positionMs) {
        player.seekTo(player.getCurrentMediaItemIndex(), positionMs);
    }

    @Override
    public long getBufferedPosition() {
        return player.getBufferedPosition();
    }

    @Override
    public boolean isPrepared() {
        return player.getPlaybackState() != Player.STATE_IDLE
                && (surfaceHolderGlueHost == null || hasSurface);
    }

    // Runnable implementation.

    @Override
    public void run() {
        Callback callback = getCallback();
        callback.onCurrentPositionChanged(this);
        callback.onBufferedPositionChanged(this);
        handler.postDelayed(this, updatePeriodMs);
    }

    // Internal methods.

    /* package */ void setVideoSurface(@Nullable Surface surface) {
        hasSurface = surface != null;
        player.setVideoSurface(surface);
        maybeNotifyPreparedStateChanged(getCallback());
    }

    /* package */ void notifyStateChanged() {
        int playbackState = player.getPlaybackState();
        Callback callback = getCallback();
        maybeNotifyPreparedStateChanged(callback);
        callback.onPlayStateChanged(this);
        callback.onBufferingStateChanged(this, playbackState == Player.STATE_BUFFERING);
        if (playbackState == Player.STATE_ENDED) {
            callback.onPlayCompleted(this);
        }
    }

    private void maybeNotifyPreparedStateChanged(Callback callback) {
        boolean isPrepared = isPrepared();
        if (lastNotifiedPreparedState != isPrepared) {
            lastNotifiedPreparedState = isPrepared;
            callback.onPreparedStateChanged(this);
        }
    }

    @SuppressWarnings("nullness:argument")
    private static void removeSurfaceHolderCallback(SurfaceHolderGlueHost surfaceHolderGlueHost) {
        surfaceHolderGlueHost.setSurfaceHolderCallback(null);
    }

    private final class PlayerListener implements Player.Listener, SurfaceHolder.Callback {

        // SurfaceHolder.Callback implementation.

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            setVideoSurface(surfaceHolder.getSurface());
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            // Do nothing.
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            setVideoSurface(null);
        }

        // Player.Listener implementation.

        @Override
        public void onPlaybackStateChanged(@Player.State int playbackState) {
            notifyStateChanged();
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            Callback callback = getCallback();
            if (errorMessageProvider != null) {
                Pair<Integer, String> errorMessage = errorMessageProvider.getErrorMessage(error);
                callback.onError(TvLeanbackPlayerAdapter.this, errorMessage.first, errorMessage.second);
            } else {
                callback.onError(
                        TvLeanbackPlayerAdapter.this,
                        error.errorCode,
                        // This string was probably tailored for MediaPlayer, whose error callback takes two
                        // int arguments (int what, int extra). Since PlaybackException defines a single error
                        // code, we pass 0 as the extra.
                        context.getString(
                                androidx.leanback.R.string.lb_media_player_error, /* formatArgs...= */ error.errorCode, 0));
            }
        }

        @Override
        public void onTimelineChanged(Timeline timeline, @Player.TimelineChangeReason int reason) {
            updateAdBreakData();
            Callback callback = getCallback();
            callback.onDurationChanged(TvLeanbackPlayerAdapter.this);
            callback.onCurrentPositionChanged(TvLeanbackPlayerAdapter.this);
            callback.onBufferedPositionChanged(TvLeanbackPlayerAdapter.this);
        }

        @Override
        public void onPositionDiscontinuity(
                Player.PositionInfo oldPosition,
                Player.PositionInfo newPosition,
                @Player.DiscontinuityReason int reason) {
            Callback callback = getCallback();
            callback.onCurrentPositionChanged(TvLeanbackPlayerAdapter.this);
            callback.onBufferedPositionChanged(TvLeanbackPlayerAdapter.this);
        }

        @Override
        public void onVideoSizeChanged(VideoSize videoSize) {
            // There's no way to pass pixelWidthHeightRatio to leanback, so we scale the width that we
            // pass to take it into account. This is necessary to ensure that leanback uses the correct
            // aspect ratio when playing content with non-square pixels.
            if (videoSize.width <= 0 || videoSize.height <= 0) return;

            int scaledWidth = Math.round(videoSize.width * videoSize.pixelWidthHeightRatio);
            getCallback().onVideoSizeChanged(TvLeanbackPlayerAdapter.this, scaledWidth, videoSize.height);
        }
    }

    private long[] adGroupTimesMs = new long[0];
    private boolean[] playedAdGroups = new boolean[0];
    private int adGroupCount = 0;
    private final Timeline.Period period = new Timeline.Period();
    private final Timeline.Window window = new Timeline.Window();

    public long[] getAdGroupTimesMs() {
        return adGroupTimesMs;
    }

    public boolean[] getPlayedAdGroups() {
        return playedAdGroups;
    }

    public int getAdGroupCount() {
        return adGroupCount;
    }

    private void updateAdBreakData() {
        Player player = this.player;
        if (player == null) {
            return;
        }
        int adGroupCount = 0;
        Timeline timeline = player.getCurrentTimeline();
        if (!timeline.isEmpty()) {
            timeline.getWindow(player.getCurrentMediaItemIndex(), window);
            for (int j = window.firstPeriodIndex; j <= window.lastPeriodIndex; j++) {
                timeline.getPeriod(j, period);
                int periodAdGroupCount = period.getAdGroupCount();
                for (int adGroupIndex = 0; adGroupIndex < periodAdGroupCount; adGroupIndex++) {
                    long adGroupTimeInPeriodUs = period.getAdGroupTimeUs(adGroupIndex);
                    if (adGroupTimeInPeriodUs == C.TIME_END_OF_SOURCE) {
                        if (period.durationUs == C.TIME_UNSET) {
                            // Don't show ad markers for postrolls in periods with unknown duration.
                            continue;
                        }
                        adGroupTimeInPeriodUs = period.durationUs;
                    }
                    long adGroupTimeInWindowUs = adGroupTimeInPeriodUs + period.getPositionInWindowUs();
                    if (adGroupTimeInWindowUs >= 0) {
                        if (adGroupCount == adGroupTimesMs.length) {
                            int newLength = adGroupTimesMs.length == 0 ? 1 : adGroupTimesMs.length * 2;
                            adGroupTimesMs = Arrays.copyOf(adGroupTimesMs, newLength);
                            playedAdGroups = Arrays.copyOf(playedAdGroups, newLength);
                        }
                        adGroupTimesMs[adGroupCount] = Util.usToMs(adGroupTimeInWindowUs);
                        playedAdGroups[adGroupCount] = period.hasPlayedAdGroup(adGroupIndex);
                        adGroupCount++;
                    }
                }
            }
        }

        this.adGroupCount = adGroupCount;
        if (this.adGroupCount > adGroupTimesMs.length) {
            adGroupTimesMs = Arrays.copyOf(adGroupTimesMs, this.adGroupCount);
            playedAdGroups = Arrays.copyOf(playedAdGroups, this.adGroupCount);
        }
    }

}