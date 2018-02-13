@file:Suppress("UNCHECKED_CAST")

package com.pedroabinajm.easytaxichallenge.ui.map

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.pedroabinajm.easytaxichallenge.data.location.PlaceAutocompleteProvider
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.search.SearchPlaceViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
        private val placeRepository: PlaceRepository,
        private val searchPlaceAutocompleteProvider: PlaceAutocompleteProvider,
        private val searchDebounce: Long,
        private val schedulerProvider: ISchedulerProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
            return PlaceViewModel(placeRepository, schedulerProvider) as T
        } else if (modelClass.isAssignableFrom(SearchPlaceViewModel::class.java)) {
            return SearchPlaceViewModel(searchPlaceAutocompleteProvider, searchDebounce, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}