package pl.cyfrowypolsat.cpplayercore.tv.leanback.unmodified;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import pl.cyfrowypolsat.cpplayercore.R;

public class PlaybackTransportRowView extends LinearLayout {


    public interface OnUnhandledKeyListener {
        /**
         * Returns true if the key event should be consumed.
         */
        boolean onUnhandledKey(KeyEvent event);
    }

    private PlaybackTransportRowView.OnUnhandledKeyListener mOnUnhandledKeyListener;

    public PlaybackTransportRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaybackTransportRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnUnhandledKeyListener(PlaybackTransportRowView.OnUnhandledKeyListener listener) {
        mOnUnhandledKeyListener = listener;
    }

    PlaybackTransportRowView.OnUnhandledKeyListener getOnUnhandledKeyListener() {
        return mOnUnhandledKeyListener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (super.dispatchKeyEvent(event)) {
            return true;
        }
        return mOnUnhandledKeyListener != null && mOnUnhandledKeyListener.onUnhandledKey(event);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        final View focused = findFocus();
        if (focused != null && focused.requestFocus(direction, previouslyFocusedRect)) {
            return true;
        }
        View progress = findViewById(R.id.playback_progress);
        if (progress != null && progress.isFocusable()) {
            if (progress.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        return getParent().getParent().focusSearch(focused, direction);
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean isPlaybackProgressFocused() {
        View progress = findViewById(R.id.playback_progress);
        if (progress != null) {
            return progress.isFocused();
        }
        return false;
    }

}
