package com.pedroabinajm.easytaxichallenge.data.location

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable


interface Locator {
    fun getCurrentLocation(): Observable<LatLng>
}