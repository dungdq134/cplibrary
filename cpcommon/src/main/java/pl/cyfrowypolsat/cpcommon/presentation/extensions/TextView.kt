package pl.cyfrowypolsat.cpcommon.presentation.extensions

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.doOnPreDraw

val TextView.leftDrawable: Drawable?
    get() = compoundDrawables[0]

val TextView.rightDrawable: Drawable?
    get() = compoundDrawables[2]

fun TextView.fitHeightAfterAutoSize(maxHeight: Int) {
    changeHeight(maxHeight)
    this.doOnPreDraw {
        changeHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}

fun TextView.setMaxLinesForEllipsize() = post {
    Handler(Looper.getMainLooper()).post {
        val numberOfCompletelyVisibleLines = (height + lineSpacingExtra - paddingTop - paddingBottom) / lineHeight
        maxLines = numberOfCompletelyVisibleLines.toInt()
    }
}

fun TextView.setTextAndAdjustVisibility(text: String?) {
    if (text.isNullOrBlank()) {
        gone()
    } else {
        this.text = text
        visible()
    }
}

fun TextView.setEmptyVisibleText() {
    this.text = ""
    visible()
}

fun TextView.setTextAndAdjustVisibility(charSequence: CharSequence?) {
    if (charSequence.isNullOrBlank()) {
        gone()
    } else {
        this.text = charSequence
        visible()
    }
}

fun TextView.fromHtml(text: String? = null) {
    val newText = text ?: this.text
    this.text = newText?.let { HtmlCompat.fromHtml(it.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY) } ?: ""
}

fun TextView.setFromHtmlAndAdjustVisibility(text: String?) {
    setTextAndAdjustVisibility(text)
    fromHtml()
}

class URLSpanNoUnderline(url: String?) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
        ds.isFakeBoldText = true
    }
}

fun TextView.stripUnderlines() {
    val s: Spannable = SpannableString(text)
    val spans = s.getSpans(0, s.length, URLSpan::class.java)
    for (span in spans) {
        val start = s.getSpanStart(span)
        val end = s.getSpanEnd(span)
        s.removeSpan(span)
        val newSpan = URLSpanNoUnderline(span.url)
        s.setSpan(newSpan, start, end, 0)
    }
    text = s
}

fun TextView.formatTextWithUrl(@ColorRes linkColor: Int) {
    setLinkTextColor(ContextCompat.getColorStateList(context, linkColor))
    stripUnderlines()
    movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.setDrawableColor(@ColorRes color: Int) {
    compoundDrawables.filterNotNull().forEach {
        it.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
    }
}

fun TextView.setDrawableTintList(@ColorRes color: Int) {
    compoundDrawables.filterNotNull().forEach {
        it.setTintList(ContextCompat.getColorStateList(context, color))
    }
}

fun TextView.setTextColorByResource(@ColorRes colorResource: Int) {
    setTextColor(ContextCompat.getColor(context, colorResource))
}