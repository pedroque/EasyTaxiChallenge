package com.pedroabinajm.easytaxichallenge.ui.search

import android.databinding.BindingAdapter
import android.databinding.ViewDataBinding
import android.widget.ImageView
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.databinding.ItemPlaceBinding
import com.pedroabinajm.easytaxichallenge.ui.base.BaseAdapter
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


class PlaceAdapter : BaseAdapter<EasyPlace>() {
    val bookmarkSubject: Subject<Int> = PublishSubject.create()

    override fun getLayoutIdForPosition(position: Int) =
            R.layout.item_place

    override fun getViewHolderForLayout(layout: Int, binding: ViewDataBinding) = PlaceViewHolder(binding as ItemPlaceBinding)

    inner class PlaceViewHolder(binding: ItemPlaceBinding) : ViewHolder(binding) {

        init {
            binding.searchIcon.setOnClickListener {
                if (adapterPosition != -1) bookmarkSubject.onNext(adapterPosition)
            }
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("bookmark")
        fun setBookmark(imageView: ImageView, bookmark: Boolean) {
            imageView.setImageResource(if (bookmark) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)
        }
    }
}