package com.pedroabinajm.easytaxichallenge.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.support.annotation.RequiresPermission
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.utils.DateHelper
import io.reactivex.Observable
import javax.inject.Inject


class FusedLocator @Inject constructor(
        private val context: Context
) : LifecycleObserver, Locator {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var requestingLocationUpdates = false
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    @RequiresPermission(anyOf = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    override fun getCurrentLocation(): Observable<LatLng> {
        return Observable.create { e ->
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            e.onNext(LatLng(location.latitude, location.longitude))
                            e.onComplete()
                        } else {
                            checkLocationSettings({
                                requestingLocationUpdates = true
                                locationCallback = object : LocationCallback() {
                                    override fun onLocationResult(result: LocationResult?) {
                                        result?.let {
                                            val latLng = getBestLocation(result)
                                            latLng?.let {
                                                e.onNext(it)
                                            }
                                            stopLocationUpdates()
                                            e.onComplete()
                                        }
                                    }

                                    override fun onLocationAvailability(availability: LocationAvailability) {
                                        super.onLocationAvailability(availability)
                                        if (!availability.isLocationAvailable) {
                                            e.onError(IllegalStateException())
                                        }
                                    }
                                }
                                fusedLocationClient.requestLocationUpdates(locationRequest(),
                                        locationCallback,
                                        null)
                            }, {
                                e.onError(it)
                            })
                        }
                    }
        }

    }

    private fun getBestLocation(result: LocationResult): LatLng? {
        val bestLocation = result.locations.minBy { it.accuracy }
        return bestLocation?.let { LatLng(it.latitude, it.longitude) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        Log.i("code_challenge", "onPause")
        if (requestingLocationUpdates) {
            stopLocationUpdates()
        }
    }

    private fun stopLocationUpdates() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        locationCallback = null
        requestingLocationUpdates = false
    }

    private fun checkLocationSettings(onSuccess: () -> Unit,
                                      onFailure: (e: ResolvableApiException) -> Unit) {
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest())
        val client = LocationServices.getSettingsClient(context)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            onSuccess()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                onFailure(e)
            }
        }
    }

    private fun locationRequest(): LocationRequest {
        val request = LocationRequest()
        request.numUpdates = 1
        request.expirationTime = DateHelper.SECOND * 30
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return request
    }
}
