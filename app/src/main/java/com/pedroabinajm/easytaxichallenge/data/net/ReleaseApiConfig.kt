package com.pedroabinajm.easytaxichallenge.data.net

import javax.inject.Inject

class ReleaseApiConfig @Inject
constructor() : ApiConfig {

    override val baseUrl: String
        get() = "http://www.mocky.io/v2/"

    override fun log(): Boolean {
        return false
    }
}
