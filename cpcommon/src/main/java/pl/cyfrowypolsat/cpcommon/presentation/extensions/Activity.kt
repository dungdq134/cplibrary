package pl.cyfrowypolsat.cpcommon.presentation.extensions

import android.app.Activity
import android.os.Build

fun Activity.isInPipOrMultiWindowMode() : Boolean {
    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            && (isInPictureInPictureMode || isInMultiWindowMode))
}

fun Activity.isAfterOnSaveInstanceState(): Boolean {
    return isFinishing or isDestroyed
}