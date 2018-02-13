package com.pedroabinajm.easytaxichallenge.data.net

import com.pedroabinajm.easytaxichallenge.data.entity.BookmarkResponseEntity
import io.reactivex.Observable
import retrofit2.http.GET


interface BookmarkServices {
    @GET("59dbaacc110000590007481d")
    fun getBookmarks() : Observable<BookmarkResponseEntity>
}