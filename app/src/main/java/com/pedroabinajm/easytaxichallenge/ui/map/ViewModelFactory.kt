@file:Suppress("UNCHECKED_CAST")

package com.pedroabinajm.easytaxichallenge.ui.map

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
        private val placeRepository: PlaceRepository,
        private val schedulerProvider: ISchedulerProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            return PlaceViewModel(placeRepository, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}