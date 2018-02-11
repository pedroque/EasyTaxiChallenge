package com.pedroabinajm.easytaxichallenge.data.dao

import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace


interface PlaceDao : Dao<EasyPlace> {
    fun find(id: String): EasyPlace?
    fun delete(id: String)
}