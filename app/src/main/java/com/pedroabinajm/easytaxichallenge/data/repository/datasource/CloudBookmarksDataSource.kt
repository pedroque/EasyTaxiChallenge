package com.pedroabinajm.easytaxichallenge.data.repository.datasource

import com.pedroabinajm.easytaxichallenge.data.entity.mapper.BookmarkMapper
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.net.BookmarkServices
import io.reactivex.Observable
import javax.inject.Inject


class CloudBookmarksDataSource @Inject constructor(
        private val bookmarkServices: BookmarkServices,
        private val bookmarkMapper: BookmarkMapper
) : BookmarkDataSource {
    override fun getBookmarks(): Observable<List<EasyPlace>> {
        return bookmarkServices.getBookmarks().map { it.favorites.map { bookmarkMapper.transform(it) } }
    }
}