package com.pedroabinajm.easytaxichallenge.data.entity.mapper


import android.location.Address
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace

interface AddressMapper {
    fun transform(address: Address): EasyPlace
}
