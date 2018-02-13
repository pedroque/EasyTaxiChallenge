package com.pedroabinajm.easytaxichallenge.data.location

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import javax.inject.Inject


class TestLocator @Inject constructor()  : Locator {
    override fun getCurrentLocation(): Observable<LatLng> {
        return Observable.just(LatLng(-23.0, -46.0))
    }
}