package com.pedroabinajm.easytaxichallenge.ui.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.pedroabinajm.easytaxichallenge.BR

class QueryViewModel(s: String) : BaseObservable() {
    val data: LiveData<String>
        get() = mutableQuery

    private val mutableQuery = MutableLiveData<String>()

    init {
        mutableQuery.value = s
    }

    @Bindable
    fun getQuery() = data.value!!

    fun setQuery(query: String) {
        if (this.data.value != query) {
            mutableQuery.value = query
            notifyPropertyChanged(BR.query)
        }
    }

}