package pl.cyfrowypolsat.cpplayercore.tv.settings

import android.view.FocusFinder
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

class TvSettingsItemViewOnKeyListener(private val itemViewClasses: List<KClass<out View>>,
                                      private val itemViewContainer: ViewGroup,
                                      private val onFinish: () -> Unit) : View.OnKeyListener {

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                || keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_ESCAPE) {
            if (event?.action == KeyEvent.ACTION_UP) {
                onFinish()
            }
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            val nextFocus = FocusFinder.getInstance().findNextFocus(itemViewContainer, v, View.FOCUS_UP)
            if (v != null && (nextFocus == null || itemViewClasses.none { it == nextFocus::class })) {
                return true
            }
            return false
        }
        return false
    }
}