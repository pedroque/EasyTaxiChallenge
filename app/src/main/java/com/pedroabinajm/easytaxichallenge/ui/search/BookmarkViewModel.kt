package com.pedroabinajm.easytaxichallenge.ui.search

import android.arch.lifecycle.MutableLiveData
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.BookmarkRepository
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.base.BaseViewModel
import com.pedroabinajm.easytaxichallenge.ui.commons.Resource
import io.reactivex.Observable


class BookmarkViewModel constructor(
        private val bookmarkRepository: BookmarkRepository,
        schedulerProvider: ISchedulerProvider
) : BaseViewModel(schedulerProvider) {
    val bookmarks = MutableLiveData<Resource<List<EasyPlace>>>()

    fun fetchBookmarks(): Observable<List<EasyPlace>> {
        bookmarks.value = Resource.loading(null)
        return execute(bookmarkRepository.getBookmarks(), {
            bookmarks.value = Resource.success(it)
        }, {
            bookmarks.value = Resource.error(it, null)
        })
    }

    fun addBookmark(easyPlace: EasyPlace, alias: String) {
        bookmarkRepository.addBookmark(easyPlace, alias)
    }

    fun removeBookmark(easyPlace: EasyPlace) {
        bookmarkRepository.removeBookmark(easyPlace)
    }
}