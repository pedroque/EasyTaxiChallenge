package com.pedroabinajm.easytaxichallenge.data.repository

import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable


interface PlaceRepository {
    fun getCurrentPlace(lastPlace: Boolean): Observable<EasyPlace>
    fun saveLastPlace(easyPlace: EasyPlace)
    fun getPlace(latLng: LatLng): Observable<EasyPlace>
}