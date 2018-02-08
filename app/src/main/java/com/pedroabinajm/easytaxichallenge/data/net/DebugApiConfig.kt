package com.pedroabinajm.easytaxichallenge.data.net

import javax.inject.Inject

class DebugApiConfig @Inject
constructor() : ApiConfig {

    override val baseUrl: String
        get() = "http://www.mocky.io/v2/"

    override fun log(): Boolean {
        return true
    }
}
