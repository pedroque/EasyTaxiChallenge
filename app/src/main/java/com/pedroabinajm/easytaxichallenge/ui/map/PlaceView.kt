package com.pedroabinajm.easytaxichallenge.ui.map

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.databinding.ViewPlaceBinding


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

    var place: EasyPlace? = null
        set(value) {
            dataBinding?.place = value
            field = value
        }


    companion object {
        @JvmStatic
        @BindingAdapter("place")
        fun setPlace(placeView: PlaceView, easyPlace: EasyPlace?) {
            placeView.place = easyPlace
        }
    }
}