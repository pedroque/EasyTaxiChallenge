package com.pedroabinajm.easytaxichallenge.data.net

interface ApiConfig {

    val baseUrl: String

    fun log(): Boolean
}
