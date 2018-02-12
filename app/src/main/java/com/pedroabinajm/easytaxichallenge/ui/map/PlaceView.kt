package com.pedroabinajm.easytaxichallenge.ui.map

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.databinding.ViewPlaceBinding
import com.pedroabinajm.easytaxichallenge.extensions.friendlyMessage
import com.pedroabinajm.easytaxichallenge.ui.commons.Resource


class PlaceView : FrameLayout {
    private var dataBinding: ViewPlaceBinding? = null

    // region constructor
    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_place, null, false)
        addView(dataBinding?.root)
    }
    // endregion

    var resource: Resource<EasyPlace?>? = null
        set(value) {
            dataBinding?.resource = value
            field = value
        }


    companion object {
        @JvmStatic
        @BindingAdapter("place")
        fun setPlace(placeView: PlaceView, resource: Resource<EasyPlace?>?) {
            placeView.resource = resource
        }

        @JvmStatic
        @BindingAdapter("placeName")
        fun setPlaceName(textView: TextView, resource: Resource<EasyPlace?>?) {
            if (resource != null) {
                when (resource.status) {
                    Resource.Status.SUCCESS -> textView.text = resource.data?.name
                    Resource.Status.LOADING -> textView.setText(R.string.loading)
                    Resource.Status.ERROR -> textView.setText(resource.error?.friendlyMessage ?: R.string.unexpected_error)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("placeDescription")
        fun setPlaceDescription(textView: TextView, resource: Resource<EasyPlace?>?) {
            if (resource != null) {
                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        textView.visibility = View.GONE
                        resource.data?.description?.let {
                            textView.text = it
                            textView.visibility = View.VISIBLE
                        }
                    }
                    Resource.Status.LOADING -> textView.visibility = View.GONE
                    Resource.Status.ERROR -> textView.visibility = View.GONE
                }
            }
        }
    }
}