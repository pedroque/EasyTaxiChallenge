package com.pedroabinajm.easytaxichallenge.ui.map

import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.base.BaseViewModel
import com.pedroabinajm.easytaxichallenge.ui.commons.Resource
import io.reactivex.Observable


class PlaceViewModel(
        private val placeRepository: PlaceRepository,
        schedulerProvider: ISchedulerProvider
) : BaseViewModel(schedulerProvider) {
    val place = MutableLiveData<Resource<EasyPlace?>>()

    fun fetchCurrentPlace(lastPlace: Boolean): Observable<EasyPlace> {
        place.postValue(Resource.loading(null))
        return execute(placeRepository.getCurrentPlace(lastPlace), {
            place.postValue(Resource.success(it))
        }, {
            place.postValue(Resource.error(it, null))
        })
    }

    fun fetchPlace(latLng: LatLng): Observable<EasyPlace> {
        place.value = Resource.loading(null)
        return execute(placeRepository.getPlace(latLng), {
            place.value = Resource.success(it)
        }, {
            place.value = Resource.error(it, null)
        })
    }

    fun setPlace(place: EasyPlace) {
        placeRepository.saveLastPlace(place)
        this.place.value = Resource.success(place)
    }
}