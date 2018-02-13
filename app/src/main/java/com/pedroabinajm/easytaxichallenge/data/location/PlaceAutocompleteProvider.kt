package com.pedroabinajm.easytaxichallenge.data.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable


interface PlaceAutocompleteProvider {
    var googleApiClient: GoogleApiClient?

    fun getPlaces(text: String): Observable<List<EasyPlace>>
    fun getLatLang(place: EasyPlace): Observable<LatLng>
}