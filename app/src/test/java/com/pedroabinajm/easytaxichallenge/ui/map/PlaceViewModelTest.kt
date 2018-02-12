package com.pedroabinajm.easytaxichallenge.ui.map

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
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
import org.mockito.MockitoAnnotations
import java.io.IOException

class PlaceViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var placeRepository: PlaceRepository
    @Mock
    private lateinit var placeObserver: Observer<Resource<EasyPlace?>>

    private lateinit var placeViewModel: PlaceViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        placeViewModel = PlaceViewModel(placeRepository, TestSchedulerProvider())
    }

    @Test
    fun fetchCurrentPlace() {
        placeViewModel.place.observeForever(placeObserver)
        val easyPlace = EasyPlace()
        Mockito.`when`(placeRepository.getCurrentPlace(Mockito.anyBoolean()))
                .thenReturn(Observable.just(easyPlace))
        placeViewModel.fetchCurrentPlace(true)
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(easyPlace)
                .awaitTerminalEvent()
        Mockito.verify(placeObserver).onChanged(Resource.loading(null))
        Mockito.verify(placeObserver).onChanged(Resource.success(easyPlace))
    }

    @Test
    fun fetchCurrentPlaceError() {
        placeViewModel.place.observeForever(placeObserver)
        val error = IOException()
        Mockito.`when`(placeRepository.getCurrentPlace(Mockito.anyBoolean()))
                .thenReturn(Observable.error(error))
        placeViewModel.fetchCurrentPlace(true)
                .test()
                .assertError(error)
                .assertNotComplete()
                .assertNoValues()
                .awaitTerminalEvent()
        Mockito.verify(placeObserver).onChanged(Resource.loading(null))
        Mockito.verify(placeObserver).onChanged(Resource.error(error, null))
    }

    @Test
    fun fetchPlace() {
        placeViewModel.place.observeForever(placeObserver)
        val easyPlace = EasyPlace()
        Mockito.`when`(placeRepository.getPlace(MockitoHelper.any()))
                .thenReturn(Observable.just(easyPlace))
        placeViewModel.fetchPlace(LatLng(0.0, 0.0))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(easyPlace)
                .awaitTerminalEvent()
        Mockito.verify(placeObserver).onChanged(Resource.loading(null))
        Mockito.verify(placeObserver).onChanged(Resource.success(easyPlace))
    }

    @Test
    fun fetchPlaceError() {
        placeViewModel.place.observeForever(placeObserver)
        val error = IOException()
        Mockito.`when`(placeRepository.getPlace(MockitoHelper.any()))
                .thenReturn(Observable.error(error))
        placeViewModel.fetchPlace(LatLng(0.0, 0.0))
                .test()
                .assertError(error)
                .assertNotComplete()
                .assertNoValues()
                .awaitTerminalEvent()
        Mockito.verify(placeObserver).onChanged(Resource.loading(null))
        Mockito.verify(placeObserver).onChanged(Resource.error(error, null))
    }

}