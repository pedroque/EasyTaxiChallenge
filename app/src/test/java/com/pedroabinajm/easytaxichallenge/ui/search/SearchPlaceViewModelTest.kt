package com.pedroabinajm.easytaxichallenge.ui.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.data.location.PlaceAutocompleteProvider
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.schedulers.TestSchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.commons.Resource
import com.pedroabinajm.easytaxichallenge.util.MockitoHelper
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.io.IOException

class SearchPlaceViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var placeAutcompleteProvider: PlaceAutocompleteProvider
    @Mock
    private lateinit var placeObserver: Observer<Resource<EasyPlace>>

    private lateinit var searchPlaceViewModel: SearchPlaceViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        searchPlaceViewModel = SearchPlaceViewModel(placeAutcompleteProvider, 0, TestSchedulerProvider())
    }

    @Test
    fun getLatLng() {
        searchPlaceViewModel.place.observeForever(placeObserver)
        Mockito.`when`(placeAutcompleteProvider.getLatLang(MockitoHelper.any()))
                .thenReturn(Observable.just(LatLng(-23.0, -46.0)))
        val place = EasyPlace()
        place.latitude = -23.0
        place.longitude = -46.0
        searchPlaceViewModel.getLatLng(place)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(place)
                .awaitTerminalEvent()
        verify(placeObserver).onChanged(Resource.loading(place))
        verify(placeObserver).onChanged(Resource.success(place))
    }

    @Test
    fun getLatLngError() {
        searchPlaceViewModel.place.observeForever(placeObserver)
        val error = IOException()
        Mockito.`when`(placeAutcompleteProvider.getLatLang(MockitoHelper.any()))
                .thenReturn(Observable.error(error))
        val place = EasyPlace()
        searchPlaceViewModel.getLatLng(place)
                .test()
                .assertError(error)
                .assertNotComplete()
                .assertNoValues()
                .awaitTerminalEvent()
        verify(placeObserver).onChanged(Resource.loading(place))
        verify(placeObserver).onChanged(Resource.error(error, place))
    }

}