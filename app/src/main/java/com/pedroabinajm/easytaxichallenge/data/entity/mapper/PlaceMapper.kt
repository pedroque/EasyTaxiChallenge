package com.pedroabinajm.easytaxichallenge.data.entity.mapper


import com.google.android.gms.location.places.Place
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import javax.inject.Inject

class PlaceMapper @Inject constructor() {
    fun transform(place: Place): EasyPlace {
        return EasyPlace(
                place.id,
                place.name.toString(),
                place.latLng.latitude,
                place.latLng.longitude,
                place.address.toString(),
                false
        )
    }
}
