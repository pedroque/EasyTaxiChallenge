package com.pedroabinajm.easytaxichallenge.data.repository

import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable


interface BookmarkRepository {
    fun getBookmarks(): Observable<List<EasyPlace>>
    fun addBookmark(easyPlace: EasyPlace, alias: String)
    fun removeBookmark(easyPlace: EasyPlace)
}