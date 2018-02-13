package com.pedroabinajm.easytaxichallenge.data.location

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallbacks
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompletePredictionBuffer
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable
import javax.inject.Inject


class PlaceAutocompleteProviderImpl @Inject constructor() : PlaceAutocompleteProvider {
    override var googleApiClient: GoogleApiClient? = null

    override fun getPlaces(text: String): Observable<List<EasyPlace>> {
        return Observable.create { emitter ->
            val predictions = Places.GeoDataApi.getAutocompletePredictions(googleApiClient, text, null, null)
            predictions.setResultCallback(object : ResultCallbacks<AutocompletePredictionBuffer>() {
                override fun onSuccess(results: AutocompletePredictionBuffer) {
                    val places = ArrayList<EasyPlace>(results.count)
                    for (i in 0 until results.count) {
                        val result = results.get(i)
                        val place = EasyPlace()
                        place.id = result.placeId!!
                        place.name = result.getPrimaryText(null).toString()
                        place.description = result.getSecondaryText(null).toString()
                        places.add(place)
                    }
                    results.release()
                    emitter.onNext(places)
                    emitter.onComplete()
                }

                override fun onFailure(status: Status) {
                    emitter.onError(Exception(status.statusMessage))
                }
            })
        }
    }


    override fun getLatLang(place: EasyPlace): Observable<LatLng> {
        return Observable.create { emitter ->
            val placeBuffer = Places.GeoDataApi.getPlaceById(
                    googleApiClient,
                    place.id
            )
            placeBuffer.setResultCallback(object : ResultCallbacks<PlaceBuffer>() {
                override fun onSuccess(places: PlaceBuffer) {
                    if (places.count > 0) {
                        val latLng = places.get(0).latLng
                        emitter.onNext(LatLng(latLng.latitude, latLng.longitude))
                        emitter.onComplete()
                    } else {
                        emitter.onError(Exception("no place was found"))
                    }
                }

                override fun onFailure(status: Status) {
                    emitter.onError(Exception(status.statusMessage))
                }
            })
        }
    }
}