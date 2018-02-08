package com.pedroabinajm.easytaxichallenge.extensions

import android.view.View
import android.view.ViewGroup

fun ViewGroup.show(child: View) {
    (0 until childCount)
            .map { getChildAt(it) }
            .forEach { if (child != it) it.hide() else it.show() }
}