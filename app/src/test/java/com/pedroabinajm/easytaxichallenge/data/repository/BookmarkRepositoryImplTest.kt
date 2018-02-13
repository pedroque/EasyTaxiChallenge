package com.pedroabinajm.easytaxichallenge.data.repository

import com.pedroabinajm.easytaxichallenge.app.Preferences
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.BookmarkDataSource
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.PlaceDataSource
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.io.IOException

class BookmarkRepositoryImplTest {
    @Mock
    private lateinit var placeDataSource: PlaceDataSource
    @Mock
    private lateinit var cloudBookmarkDataSource: BookmarkDataSource
    @Mock
    private lateinit var cacheBookmarkDataSource: BookmarkDataSource
    @Mock
    private lateinit var preferences: Preferences

    private lateinit var bookmarkRepository: BookmarkRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        bookmarkRepository = BookmarkRepositoryImpl(placeDataSource, cloudBookmarkDataSource,
                cacheBookmarkDataSource, preferences)
    }

    @Test
    fun addBookmark() {
        val place = EasyPlace()
        val alias = "alias"
        bookmarkRepository.addBookmark(place, alias)
        verify(placeDataSource).savePlace(place)
        assertEquals(true, place.bookmark)
        assertEquals(alias, place.alias)
    }

    @Test
    fun removeBookmarkGivenNoLastPlace() {
        Mockito.`when`(preferences.lastPlaceId)
                .thenReturn(null)
        val place = EasyPlace()
        place.bookmark = true
        place.id = "id"
        bookmarkRepository.removeBookmark(place)
        verify(placeDataSource).delete("id")
    }

    @Test
    fun removeBookmarkGivenDifferentLastPlace() {
        Mockito.`when`(preferences.lastPlaceId)
                .thenReturn("some_id")
        val place = EasyPlace()
        place.bookmark = true
        place.id = "id"
        bookmarkRepository.removeBookmark(place)
        verify(placeDataSource).delete("id")
    }

    @Test
    fun removeBookmarkGivenSameLastPlace() {
        Mockito.`when`(preferences.lastPlaceId)
                .thenReturn("id")
        val place = EasyPlace()
        place.bookmark = true
        place.id = "id"
        bookmarkRepository.removeBookmark(place)
        assertEquals(false, place.bookmark)
        verify(placeDataSource).savePlace(place)
    }

    @Test
    fun getBookmarksGivenBookmarksNotFetched() {
        Mockito.`when`(preferences.bookmarksFetched)
                .thenReturn(false)
        val cacheBookmarks = listOf(EasyPlace(), EasyPlace(), EasyPlace())
        Mockito.`when`(cacheBookmarkDataSource.getBookmarks())
                .thenReturn(Observable.just(cacheBookmarks))
        val cloudBookmarks = listOf(EasyPlace(), EasyPlace())
        Mockito.`when`(cloudBookmarkDataSource.getBookmarks())
                .thenReturn(Observable.just(cloudBookmarks))
        val test = bookmarkRepository.getBookmarks()
                .test()
        test.awaitTerminalEvent()
        test.assertNoErrors()
                .assertComplete()
                .assertValues(cloudBookmarks, cacheBookmarks)
        verify(preferences).bookmarksFetched = true
    }

    @Test
    fun getBookmarksGivenBookmarksNotFetchedError() {
        Mockito.`when`(preferences.bookmarksFetched)
                .thenReturn(false)
        val cacheBookmarks = listOf(EasyPlace(), EasyPlace(), EasyPlace())
        Mockito.`when`(cacheBookmarkDataSource.getBookmarks())
                .thenReturn(Observable.just(cacheBookmarks))
        val error = IOException()
        Mockito.`when`(cloudBookmarkDataSource.getBookmarks())
                .thenReturn(Observable.error(error))
        val test = bookmarkRepository.getBookmarks()
                .test()
        test.awaitTerminalEvent()
        test.assertNotComplete()
                .assertError(error)
                .assertNoValues()
        verify(preferences, never()).bookmarksFetched = true
    }

    @Test
    fun getBookmarksGivenBookmarksFetched() {
        Mockito.`when`(preferences.bookmarksFetched)
                .thenReturn(true)
        val cacheBookmarks = listOf(EasyPlace(), EasyPlace(), EasyPlace())
        Mockito.`when`(cacheBookmarkDataSource.getBookmarks())
                .thenReturn(Observable.just(cacheBookmarks))
        val cloudBookmarks = listOf(EasyPlace(), EasyPlace())
        Mockito.`when`(cloudBookmarkDataSource.getBookmarks())
                .thenReturn(Observable.just(cloudBookmarks))
        val test = bookmarkRepository.getBookmarks()
                .test()
        test.awaitTerminalEvent()
        test.assertNoErrors()
                .assertComplete()
                .assertValue(cacheBookmarks)
    }

    @Test
    fun getBookmarksGivenBookmarksFetchedError() {
        Mockito.`when`(preferences.bookmarksFetched)
                .thenReturn(true)
        val error = IOException()
        Mockito.`when`(cacheBookmarkDataSource.getBookmarks())
                .thenReturn(Observable.error(error))
        val test = bookmarkRepository.getBookmarks()
                .test()
        test.awaitTerminalEvent()
        test.assertNotComplete()
                .assertError(error)
                .assertNoValues()
    }

}