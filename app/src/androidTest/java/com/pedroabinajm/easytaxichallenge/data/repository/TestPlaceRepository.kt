package com.pedroabinajm.easytaxichallenge.data.repository

import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable
import javax.inject.Inject


class TestPlaceRepository @Inject constructor() : PlaceRepository {
    override fun getCurrentPlace(lastPlace: Boolean): Observable<EasyPlace> {
        val place = EasyPlace(
                "some_id",
                "Rua Luis Gois, 206",
                -23.0,
                -46.0,
                "Vila Mariana, Sao Paulo",
                false
        )
        return Observable.just(place)
    }

    override fun saveLastPlace(easyPlace: EasyPlace) {
    }

    override fun getPlace(latLng: LatLng): Observable<EasyPlace> {
        val place = EasyPlace(
                "some_id",
                "Rua Luis Gois, 666",
                -23.5,
                -46.5,
                "Vila Mariana, Sao Paulo",
                false
        )
        return Observable.just(place)
    }
}