package com.pedroabinajm.easytaxichallenge.data.repository.datasource

import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable


interface BookmarkDataSource {
    fun getBookmarks(): Observable<List<EasyPlace>>
}
