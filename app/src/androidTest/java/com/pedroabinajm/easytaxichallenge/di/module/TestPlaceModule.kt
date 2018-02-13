package com.pedroabinajm.easytaxichallenge.di.module

import com.pedroabinajm.easytaxichallenge.data.location.Locator
import com.pedroabinajm.easytaxichallenge.data.location.PlaceAutocompleteProvider
import com.pedroabinajm.easytaxichallenge.data.location.TestLocator
import com.pedroabinajm.easytaxichallenge.data.location.TestPlaceAutocompleteProvider
import com.pedroabinajm.easytaxichallenge.data.repository.BookmarkRepository
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
import com.pedroabinajm.easytaxichallenge.data.repository.TestBookmarkRepository
import com.pedroabinajm.easytaxichallenge.data.repository.TestPlaceRepository
import com.pedroabinajm.easytaxichallenge.di.ActivityScope
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.map.ViewModelFactory
import com.pedroabinajm.easytaxichallenge.ui.search.BookmarkViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class TestPlaceModule {

    @Provides
    @Reusable
    internal fun providePlaceRepository(placeRepository: TestPlaceRepository): PlaceRepository = placeRepository

    @Provides
    @Reusable
    internal fun provideBookmarkRepository(bookmarkRepository: TestBookmarkRepository): BookmarkRepository = bookmarkRepository

    @Provides
    @Reusable
    internal fun provideLocator(locator: TestLocator): Locator = locator

    @Provides
    @Reusable
    internal fun providePlaceAutocompleteProvider(placeAutocompleteProvider: TestPlaceAutocompleteProvider):
            PlaceAutocompleteProvider = placeAutocompleteProvider

    @Provides
    @ActivityScope
    internal fun provideViewModelFactory(placeRepository: PlaceRepository, placeAutocompleteProvider: PlaceAutocompleteProvider,
                                         schedulerProvider: ISchedulerProvider) =
            ViewModelFactory(placeRepository, placeAutocompleteProvider, 0, schedulerProvider)

    @Provides
    @ActivityScope
    internal fun provideBookmarkViewModelFactory(bookmarkRepository: BookmarkRepository,
                                                 schedulerProvider: ISchedulerProvider) =
            BookmarkViewModelFactory(bookmarkRepository, schedulerProvider)
}