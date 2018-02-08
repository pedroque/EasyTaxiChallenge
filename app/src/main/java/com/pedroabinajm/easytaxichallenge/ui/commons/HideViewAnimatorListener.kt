package com.pedroabinajm.easytaxichallenge.ui.commons

import android.animation.Animator
import android.view.View

class HideViewAnimatorListener @JvmOverloads constructor(private val view: View, private val mHideState: Int = View.GONE) : Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator) {

    }

    override fun onAnimationEnd(animation: Animator) {
        view.visibility = mHideState
        view.animate().setListener(null)
    }

    override fun onAnimationCancel(animation: Animator) {

    }

    override fun onAnimationRepeat(animation: Animator) {

    }
}
