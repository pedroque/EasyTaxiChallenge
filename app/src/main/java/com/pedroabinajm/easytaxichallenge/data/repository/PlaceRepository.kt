package com.pedroabinajm.easytaxichallenge.data.repository

import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace


interface PlaceRepository {
    fun getLastPlace(): EasyPlace?
    fun saveLastPlace(easyPlace: EasyPlace)
}