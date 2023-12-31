package pl.cyfrowypolsat.cpcommon.presentation.imageloader;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;

// This is a workaround for this issue: https://github.com/bumptech/glide/issues/363
// PR: https://github.com/bumptech/glide/pull/3913
// https://gist.github.com/kevinvanmierlo/c46f66027e3ae37ebea85a8d2e12aaba
public class CrossFadeFactory implements TransitionFactory<Drawable> {
    @Override
    public Transition<Drawable> build(DataSource dataSource, boolean isFirstResource) {
        if (dataSource == DataSource.MEMORY_CACHE) {
            return NoTransition.get();
        }
        return new CrossFadeTransition();
    }
}

class CrossFadeTransition implements Transition<Drawable> {
    @Override
    public boolean transition(Drawable current, ViewAdapter adapter) {
        Drawable previous = adapter.getCurrentDrawable();
        if (previous == null) {
            previous = new ColorDrawable(Color.TRANSPARENT);
        }

        CrossFadeDrawable crossFadeDrawable = new CrossFadeDrawable(previous, current);
        crossFadeDrawable.setCrossFadeEnabled(true);
        crossFadeDrawable.startTransition();
        adapter.setDrawable(crossFadeDrawable);
        return true;
    }
}

class CrossFadeDrawable extends Drawable implements Drawable.Callback {
    private Drawable previousDrawable;
    private Drawable currentDrawable;

    private float fadeDuration = 300f;
    private long startTimeMillis = 0L;
    private boolean animating = false;
    private int alpha = 0xFF;
    private boolean crossFade = false;

    public CrossFadeDrawable(@NonNull Drawable previousDrawable, @NonNull Drawable currentDrawable) {
        this.previousDrawable = previousDrawable;
        this.currentDrawable = currentDrawable;

        previousDrawable.setCallback(this);
        currentDrawable.setCallback(this);
    }

    public void startTransition(float duration) {
        fadeDuration = duration;
        startTransition();
    }

    public void startTransition() {
        animating = true;
        startTimeMillis = SystemClock.uptimeMillis();
        invalidateSelf();
    }

    private float getNormalizedTime() {
        if (startTimeMillis == 0L) {
            return 0f;
        }
        return Math.min(1f, (SystemClock.uptimeMillis() - startTimeMillis) / fadeDuration);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (!animating) {
            if (previousDrawable != null) {
                previousDrawable.draw(canvas);
            } else {
                currentDrawable.draw(canvas);
            }
        } else {
            float normalized = getNormalizedTime();
            if (normalized >= 1f) {
                previousDrawable.setCallback(null);

                animating = false;
                previousDrawable = null;
                currentDrawable.draw(canvas);
            } else {
                if (crossFade) {
                    int partialAlpha = (int) (alpha * normalized);
                    if (previousDrawable != null) {
                        previousDrawable.setAlpha(255 - partialAlpha);
                        previousDrawable.draw(canvas);
                        previousDrawable.setAlpha(alpha);
                    }

                    currentDrawable.setAlpha(partialAlpha);
                    currentDrawable.draw(canvas);
                    currentDrawable.setAlpha(alpha);
                } else {
                    if (previousDrawable != null) {
                        previousDrawable.draw(canvas);
                    } else {
                        currentDrawable.draw(canvas);
                    }
                }

                invalidateSelf();
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    @Override
    public int getAlpha() {
        return this.alpha;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        if (previousDrawable != null) {
            previousDrawable.setBounds(bounds);
        }
        if (currentDrawable != null) {
            currentDrawable.setBounds(bounds);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        if (!animating && previousDrawable != null) {
            return previousDrawable.getIntrinsicWidth();
        } else {
            return currentDrawable.getIntrinsicWidth();
        }
    }

    @Override
    public int getIntrinsicHeight() {
        if (!animating && previousDrawable != null) {
            return previousDrawable.getIntrinsicHeight();
        } else {
            return currentDrawable.getIntrinsicHeight();
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    /**
     * Enables or disables the cross fade of the drawables. When cross fade
     * is disabled, the first drawable is always drawn opaque. With cross
     * fade enabled, the first drawable is drawn with the opposite alpha of
     * the second drawable. Cross fade is disabled by default.
     *
     * @param enabled True to enable cross fading, false otherwise.
     */
    public void setCrossFadeEnabled(boolean enabled) {
        crossFade = enabled;
    }

    /**
     * Indicates whether the cross fade is enabled for this transition.
     *
     * @return True if cross fading is enabled, false otherwise.
     */
    public boolean isCrossFadeEnabled() {
        return crossFade;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        invalidateSelf();
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        unscheduleSelf(what);
    }
}