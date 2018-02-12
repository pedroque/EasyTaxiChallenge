package com.pedroabinajm.easytaxichallenge.app

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesImpl @Inject
constructor(private val prefs: SharedPreferences) : Preferences {

    companion object {
        @JvmField
        val LAST_PLACE_ID = "last_place_id"
    }

    override var lastPlaceId: String?
        get() = prefs.getString(LAST_PLACE_ID, null)
        set(value) = prefs.edit().putString(LAST_PLACE_ID, value).apply()

}