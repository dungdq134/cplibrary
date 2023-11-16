package pl.cyfrowypolsat.cpcommon.presentation.extensions

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


fun Context.asActivity() = this as? Activity

fun Context.retrieveDimension(set: AttributeSet?,
                              @StyleableRes attrs: IntArray,
                              @StyleableRes index: Int) = retrieveValue(
        set = set,
        attrs = attrs,
        onRetrieve = { attrArray -> attrArray.getDimension(index, 0f) },
        defaultValue = 0f)

fun Context.retrieveInteger(set: AttributeSet?,
                            @StyleableRes attrs: IntArray,
                            @StyleableRes index: Int,
                            defaultValue: Int = 0) = retrieveValue(
        set = set,
        attrs = attrs,
        onRetrieve = { attrArray -> attrArray.getInteger(index, 0) },
        defaultValue = defaultValue)

fun Context.retrieveDrawable(set: AttributeSet?,
                             @StyleableRes attrs: IntArray,
                             @StyleableRes index: Int) = retrieveValue(
        set = set,
        attrs = attrs,
        onRetrieve = { attrArray -> attrArray.getDrawable(index) },
        defaultValue = null)

fun Context.retrieveDimensionPixelSize(set: AttributeSet?,
                              @StyleableRes attrs: IntArray,
                              @StyleableRes index: Int) = retrieveValue(
    set = set,
    attrs = attrs,
    onRetrieve = { attrArray -> attrArray.getDimensionPixelSize(index, -1) },
    defaultValue = -1)

fun Context.retrieveResourceId(set: AttributeSet?,
                               @StyleableRes attrs: IntArray,
                               @StyleableRes index: Int) = retrieveValue(
    set = set,
    attrs = attrs,
    onRetrieve = { attrArray -> attrArray.getResourceId(index, 0) },
    defaultValue = 0)

fun Context.retrieveBoolean(set: AttributeSet?,
                            @StyleableRes attrs: IntArray,
                            @StyleableRes index: Int,
                            defaultValue: Boolean = false) = retrieveValue(
        set = set,
        attrs = attrs,
        onRetrieve = { attrArray -> attrArray.getBoolean(index, defaultValue) },
        defaultValue = defaultValue)


fun Context.retrieveFloat(set: AttributeSet?,
                          @StyleableRes attrs: IntArray,
                          @StyleableRes index: Int) = retrieveValue(
        set = set,
        attrs = attrs,
        onRetrieve = { attrArray -> attrArray.getFloat(index, 0f) },
        defaultValue = 0f)

fun Context.retrieveString(set: AttributeSet?,
                           @StyleableRes attrs: IntArray,
                           @StyleableRes index: Int) = retrieveValue(
        set = set,
        attrs = attrs,
        onRetrieve = { attrArray -> attrArray.getString(index) },
        defaultValue = null)


fun Context.retrieveColor(set: AttributeSet?,
                          @StyleableRes attrs: IntArray,
                          @StyleableRes index: Int,
                          @ColorRes defaultColorId: Int) = retrieveValue(
        set = set,
        attrs = attrs,
        onRetrieve = { attrArray -> attrArray.getColor(index, ContextCompat.getColor(this, defaultColorId)) },
        defaultValue = ContextCompat.getColor(this, defaultColorId))


fun Context.retrieveFont(set: AttributeSet?,
                         @StyleableRes attrs: IntArray,
                         @StyleableRes index: Int) = retrieveValue(
        set = set,
        attrs = attrs,
        onRetrieve = { attrArray ->
            val fontFamilyId = attrArray.getResourceId(index, 0)
            ResourcesCompat.getFont(this, fontFamilyId)
        },
        defaultValue = null)

fun Context.getDimension(@DimenRes resId: Int): Float {
    return resources.getDimension(resId)
}

private fun <T> Context.retrieveValue(set: AttributeSet?,
                                      @StyleableRes attrs: IntArray,
                                      onRetrieve: (attrArray: TypedArray) -> T,
                                      defaultValue: T): T {
    val attrArray = theme.obtainStyledAttributes(set, attrs, 0, 0)
    return try {
        onRetrieve(attrArray)
    } catch (ex: Exception) {
        defaultValue
    } finally {
        attrArray.recycle()
    }
}