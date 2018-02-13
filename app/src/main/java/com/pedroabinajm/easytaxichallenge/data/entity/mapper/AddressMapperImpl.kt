package com.pedroabinajm.easytaxichallenge.data.entity.mapper


import android.location.Address
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import java.util.*
import javax.inject.Inject

class AddressMapperImpl @Inject constructor() : AddressMapper {
    override fun transform(address: Address): EasyPlace {
        val name = address.premises?.let { it } ?:
                address.thoroughfare?.let {
                    address.subThoroughfare?.let {
                        listOf(address.thoroughfare, address.subThoroughfare).joinToString(separator = ", ")
                    } ?: it
                } ?:
                "Unknown"
        val description = address.subLocality?.let {
            address.subAdminArea?.let {
                listOf(address.subLocality, address.subAdminArea).joinToString(separator = ", ")
            } ?: it
        }
        return EasyPlace(
                UUID.randomUUID().toString(),
                name,
                address.latitude,
                address.longitude,
                description,
                false
        )
    }
}
