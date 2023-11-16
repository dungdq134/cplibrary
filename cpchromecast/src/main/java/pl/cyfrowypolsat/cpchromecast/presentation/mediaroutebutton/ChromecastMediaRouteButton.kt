package pl.cyfrowypolsat.cpchromecast.presentation.mediaroutebutton

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.mediarouter.app.MediaRouteButton
import androidx.mediarouter.media.MediaRouteSelector
import com.google.android.gms.cast.framework.CastButtonFactory
import pl.cyfrowypolsat.cpchromecast.CpChromecast
import pl.cyfrowypolsat.cpchromecast.R
import pl.cyfrowypolsat.cpchromecast.core.di.CpChromecastQualifier
import pl.cyfrowypolsat.cpchromecast.presentation.manager.ChromecastManager
import javax.inject.Inject

class ChromecastMediaRouteButton : MediaRouteButton {

    companion object {
        private fun getThemedContext(context: Context?): Context {
            return ContextThemeWrapper(context, androidx.appcompat.R.style.Theme_AppCompat_Light)
        }
    }

    @CpChromecastQualifier
    @Inject
    lateinit var chromecastManager: ChromecastManager

    private val activatedTint = ContextCompat.getColor(context, R.color.cpchr_color_primary)
    private val defaultTint = ContextCompat.getColor(context, R.color.cpchr_white_solid)

    constructor(context: Context?) : super(getThemedContext(context))
    constructor(context: Context?, attrs: AttributeSet?) : super(getThemedContext(context), attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(getThemedContext(context), attrs, defStyleAttr)

    init {
        if (CpChromecast.instance.isChromecastComponentInitialized()) {
            CpChromecast.instance.getCpChromecastComponent().inject(this)
            init()
        }
    }

    private fun init() {
        dialogFactory = ThemeableMediaRouteDialogFactory()
        routeSelector = MediaRouteSelector.EMPTY
        setupMediaRouteButton()
    }

    private fun setupMediaRouteButton() {
        chromecastManager.setUpMediaRouteButtonSafe {
            CastButtonFactory.setUpMediaRouteButton(context, this)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        refreshTint(chromecastManager.isConnectingOrConnected())
    }

    private fun refreshTint(isActivated: Boolean) {
        if (isActivated) {
            applyTint(activatedTint)
        } else {
            applyTint(defaultTint)
        }
    }

    private fun applyTint(color: Int) {
        val remoteIndicator = getRemoteIndicator()
        if (remoteIndicator != null) {
            val wrappedDrawable = DrawableCompat.wrap(remoteIndicator)
            DrawableCompat.setTint(wrappedDrawable, color)
        }
    }

    private fun getRemoteIndicator(): Drawable? {
        try {
            val remoteIndicatorField = MediaRouteButton::class.java.getDeclaredField("mRemoteIndicator")
            remoteIndicatorField.isAccessible = true
            return remoteIndicatorField[this] as Drawable
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}