package com.pedroabinajm.easytaxichallenge.data.repository.datasource

import com.pedroabinajm.easytaxichallenge.data.dao.PlaceDao
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable
import javax.inject.Inject


class CacheBookmarkDataSource @Inject constructor(
        private val placeDao: PlaceDao
) : BookmarkDataSource {
    override fun getBookmarks(): Observable<List<EasyPlace>> {
        return Observable.just(placeDao.findBookmarks())
    }
}