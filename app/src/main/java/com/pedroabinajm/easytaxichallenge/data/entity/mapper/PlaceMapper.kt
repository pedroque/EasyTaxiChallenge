package com.pedroabinajm.easytaxichallenge.data.entity.mapper


import com.google.android.gms.location.places.Place
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace

interface PlaceMapper {
    fun transform(place: Place): EasyPlace
}
