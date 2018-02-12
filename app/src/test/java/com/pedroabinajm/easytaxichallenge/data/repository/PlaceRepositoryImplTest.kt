package com.pedroabinajm.easytaxichallenge.data.repository

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import com.pedroabinajm.easytaxichallenge.app.Preferences
import com.pedroabinajm.easytaxichallenge.data.entity.mapper.AddressMapper
import com.pedroabinajm.easytaxichallenge.data.location.Locator
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.AddressDataSource
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.PlaceDataSource
import com.pedroabinajm.easytaxichallenge.util.MockitoHelper
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.IOException
import java.util.*

class PlaceRepositoryImplTest {

    @Mock
    private lateinit var placeDataSource: PlaceDataSource
    @Mock
    private lateinit var addressDataSource: AddressDataSource
    @Mock
    private lateinit var addressMapper: AddressMapper
    @Mock
    private lateinit var locator: Locator
    @Mock
    private lateinit var preferences: Preferences

    private lateinit var placeRepository: PlaceRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        placeRepository = PlaceRepositoryImpl(placeDataSource, addressDataSource, addressMapper,
                locator, preferences)
    }

    @Test
    fun getCurrentPlaceGivenLastPlaceFalse() {
        val address = Address(Locale.getDefault())
        Mockito.`when`(addressDataSource.getAddress(MockitoHelper.any()))
                .thenReturn(Observable.just(address))
        val place = EasyPlace()
        place.id = "some_id"
        Mockito.`when`(addressMapper.transform(MockitoHelper.any()))
                .thenReturn(place)
        val latLng = LatLng(0.0, 0.0)
        Mockito.`when`(locator.getCurrentLocation())
                .thenReturn(Observable.just(latLng))
        placeRepository.getCurrentPlace(false)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(place)
                .awaitTerminalEvent()
    }

    @Test
    fun getCurrentPlaceGivenLastPlaceFalseError() {
        val error = IOException()
        Mockito.`when`(locator.getCurrentLocation())
                .thenReturn(Observable.error(error))
        placeRepository.getCurrentPlace(false)
                .test()
                .assertNotComplete()
                .assertError(error)
                .assertNoValues()
                .awaitTerminalEvent()
    }

    @Test
    fun getCurrentPlaceGivenLastPlaceTrueNoPreviousPlace() {
        Mockito.`when`(preferences.lastPlaceId).thenReturn(null)
        Mockito.`when`(placeDataSource.getPlace(anyString())).thenReturn(null)
        val address = Address(Locale.getDefault())
        Mockito.`when`(addressDataSource.getAddress(MockitoHelper.any()))
                .thenReturn(Observable.just(address))
        val place = EasyPlace()
        place.id = "some_id"
        Mockito.`when`(addressMapper.transform(MockitoHelper.any()))
                .thenReturn(place)
        val latLng = LatLng(0.0, 0.0)
        Mockito.`when`(locator.getCurrentLocation())
                .thenReturn(Observable.just(latLng))
        placeRepository.getCurrentPlace(true)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(place)
                .awaitTerminalEvent()
    }

    @Test
    fun getCurrentPlaceGivenLastPlaceTruePreferencesIdNoPreviousPlace() {
        Mockito.`when`(preferences.lastPlaceId).thenReturn("previous_id")
        Mockito.`when`(placeDataSource.getPlace(anyString())).thenReturn(null)
        val address = Address(Locale.getDefault())
        Mockito.`when`(addressDataSource.getAddress(MockitoHelper.any()))
                .thenReturn(Observable.just(address))
        val place = EasyPlace()
        place.id = "some_id"
        Mockito.`when`(addressMapper.transform(MockitoHelper.any()))
                .thenReturn(place)
        val latLng = LatLng(0.0, 0.0)
        Mockito.`when`(locator.getCurrentLocation())
                .thenReturn(Observable.just(latLng))
        placeRepository.getCurrentPlace(true)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(place)
                .awaitTerminalEvent()
    }

    @Test
    fun getCurrentPlaceGivenLastPlaceTruePreviousPlace() {
        Mockito.`when`(preferences.lastPlaceId).thenReturn("previous_id")
        val previousPlace = EasyPlace()
        previousPlace.id = "preivous_id"
        Mockito.`when`(placeDataSource.getPlace(anyString())).thenReturn(previousPlace)
        placeRepository.getCurrentPlace(true)
                .test()
                .assertComplete()
                .assertNoErrors()
                .assertValue(previousPlace)
                .awaitTerminalEvent()
    }

    @Test
    fun saveLastPlaceGivenNoPreviousPlace() {
        Mockito.`when`(preferences.lastPlaceId).thenReturn(null)
        Mockito.`when`(placeDataSource.getPlace(anyString())).thenReturn(null)
        val place = EasyPlace()
        place.id = "some_id"
        placeRepository.saveLastPlace(place)
        verify(placeDataSource, never()).delete(anyString())
        verify(placeDataSource).savePlace(place)
        verify(preferences).lastPlaceId = anyString()
    }

    @Test
    fun saveLastPlaceGivenPreferencesIdNoPlace() {
        Mockito.`when`(preferences.lastPlaceId).thenReturn("some_id")
        Mockito.`when`(placeDataSource.getPlace(anyString())).thenReturn(null)
        val place = EasyPlace()
        place.id = "some_id"
        placeRepository.saveLastPlace(place)
        verify(placeDataSource, never()).delete(anyString())
        verify(placeDataSource).savePlace(place)
        verify(preferences).lastPlaceId = anyString()
    }

    @Test
    fun saveLastPlaceGivenPreviousPlace() {
        Mockito.`when`(preferences.lastPlaceId).thenReturn("previous_id")
        val previousPlace = EasyPlace()
        previousPlace.id = "previous_id"
        Mockito.`when`(placeDataSource.getPlace(anyString())).thenReturn(previousPlace)
        val place = EasyPlace()
        place.id = "some_id"
        placeRepository.saveLastPlace(place)
        verify(placeDataSource).delete("previous_id")
        verify(placeDataSource).savePlace(place)
        verify(preferences).lastPlaceId = anyString()
    }

    @Test
    fun saveLastPlaceGivenPreviousBookmarkPlace() {
        Mockito.`when`(preferences.lastPlaceId).thenReturn("previous_id")
        val previousPlace = EasyPlace()
        previousPlace.id = "previous_id"
        previousPlace.bookmark = true
        Mockito.`when`(placeDataSource.getPlace(anyString())).thenReturn(previousPlace)
        val place = EasyPlace()
        place.id = "some_id"
        placeRepository.saveLastPlace(place)
        verify(placeDataSource, never()).delete("previous_id")
        verify(placeDataSource).savePlace(place)
        verify(preferences).lastPlaceId = anyString()
    }

    @Test
    fun getPlace() {
        val address = Address(Locale.getDefault())
        Mockito.`when`(addressDataSource.getAddress(MockitoHelper.any()))
                .thenReturn(Observable.just(address))
        val place = EasyPlace()
        place.id = "some_id"
        Mockito.`when`(addressMapper.transform(MockitoHelper.any()))
                .thenReturn(place)
        placeRepository.getPlace(LatLng(0.0, 0.0))
                .test()
                .assertNoErrors()
                .assertComplete()
                .assertValue(place)
                .awaitTerminalEvent()
    }

    @Test
    fun getPlaceError() {
        val error = IOException()
        Mockito.`when`(addressDataSource.getAddress(MockitoHelper.any()))
                .thenReturn(Observable.error(error))
        placeRepository.getPlace(LatLng(0.0, 0.0))
                .test()
                .assertError(error)
                .assertNotComplete()
                .assertNoValues()
                .awaitTerminalEvent()
    }

}