package pl.cyfrowypolsat.cpcommon.presentation.extensions

import android.graphics.Rect
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatEditText

fun CheckBox.setTogglePasswordTransformation(passwordEditText: AppCompatEditText) {
    setOnCheckedChangeListener { _, isChecked ->
        if (isChecked) {
            passwordEditText.transformationMethod = object : TransformationMethod {
                override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
                    return source ?: ""
                }

                override fun onFocusChanged(view: View?, sourceText: CharSequence?, focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {

                }
            }
        } else {
            passwordEditText.transformationMethod = PasswordTransformationMethod()
        }
    }
}