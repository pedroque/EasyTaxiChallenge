package com.pedroabinajm.easytaxichallenge.data.repository

import com.pedroabinajm.easytaxichallenge.app.Preferences
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.PlaceDataSource
import javax.inject.Inject


class PlaceRepositoryImpl @Inject constructor(
        private val placeDataSource: PlaceDataSource,
        private val preferences: Preferences
) : PlaceRepository {
    override fun getLastPlace(): EasyPlace? {
        return preferences.lastPlaceId?.let { placeDataSource.getPlace(it) }
    }

    override fun saveLastPlace(easyPlace: EasyPlace) {
        getLastPlace()?.let {
            if (!it.bookmark) {
                placeDataSource.delete(it.id)
            }
        }
        placeDataSource.savePlace(easyPlace)
        preferences.lastPlaceId = easyPlace.id
    }
}