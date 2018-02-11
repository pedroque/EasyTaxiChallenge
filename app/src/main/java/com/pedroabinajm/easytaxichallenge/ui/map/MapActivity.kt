package com.pedroabinajm.easytaxichallenge.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.extensions.doOnCheckPermissions
import com.pedroabinajm.easytaxichallenge.ui.base.BaseActivity
import com.pedroabinajm.easytaxichallenge.utils.Constants
import javax.inject.Inject


class MapActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    var map: GoogleMap? = null
    var googleApiClient: GoogleApiClient? = null
    lateinit var placeViewModel: PlaceViewModel
    private lateinit var dataBinding: MapActivityClassBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var placeDetection: PlaceDetection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 23) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_map, null, false)
        setContentView(dataBinding.root)
        placeViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel::class.java)
        setUpGoogleApiClient()
        setUpMapFragment()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.Permissions.LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleApiClient?.let {
                        placeDetection.getCurrentPlace(it, {
                            placeViewModel.savePlace(it)
                        })
                    }
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
            setUpObservers()
        }
    }

    private fun setUpObservers() {
        placeViewModel.place.observe(this, Observer<EasyPlace> {
            dataBinding.place = it
            if (it == null) {
                getCurrentPlace()
            } else {
                setMapLocation(it)
            }
        })
        if (placeViewModel.place.value == null) {
            placeViewModel.fetchLastPlace()
        }
    }

    private fun setMapLocation(place: EasyPlace) {
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(place.latitude, place.longitude), 16f))
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentPlace() {
        googleApiClient?.let {
            doOnCheckPermissions({
                placeDetection.getCurrentPlace(it, {
                    placeViewModel.savePlace(it)
                })
            }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}