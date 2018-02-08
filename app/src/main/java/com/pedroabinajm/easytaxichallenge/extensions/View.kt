package com.pedroabinajm.easytaxichallenge.extensions

import android.view.View
import com.pedroabinajm.easytaxichallenge.ui.commons.ViewHelper

fun View.show() {
    ViewHelper.showViewAnimated(this)
}

fun View.hide() {
    ViewHelper.hideViewAnimated(this)
}

fun View.addPaddingTop(extraPaddingTop: Int) {
    setPadding(paddingLeft,
            paddingTop + extraPaddingTop,
            paddingRight,
            paddingBottom)
}