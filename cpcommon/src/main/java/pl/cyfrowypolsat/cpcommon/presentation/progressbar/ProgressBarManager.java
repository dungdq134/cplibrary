package pl.cyfrowypolsat.cpcommon.presentation.progressbar;

import android.os.SystemClock;
import android.view.View;

/**
 * Based on ContentLoadingProgressBar
 *
 * https://gist.github.com/cbeyls/133164625e06b16520c1
 * ContentLoadingProgressBar implements a ProgressBar that waits a minimum time to be
 * dismissed before showing. Once visible, the progress bar will be visible for
 * a minimum amount of time to avoid "flashes" in the UI when an event could take
 * a largely variable time to complete (from none, to a user perceivable amount).
 * <p/>
 * This version is similar to the support library version but implemented "the right way".
 *
 * @author Christophe Beyls
 */
public class ProgressBarManager {

    interface Listener {
        void show();
        void hide();
    }

    private static final long MIN_SHOW_TIME = 250L; // ms
    private static final long MIN_DELAY = 1500L; // ms

    private boolean mIsAttachedToWindow = false;
    private boolean mIsShown;
    long mStartTime = -1L;
    private final Long mDelay;
    private final View mView;
    private final Listener mListener;

    private final Runnable mDelayedHide = new Runnable() {

        @Override
        public void run() {
            mListener.hide();
            mStartTime = -1L;
        }
    };

    private final Runnable mDelayedShow = new Runnable() {

        @Override
        public void run() {
            mStartTime = SystemClock.uptimeMillis();
            mListener.show();
        }
    };

    public ProgressBarManager(View view, boolean isDelayed, Listener listener) {
        this.mView = view;
        mIsShown = view.getVisibility() == View.VISIBLE;
        mDelay = isDelayed ? MIN_DELAY : 0;
        mListener = listener;
    }

    public void onAttachedToWindow() {
        mIsAttachedToWindow = true;
        if (mIsShown && (mView.getVisibility() != View.VISIBLE)) {
            mView.postDelayed(mDelayedShow, mDelay);
        }
    }

    public void onDetachedFromWindow() {
        mIsAttachedToWindow = false;
        mView.removeCallbacks(mDelayedHide);
        mView.removeCallbacks(mDelayedShow);
        if (!mIsShown && mStartTime != -1L) {
            mListener.hide();
        }
        mStartTime = -1L;
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    public void maybeHide() {
        if (mIsShown) {
            mIsShown = false;
            if (mIsAttachedToWindow) {
                mView.removeCallbacks(mDelayedShow);
            }
            long diff = SystemClock.uptimeMillis() - mStartTime;
            if (mStartTime == -1L || diff >= MIN_SHOW_TIME) {
                // The progress spinner has been shown long enough
                // OR was not shown yet. If it wasn't shown yet,
                // it will just never be shown.
                mListener.hide();
                mStartTime = -1L;
            } else {
                // The progress spinner is shown, but not long enough,
                // so put a delayed message in to hide it when its been
                // shown long enough.
                mView.postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    public void maybeShow() {
        if (!mIsShown) {
            mIsShown = true;
            if (mIsAttachedToWindow) {
                mView.removeCallbacks(mDelayedHide);
                if (mStartTime == -1L) {
                    mView.postDelayed(mDelayedShow, mDelay);
                }
            }
        }
    }
}