package com.pedroabinajm.easytaxichallenge.data.repository.datasource

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable


interface AddressDataSource {
    fun getAddress(latLng: LatLng): Observable<Address>
}