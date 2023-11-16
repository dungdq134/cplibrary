package pl.cyfrowypolsat.cpplayercore.tv.seealso.adapter

import android.view.KeyEvent
import android.view.View

class TvSeeAlsoKeyListener {

    class UpKeyListener(private val onFocusUp : () -> Unit) : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
            return when (keyCode) {
                KeyEvent.KEYCODE_DPAD_UP -> {
                    if (event.action != KeyEvent.ACTION_UP) {
                        onFocusUp()
                    }
                    true
                }
                else -> false
            }
        }
    }

    class DownKeyListener(private val onFocusDown : () -> Unit) : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
            return when (keyCode) {
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    if (event.action != KeyEvent.ACTION_UP) {
                        onFocusDown()
                    }
                    true
                }
                else -> false
            }
        }
    }

    class UpOrDownKeyListener(private val onFocusUp : () -> Unit, private val onFocusDown : () -> Unit) : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
            return when (keyCode) {
                KeyEvent.KEYCODE_DPAD_UP -> {
                    if (event.action != KeyEvent.ACTION_UP) {
                        onFocusUp()
                    }
                    true
                }
                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    if (event.action != KeyEvent.ACTION_UP) {
                        onFocusDown()
                    }
                    true
                }
                else -> false
            }
        }
    }

    class NonBlockingOnKeyListener : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
            return false
        }
    }
}