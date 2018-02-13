package com.pedroabinajm.easytaxichallenge.app

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesImpl @Inject
constructor(private val prefs: SharedPreferences) : Preferences {

    companion object {
        @JvmField
        val LAST_PLACE_ID = "last_place_id"
        @JvmField
        val BOOKMARKS_FETCHED = "bookmarks_fetched"
    }

    override var lastPlaceId: String?
        get() = prefs.getString(LAST_PLACE_ID, null)
        set(value) = prefs.edit().putString(LAST_PLACE_ID, value).apply()

    override var bookmarksFetched: Boolean
        get() = prefs.getBoolean(BOOKMARKS_FETCHED, false)
        set(value) = prefs.edit().putBoolean(BOOKMARKS_FETCHED, value).apply()

}