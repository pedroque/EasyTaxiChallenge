package com.pedroabinajm.easytaxichallenge.data.repository

import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import io.reactivex.Observable
import javax.inject.Inject


class TestBookmarkRepository @Inject constructor() : BookmarkRepository {
    override fun getBookmarks(): Observable<List<EasyPlace>> {
        val bookmarks = listOf(
                EasyPlace(
                        "1",
                        "Avenida dos Ourives, 480",
                        0.0,
                        0.0,
                        "Pq. Bristol, Sao Paulo",
                        "Origem",
                        true
                ), EasyPlace(
                "2",
                "Rua Luis Gois, 206",
                0.0,
                0.0,
                "Vila Mariana, Sao Paulo",
                "Atual",
                true
        ), EasyPlace(
                "3",
                "Rua Paraguassu, 253",
                0.0,
                0.0,
                "Olimpico, Sao Caetano do Sul",
                "Casa Da Dani",
                true
        )
        )
        return Observable.just(bookmarks)
    }

    override fun addBookmark(easyPlace: EasyPlace, alias: String) {
        easyPlace.bookmark = true
        easyPlace.alias = alias
    }

    override fun removeBookmark(easyPlace: EasyPlace) {
        easyPlace.bookmark = false
    }
}