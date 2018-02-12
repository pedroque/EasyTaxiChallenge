package com.pedroabinajm.easytaxichallenge.data.repository

import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.app.Preferences
import com.pedroabinajm.easytaxichallenge.data.entity.mapper.AddressMapper
import com.pedroabinajm.easytaxichallenge.data.location.Locator
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.AddressDataSource
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.PlaceDataSource
import io.reactivex.Observable
import javax.inject.Inject


class PlaceRepositoryImpl @Inject constructor(
        private val placeDataSource: PlaceDataSource,
        private val addressDataSource: AddressDataSource,
        private val addressMapper: AddressMapper,
        private val locator: Locator,
        private val preferences: Preferences
) : PlaceRepository {

    override fun getCurrentPlace(lastPlace: Boolean): Observable<EasyPlace> {
        return if (lastPlace) {
            getLastPlace()?.let {
                Observable.just(it)
            } ?: locator.getCurrentLocation().flatMap { getPlace(it) }
        } else {
            locator.getCurrentLocation().flatMap { getPlace(it) }
        }
    }

    override fun saveLastPlace(easyPlace: EasyPlace) {
        getLastPlace()?.let {
            if (!it.bookmark) {
                placeDataSource.delete(it.id)
            }
        }
        placeDataSource.savePlace(easyPlace)
        preferences.lastPlaceId = easyPlace.id
    }

    private fun getLastPlace(): EasyPlace? {
        return preferences.lastPlaceId?.let {
            placeDataSource.getPlace(it)
        }
    }

    override fun getPlace(latLng: LatLng): Observable<EasyPlace> {
        return addressDataSource.getAddress(latLng).map { address ->
            val place = addressMapper.transform(address)
            saveLastPlace(place)
            place
        }
    }
}