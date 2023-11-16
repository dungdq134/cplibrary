package pl.cyfrowypolsat.cpplayercore.mobile.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.media3.ui.R

class PlayerImageButton
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : AppCompatImageButton(context, attrs, defStyleAttr) {

    private val buttonAlphaEnabled = resources.getInteger(R.integer.exo_media_button_opacity_percentage_enabled) / 100f
    private val buttonAlphaDisabled = resources.getInteger(R.integer.exo_media_button_opacity_percentage_disabled) / 100f

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (enabled) {
            alpha = buttonAlphaEnabled
        } else {
            alpha = buttonAlphaDisabled
        }
    }

}