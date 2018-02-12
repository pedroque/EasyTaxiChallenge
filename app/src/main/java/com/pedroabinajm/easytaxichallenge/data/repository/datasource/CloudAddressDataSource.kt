package com.pedroabinajm.easytaxichallenge.data.repository.datasource

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import java.io.IOException
import java.util.*
import javax.inject.Inject


class CloudAddressDataSource @Inject constructor(
        private val context: Context
) : AddressDataSource {
    override fun getAddress(latLng: LatLng): Observable<Address> {
        return Observable.create { e ->
            val addresses = Geocoder(context, Locale.getDefault()).getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1)
            if (addresses == null || addresses.isEmpty()) {
                e.onError(IOException("address not found"))
            } else {
                e.onNext(addresses[0])
                e.onComplete()
            }
        }
    }
}