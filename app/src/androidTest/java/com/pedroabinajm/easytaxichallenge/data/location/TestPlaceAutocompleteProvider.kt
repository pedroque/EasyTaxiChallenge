package com.pedroabinajm.easytaxichallenge.data.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable
import javax.inject.Inject


class TestPlaceAutocompleteProvider @Inject constructor()  : PlaceAutocompleteProvider {
    override var googleApiClient: GoogleApiClient?
        get() = null
        set(value) {}

    override fun getPlaces(text: String): Observable<List<EasyPlace>> {
        val places = listOf(
                EasyPlace(
                        "1",
                        "Avenida dos Ourives, 480",
                        0.0,
                        0.0,
                        "Pq. Bristol, Sao Paulo",
                        null,
                        false
                ), EasyPlace(
                "2",
                "Rua Luis Gois, 206",
                0.0,
                0.0,
                "Vila Mariana, Sao Paulo",
                null,
                false
        ), EasyPlace(
                "3",
                "Rua Paraguassu, 253",
                0.0,
                0.0,
                "Olimpico, Sao Caetano do Sul",
                null,
                false
        )
        )
        return Observable.just(places)
    }

    override fun getLatLang(place: EasyPlace): Observable<LatLng> {
        return Observable.just(LatLng(-23.0, -46.0))
    }
}