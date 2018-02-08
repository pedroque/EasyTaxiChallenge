package com.pedroabinajm.easytaxichallenge.ui.commons

import android.R
import android.animation.Animator
import android.graphics.Rect
import android.view.View
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator

object ViewHelper {

    // A method to findAsync height of the status bar
    fun getStatusBarHeight(window: Window): Int {
        val rectangle = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        return rectangle.top
    }

    fun hideViewAnimated(view: View, duration: Int, hideState: Int) {
        hideViewAnimated(view, duration, HideViewAnimatorListener(view, hideState))
    }

    fun hideViewAnimated(view: View, hideState: Int) {
        hideViewAnimated(view, HideViewAnimatorListener(view, hideState))
    }

    fun hideViewAnimated(view: View, listener: Animator.AnimatorListener) {
        toggleViewVisibilityAnimated(view, view.resources.getInteger(R.integer.config_shortAnimTime), listener, 0f)
    }

    fun hideViewAnimated(view: View, duration: Int, listener: Animator.AnimatorListener) {
        toggleViewVisibilityAnimated(view, duration, listener, 0f)
    }

    fun showViewAnimated(view: View) {
        showViewAnimated(view, ShowViewAnimatorListener(view))
    }

    fun showViewAnimated(view: View, duration: Int) {
        showViewAnimated(view, duration, ShowViewAnimatorListener(view))
    }

    fun showViewAnimated(view: View, duration: Int, listener: Animator.AnimatorListener) {
        toggleViewVisibilityAnimated(view, duration, listener, 1f)
    }

    fun showViewAnimated(view: View, listener: Animator.AnimatorListener) {
        toggleViewVisibilityAnimated(view, view.resources.getInteger(R.integer.config_shortAnimTime), listener, 1f)
    }

    fun hideViewAnimated(view: View) {
        hideViewAnimated(view, View.GONE)
    }


    private fun toggleViewVisibilityAnimated(view: View, duration: Int, listener: Animator.AnimatorListener, alpha: Float) {
        view.animate().alpha(alpha).translationY(0f).setDuration(duration.toLong()).setInterpolator(AccelerateDecelerateInterpolator()).setListener(listener)
    }
}