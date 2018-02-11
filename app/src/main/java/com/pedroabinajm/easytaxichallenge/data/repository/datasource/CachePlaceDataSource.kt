package com.pedroabinajm.easytaxichallenge.data.repository.datasource

import com.pedroabinajm.easytaxichallenge.data.dao.PlaceDao
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import javax.inject.Inject


class CachePlaceDataSource @Inject constructor(
        private val placeDao: PlaceDao
) : PlaceDataSource {
    override fun delete(id: String) {
        placeDao.delete(id)
    }

    override fun getPlace(id: String): EasyPlace? {
        return placeDao.find(id)
    }

    override fun savePlace(place: EasyPlace) {
        placeDao.save(place)
    }
}