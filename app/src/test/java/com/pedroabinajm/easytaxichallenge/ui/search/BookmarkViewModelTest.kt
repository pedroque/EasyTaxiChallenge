package com.pedroabinajm.easytaxichallenge.ui.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.BookmarkRepository
import com.pedroabinajm.easytaxichallenge.schedulers.TestSchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.commons.Resource
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.io.IOException

class BookmarkViewModelTest {
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var bookmarkRepository: BookmarkRepository
    @Mock
    private lateinit var placesObserver: Observer<Resource<List<EasyPlace>>>

    private lateinit var bookmarkViewModel: BookmarkViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        bookmarkViewModel = BookmarkViewModel(bookmarkRepository, TestSchedulerProvider())
    }

    @Test
    fun fetchBookmarks() {
        bookmarkViewModel.bookmarks.observeForever(placesObserver)
        val bookmarks = listOf(EasyPlace(), EasyPlace(), EasyPlace())
        Mockito.`when`(bookmarkRepository.getBookmarks())
                .thenReturn(Observable.just(bookmarks))
        bookmarkViewModel.fetchBookmarks()
                .test()
                .assertValue(bookmarks)
                .assertNoErrors()
                .assertComplete()
                .awaitTerminalEvent()
        verify(placesObserver).onChanged(Resource.loading(null))
        verify(placesObserver).onChanged(Resource.success(bookmarks))
    }

    @Test
    fun fetchBookmarksError() {
        bookmarkViewModel.bookmarks.observeForever(placesObserver)
        val error = IOException()
        Mockito.`when`(bookmarkRepository.getBookmarks())
                .thenReturn(Observable.error(error))
        bookmarkViewModel.fetchBookmarks()
                .test()
                .assertNoValues()
                .assertError(error)
                .assertNotComplete()
                .awaitTerminalEvent()
        verify(placesObserver).onChanged(Resource.loading(null))
        verify(placesObserver).onChanged(Resource.error(error, null))
    }

    @Test
    fun addBookmark() {
        val place = EasyPlace()
        val alias = "alias"
        bookmarkViewModel.addBookmark(place, alias)
        verify(bookmarkRepository).addBookmark(place, alias)
    }

    @Test
    fun removeBookmark() {
        val place = EasyPlace()
        bookmarkViewModel.removeBookmark(place)
        verify(bookmarkRepository).removeBookmark(place)
    }

}