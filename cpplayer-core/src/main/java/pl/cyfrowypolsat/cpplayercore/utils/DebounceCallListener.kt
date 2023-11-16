package pl.cyfrowypolsat.cpplayercore.utils

class DebounceCallListener(private val interval: Long) {
    private var lastClickTime = 0L

    fun call(listenerBlock: () -> Unit) {
        val time = System.currentTimeMillis()
        if (time - lastClickTime >= interval) {
            lastClickTime = time
            listenerBlock()
        }
    }

}