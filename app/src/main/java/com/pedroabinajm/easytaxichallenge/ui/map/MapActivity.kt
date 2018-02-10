package com.pedroabinajm.easytaxichallenge.ui.map

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.ui.base.BaseActivity


class MapActivity : BaseActivity() {

    var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setUpMapFragment()
    }

    private fun setUpMapFragment() {
        val mapFragment = fragmentManager
                .findFragmentById(R.id.map) as MapFragment
        mapFragment.getMapAsync { map ->
            this.map = map
            this.map?.uiSettings?.isMapToolbarEnabled = false
            this.map?.uiSettings?.isMyLocationButtonEnabled = false
        }
    }
}