package com.pedroabinajm.easytaxichallenge.data.cache

import com.pedroabinajm.easytaxichallenge.app.Preferences
import javax.inject.Inject


class BookmarkCacheImpl @Inject constructor(
        private val preferences: Preferences
) : BookmarkCache {
    override var fetched: Boolean
        get() = preferences.bookmarksFetched
        set(value) { preferences.bookmarksFetched = value}

}