package com.pedroabinajm.easytaxichallenge.ui.search

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.databinding.ActivitySearchPlaceBinding
import com.pedroabinajm.easytaxichallenge.ui.base.BaseActivity
import com.pedroabinajm.easytaxichallenge.ui.commons.AutoClearedValue
import com.pedroabinajm.easytaxichallenge.ui.commons.Resource
import com.pedroabinajm.easytaxichallenge.ui.map.ViewModelFactory
import com.pedroabinajm.easytaxichallenge.utils.Constants
import javax.inject.Inject


class SearchPlaceActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var bookmarkViewModelFactory: BookmarkViewModelFactory

    private lateinit var dataBinding: ActivitySearchPlaceBinding
    private lateinit var searchPlaceViewModel: SearchPlaceViewModel
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var queryViewModel: AutoClearedValue<QueryViewModel>
    private var adapter: PlaceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_search_place, null, false)
        setContentView(dataBinding.root)
        init()
        setUpViews()
        bind()
    }

    override fun onConnectionFailed(result: ConnectionResult) {

    }

    private fun init() {
        searchPlaceViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchPlaceViewModel::class.java)
        bookmarkViewModel = ViewModelProviders.of(this, bookmarkViewModelFactory).get(BookmarkViewModel::class.java)
        queryViewModel = AutoClearedValue(this, QueryViewModel(searchPlaceViewModel.query))
        dataBinding.query = queryViewModel.value
        searchPlaceViewModel.placeAutocompleteProvider.googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()
    }

    private fun bind() {
        bookmarkViewModel.bookmarks.observe(this, Observer<Resource<List<EasyPlace>>> {
            dataBinding.resource = it
            if (it?.status == Resource.Status.SUCCESS) {
                when {
                    it.data == null || it.isEmpty -> setEmptyQuery()
                    else -> setPlaces(it.data)
                }
            }
        })
        searchPlaceViewModel.places.observe(this, Observer<Resource<List<EasyPlace>>> {
            dataBinding.resource = it
            if (it?.status == Resource.Status.SUCCESS) {
                when {
                    it.data == null -> fetchBookmarks()
                    it.isEmpty -> setEmptySearch()
                    else -> setPlaces(it.data)
                }
            }
        })
        searchPlaceViewModel.place.observe(this, Observer<Resource<EasyPlace>> {
            if (it?.status == Resource.Status.SUCCESS) {
                pickPlace(it.data!!)
            }
        })
        adapter?.clickSubject?.subscribe { position ->
            val place = adapter?.items?.get(position)
            place?.let { searchPlaceViewModel.getLatLng(it) }
        }
        adapter?.bookmarkSubject?.subscribe { position ->
            val place = adapter?.items?.get(position)
            place?.let {
                it.bookmark = !it.bookmark
                adapter?.notifyItemChanged(position)
                if (it.bookmark) {
                    showAliasDialog(it, position)
                } else {
                    removeBookmark(it)
                }
            }
        }
        queryViewModel.value?.data?.observe(this, Observer<String> { query ->
            searchPlaceViewModel.query = query!!
        })
    }

    private fun removeBookmark(place: EasyPlace) {
        bookmarkViewModel.removeBookmark(place)
    }

    private fun showAliasDialog(place: EasyPlace, position: Int) {
        PlaceAliasDialog.Builder()
                .negativeButton {
                    place.bookmark = false
                    adapter?.notifyItemChanged(position)
                }
                .positiveButton { _, alias ->
                    bookmarkViewModel.addBookmark(place, alias)
                    searchPlaceViewModel.getLatLng(place)
                }
                .show(supportFragmentManager)
    }

    private fun pickPlace(place: EasyPlace) {
        val data = Intent()
        data.putExtra(Constants.Arguments.PLACE, place)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun setPlaces(places: List<EasyPlace>) {
        adapter?.replace(places)
    }

    private fun fetchBookmarks() {
        bookmarkViewModel.fetchBookmarks()
    }

    private fun setEmptySearch() {
        dataBinding.errorText.setText(R.string.no_address_found)
    }

    private fun setEmptyQuery() {
        dataBinding.errorText.setText(R.string.search_address_content)
    }

    private fun setUpViews() {
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpRecycler()
    }


    private fun setUpRecycler() {
        dataBinding.placesRecycler.setHasFixedSize(true)
        dataBinding.placesRecycler.layoutManager = LinearLayoutManager(this)
        adapter = PlaceAdapter()
        adapter?.replace(mutableListOf())
        dataBinding.placesRecycler.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context): Intent {
            return Intent(context, SearchPlaceActivity::class.java)
        }
    }
}