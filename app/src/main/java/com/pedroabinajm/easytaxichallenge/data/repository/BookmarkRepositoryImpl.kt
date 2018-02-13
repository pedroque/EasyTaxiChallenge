package com.pedroabinajm.easytaxichallenge.data.repository

import com.pedroabinajm.easytaxichallenge.app.Preferences
import com.pedroabinajm.easytaxichallenge.data.cache.BookmarkCache
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.BookmarkDataSource
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.PlaceDataSource
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named


class BookmarkRepositoryImpl @Inject constructor(
        private val placeDataSource: PlaceDataSource,
        private val bookmarkCache: BookmarkCache,
        @Named("cloud")
        private val cloudBookmarkDataSource: BookmarkDataSource,
        @Named("cache")
        private val cacheBookmarkDataSource: BookmarkDataSource,
        private val preferences: Preferences
) : BookmarkRepository {

    override fun addBookmark(easyPlace: EasyPlace) {
        easyPlace.bookmark = true
        placeDataSource.savePlace(easyPlace)
    }

    override fun removeBookmark(easyPlace: EasyPlace) {
        if (preferences.lastPlaceId == null || preferences.lastPlaceId != easyPlace.id) {
            placeDataSource.delete(easyPlace.id)
        } else {
            easyPlace.bookmark = false
            placeDataSource.savePlace(easyPlace)
        }
    }

    override fun getBookmarks(): Observable<List<EasyPlace>> {
        return if (!bookmarkCache.fetched) {
            cloudBookmarkDataSource.getBookmarks()
                    .map {
                        it.forEach { placeDataSource.savePlace(it) }
                        bookmarkCache.fetched = true
                        it
                    }
                    .mergeWith { cacheBookmarkDataSource.getBookmarks() }
        } else {
            cacheBookmarkDataSource.getBookmarks()
        }
    }
}