package com.pedroabinajm.easytaxichallenge.ui.commons

import android.animation.Animator
import android.view.View

class ShowViewAnimatorListener(private val view: View) : Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator) {
        view.visibility = View.VISIBLE
    }

    override fun onAnimationEnd(animation: Animator) {
        view.animate().setListener(null)
    }

    override fun onAnimationCancel(animation: Animator) {

    }

    override fun onAnimationRepeat(animation: Animator) {

    }
}
