@file:Suppress("UNCHECKED_CAST")

package com.pedroabinajm.easytaxichallenge.ui.search

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.pedroabinajm.easytaxichallenge.data.repository.BookmarkRepository
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import javax.inject.Inject

class BookmarkViewModelFactory @Inject constructor(
        private val bookmarkRepository: BookmarkRepository,
        private val schedulerProvider: ISchedulerProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(bookmarkRepository, schedulerProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}