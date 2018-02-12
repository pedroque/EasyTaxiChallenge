package com.pedroabinajm.easytaxichallenge.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.databinding.ActivityMapBinding
import com.pedroabinajm.easytaxichallenge.extensions.addStatusBarMargin
import com.pedroabinajm.easytaxichallenge.extensions.doOnCheckPermissions
import com.pedroabinajm.easytaxichallenge.ui.base.BaseActivity
import com.pedroabinajm.easytaxichallenge.ui.commons.Resource
import com.pedroabinajm.easytaxichallenge.utils.Constants
import javax.inject.Inject


class MapActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var map: GoogleMap? = null
    private var googleApiClient: GoogleApiClient? = null
    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var dataBinding: ActivityMapBinding
    private var reason: Int = 0

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
        init()
        setUpView()
    }

    private fun init() {
        placeViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel::class.java)
        setUpGoogleApiClient()
    }

    private fun setUpView() {
        setUpMapFragment()
        dataBinding.placeView.addStatusBarMargin()
        dataBinding.placeView.setOnClickListener {
            Toast.makeText(this, "navigate to bookmarks", Toast.LENGTH_SHORT).show()
        }
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
            this.map?.setOnCameraMoveStartedListener { r ->
                reason = r
            }
            this.map?.setOnCameraIdleListener {
                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    map?.let {
                        val target = it.projection.fromScreenLocation(Point(
                                dataBinding.centerView.x.toInt(),
                                dataBinding.centerView.y.toInt()
                        ))
                        placeViewModel.fetchPlace(target)
                    }
                }
            }
            setUpObservers()
        }
    }

    private fun setUpObservers() {
        placeViewModel.place.observe(this, Observer<Resource<EasyPlace?>> {
            dataBinding.resource = it
            if (it?.status == Resource.Status.SUCCESS) {
                if (it.data == null) {
                    getCurrentPlace()
                } else {
                    setMapLocation(it.data)
                }
            }
        })
        if (placeViewModel.place.value == null) {
            placeViewModel.fetchLastPlace()
        }
    }

    private fun setMapLocation(place: EasyPlace) {
        map?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(place.latitude, place.longitude)))
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