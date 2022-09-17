package com.arash.altafi.applockapp

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

fun ImageView.clear() {
    this.setImageDrawable(null)
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.toShow() {
    this.visibility = View.VISIBLE
}

fun View.isShow(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.toHide() {
    this.visibility = View.INVISIBLE
}

fun View.isHide(): Boolean {
    return this.visibility == View.INVISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}

inline fun <reified NEW> Any.cast(): NEW? {
    return if (this.isCastable<NEW>())
        this as NEW
    else null
}

inline fun <reified NEW> Any.isCastable(): Boolean {
    return this is NEW
}

fun Context.getActivity(): Activity {
    return ((this as ContextWrapper)).baseContext as Activity
}

@ColorInt
fun Context.getAttrColor(@AttrRes attrID: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(attrID, typedValue, true)
    return typedValue.data
}

fun Context.getAttr(attrID: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(attrID, typedValue, true)
    return typedValue.data
}

fun View.shakeError(listener: Animator.AnimatorListener? = null) {
    YoYo.with(Techniques.Shake)
        .duration(500)
        .repeat(0)
        .apply {
            if (listener != null)
                withListener(listener)
        }
        .playOn(this)
}

fun View.pulseSlow(listener: Animator.AnimatorListener? = null) {
    YoYo.with(Techniques.Pulse)
        .duration(1500)
        .repeat(-1)
        .apply {
            if (listener != null)
                withListener(listener)
        }
        .playOn(this)
}