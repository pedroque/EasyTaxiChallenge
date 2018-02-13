package com.pedroabinajm.easytaxichallenge.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.view.View
import com.google.android.gms.common.api.ResolvableApiException
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
import com.pedroabinajm.easytaxichallenge.ui.search.BookmarkViewModel
import com.pedroabinajm.easytaxichallenge.ui.search.BookmarkViewModelFactory
import com.pedroabinajm.easytaxichallenge.ui.search.PlaceAliasDialog
import com.pedroabinajm.easytaxichallenge.ui.search.SearchPlaceActivity
import com.pedroabinajm.easytaxichallenge.utils.Constants
import javax.inject.Inject


class MapActivity : BaseActivity() {

    private var map: GoogleMap? = null
    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var dataBinding: ActivityMapBinding
    private var reason: Int = 0
    private var lastPlace = false
    private var place: EasyPlace? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var bookmarkViewModelFactory: BookmarkViewModelFactory

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

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.Permissions.LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    placeViewModel.fetchCurrentPlace(lastPlace)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.ReqCode.REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            placeViewModel.fetchCurrentPlace(lastPlace)
        } else if (requestCode == Constants.ReqCode.SEARCH_PLACE && resultCode == Activity.RESULT_OK) {
            val place = data?.getParcelableExtra<EasyPlace>(Constants.Arguments.PLACE)
            place?.let {
                placeViewModel.setPlace(place)
            }
        }
    }

    private fun init() {
        placeViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceViewModel::class.java)
        bookmarkViewModel = ViewModelProviders.of(this, bookmarkViewModelFactory).get(BookmarkViewModel::class.java)
    }

    private fun setUpView() {
        setUpMapFragment()
        dataBinding.placeView.addStatusBarMargin()
        dataBinding.placeView.setOnClickListener {
            navigateToSearch()
        }
        dataBinding.myLocationButton.setOnClickListener {
            getCurrentPlace(false)
        }
        dataBinding.favoriteButton.setOnClickListener {
            place?.let {
                if (!it.bookmark) {
                    showAliasDialog(it)
                } else {
                    removeBookmark(it)
                }
            }
        }
    }

    private fun removeBookmark(place: EasyPlace) {
        bookmarkViewModel.removeBookmark(place)
        place.bookmark = false
        placeViewModel.setPlace(place)
    }

    private fun showAliasDialog(place: EasyPlace) {
        PlaceAliasDialog.Builder()
                .positiveButton { _, alias ->
                    bookmarkViewModel.addBookmark(place, alias)
                    placeViewModel.setPlace(place)
                }
                .show(supportFragmentManager)
    }

    private fun navigateToSearch() {
        startActivityForResult(SearchPlaceActivity.getIntent(this), Constants.ReqCode.SEARCH_PLACE)
    }

    private fun setUpMapFragment() {
        val mapFragment = getMapFragment()
        mapFragment.getMapAsync { map ->
            setUpMap(map)
            setUpObservers()
        }
    }


    @VisibleForTesting
    fun getMapFragment(): MapFragment {
        return fragmentManager
                .findFragmentById(R.id.map) as MapFragment
    }

    private fun setUpMap(map: GoogleMap) {
        this.map = map
        this.map?.uiSettings?.isMapToolbarEnabled = false
        this.map?.uiSettings?.isMyLocationButtonEnabled = false
        this.map?.setOnCameraMoveStartedListener { r ->
            reason = r
        }
        this.map?.setOnCameraIdleListener {
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                placeViewModel.fetchPlace(getMapLatLng(map))
            }
        }
    }

    private fun getMapLatLng(it: GoogleMap): LatLng {
        return it.projection.fromScreenLocation(Point(
                dataBinding.centerView.x.toInt(),
                dataBinding.centerView.y.toInt()
        ))
    }

    private fun setUpObservers() {
        placeViewModel.place.observe(this, Observer<Resource<EasyPlace?>> {
            dataBinding.resource = it
            place = it?.data
            if (it?.status == Resource.Status.SUCCESS) {
                setMapLocation(it.data!!)
            } else if (it?.status == Resource.Status.ERROR) {
                it.error?.let {
                    if (it is ResolvableApiException) {
                        resolve(it)
                    }
                }
            }
        })
        if (placeViewModel.place.value == null) {
            getCurrentPlace(true)
        }
    }

    private fun resolve(e: ResolvableApiException) {
        try {
            e.startResolutionForResult(this,
                    Constants.ReqCode.REQUEST_CHECK_SETTINGS)
        } catch (ignore: IntentSender.SendIntentException) {
        }
    }

    private fun setMapLocation(place: EasyPlace) {
        map?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(place.latitude, place.longitude)))
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentPlace(lastPlace: Boolean) {
        this.lastPlace = lastPlace
        doOnCheckPermissions({
            placeViewModel.fetchCurrentPlace(lastPlace)
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    }
}