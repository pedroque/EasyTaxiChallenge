package com.pedroabinajm.easytaxichallenge.data.repository.datasource

import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace


interface PlaceDataSource {
    fun getPlace(id: String): EasyPlace?
    fun savePlace(place: EasyPlace)
    fun delete(id: String)
}