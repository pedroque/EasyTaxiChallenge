package com.pedroabinajm.easytaxichallenge.ui.commons

import android.databinding.BindingAdapter
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.extensions.friendlyMessage
import com.pedroabinajm.easytaxichallenge.extensions.hide
import com.pedroabinajm.easytaxichallenge.extensions.show

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("showIf")
    fun showIf(view: View, show: Boolean) {
        if (show) view.show() else view.hide()
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @JvmStatic
    @BindingAdapter("resourceText")
    fun resourceText(view: TextView, resource: Resource<Any>?) {
        resource?.error?.let {
            view.setText(it.friendlyMessage)
        }
    }

    @JvmStatic
    @BindingAdapter("bookmarkButton")
    fun setBookmarkButton(imageButton: ImageButton, resource: Resource<EasyPlace?>?) {
        imageButton.hide()
        resource?.data?.let {
            imageButton.show()
            imageButton.setImageResource(if (it.bookmark) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)
        }
    }
}