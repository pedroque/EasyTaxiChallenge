package com.pedroabinajm.easytaxichallenge.ui.map

import android.arch.lifecycle.MutableLiveData
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.base.BaseViewModel


class PlaceViewModel(
        private val placeRepository: PlaceRepository,
        schedulerProvider: ISchedulerProvider
) : BaseViewModel(schedulerProvider) {
    val place = MutableLiveData<EasyPlace>()

    fun fetchLastPlace() {
        place.value = placeRepository.getLastPlace()
    }

    fun savePlace(place: EasyPlace) {
        placeRepository.saveLastPlace(place)
        fetchLastPlace()
    }
}