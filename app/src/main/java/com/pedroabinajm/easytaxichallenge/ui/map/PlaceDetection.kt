package com.pedroabinajm.easytaxichallenge.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.support.annotation.RequiresPermission
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.pedroabinajm.easytaxichallenge.data.entity.mapper.PlaceMapper
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import javax.inject.Inject


class PlaceDetection @Inject constructor(
        private val placeMapper: PlaceMapper
) {
    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getCurrentPlace(googleApiClient: GoogleApiClient, callback: (easyPlace: EasyPlace) -> Unit) {
        val result = Places.PlaceDetectionApi.getCurrentPlace(googleApiClient, null)
        result.setResultCallback { likelyPlaces ->
            likelyPlaces[0]?.let {
                callback(placeMapper.transform(it.place))
            }
            likelyPlaces.release()
        }
    }
}
