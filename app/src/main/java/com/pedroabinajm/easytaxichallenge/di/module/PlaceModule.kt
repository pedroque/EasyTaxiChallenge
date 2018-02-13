package com.pedroabinajm.easytaxichallenge.di.module

import com.pedroabinajm.easytaxichallenge.data.dao.PlaceDao
import com.pedroabinajm.easytaxichallenge.data.dao.PlaceDaoImpl
import com.pedroabinajm.easytaxichallenge.data.entity.mapper.AddressMapper
import com.pedroabinajm.easytaxichallenge.data.entity.mapper.AddressMapperImpl
import com.pedroabinajm.easytaxichallenge.data.entity.mapper.BookmarkMapper
import com.pedroabinajm.easytaxichallenge.data.entity.mapper.BookmarkMapperImpl
import com.pedroabinajm.easytaxichallenge.data.location.FusedLocator
import com.pedroabinajm.easytaxichallenge.data.location.Locator
import com.pedroabinajm.easytaxichallenge.data.location.PlaceAutocompleteProvider
import com.pedroabinajm.easytaxichallenge.data.location.PlaceAutocompleteProviderImpl
import com.pedroabinajm.easytaxichallenge.data.repository.BookmarkRepository
import com.pedroabinajm.easytaxichallenge.data.repository.BookmarkRepositoryImpl
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepositoryImpl
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.*
import com.pedroabinajm.easytaxichallenge.di.ActivityScope
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.map.ViewModelFactory
import com.pedroabinajm.easytaxichallenge.ui.search.BookmarkViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Named


@Module
class PlaceModule {
    @Provides
    @Reusable
    internal fun providePlaceDao(placeDao: PlaceDaoImpl): PlaceDao = placeDao

    @Provides
    @Reusable
    @Named("cloud")
    internal fun provideCloudBookmarkDataSource(cloudBookmarksDataSource: CloudBookmarksDataSource):
            BookmarkDataSource = cloudBookmarksDataSource

    @Provides
    @Reusable
    @Named("cache")
    internal fun provideCacheBookmarkDataSource(cacheBookmarkDataSource: CacheBookmarkDataSource):
            BookmarkDataSource = cacheBookmarkDataSource

    @Provides
    @Reusable
    internal fun provideBookmarkMapper(bookmarkMapper: BookmarkMapperImpl):
            BookmarkMapper = bookmarkMapper

    @Provides
    @Reusable
    internal fun provideBookmarkRepository(bookmarkRepository: BookmarkRepositoryImpl):
            BookmarkRepository = bookmarkRepository

    @Provides
    @Reusable
    internal fun provideCachePlaceDataSource(placeDataSource: CachePlaceDataSource): PlaceDataSource = placeDataSource

    @Provides
    @Reusable
    internal fun provideLocator(locator: FusedLocator): Locator = locator

    @Provides
    @Reusable
    internal fun provideAddressMapper(addressMapper: AddressMapperImpl): AddressMapper = addressMapper

    @Provides
    @Reusable
    internal fun provideCloudAddressDataSource(addressDataSource: CloudAddressDataSource): AddressDataSource = addressDataSource

    @Provides
    @Reusable
    internal fun providePlaceRepository(placeRepository: PlaceRepositoryImpl): PlaceRepository = placeRepository

    @Provides
    @Reusable
    internal fun providePlaceAutocompleteProvider(placeAutocompleteProvider: PlaceAutocompleteProviderImpl):
            PlaceAutocompleteProvider = placeAutocompleteProvider

    @Provides
    @ActivityScope
    internal fun provideViewModelFactory(placeRepository: PlaceRepository,
                                         placeAutocompleteProvider: PlaceAutocompleteProvider,
                                         schedulerProvider: ISchedulerProvider) =
            ViewModelFactory(placeRepository, placeAutocompleteProvider, 500, schedulerProvider)

    @Provides
    @ActivityScope
    internal fun provideBookmarkViewModelFactory(bookmarkRepository: BookmarkRepository,
                                                 schedulerProvider: ISchedulerProvider) =
            BookmarkViewModelFactory(bookmarkRepository, schedulerProvider)
}