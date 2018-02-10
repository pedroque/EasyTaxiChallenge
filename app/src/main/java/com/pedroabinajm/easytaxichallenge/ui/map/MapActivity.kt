package com.pedroabinajm.easytaxichallenge.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresPermission
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.extensions.doOnCheckPermissions
import com.pedroabinajm.easytaxichallenge.ui.base.BaseActivity
import com.pedroabinajm.easytaxichallenge.utils.Constants


class MapActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    var map: GoogleMap? = null
    var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        if (Build.VERSION.SDK_INT >= 23) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setUpGoogleApiClient()
        setUpMapFragment()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.Permissions.LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentPlace()
                }
            }
        }
    }

    private fun setUpGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    private fun setUpMapFragment() {
        val mapFragment = fragmentManager
                .findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync { map ->
            this.map = map
            this.map?.uiSettings?.isMapToolbarEnabled = false
            this.map?.uiSettings?.isMyLocationButtonEnabled = false
            doOnCheckPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, getCurrentPlace())
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    private fun getCurrentPlace() {
        val result = Places.PlaceDetectionApi
                .getCurrentPlace(googleApiClient, null)
        result.setResultCallback { likelyPlaces ->
            likelyPlaces[0]?.let {
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(it.place.latLng, 16f))
            }
            likelyPlaces.release()
        }
    }
}