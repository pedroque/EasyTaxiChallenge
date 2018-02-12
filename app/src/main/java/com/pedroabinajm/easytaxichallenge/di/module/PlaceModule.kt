package com.pedroabinajm.easytaxichallenge.di.module

import com.pedroabinajm.easytaxichallenge.data.dao.PlaceDao
import com.pedroabinajm.easytaxichallenge.data.dao.PlaceDaoImpl
import com.pedroabinajm.easytaxichallenge.data.entity.mapper.PlaceMapper
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepositoryImpl
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.AddressDataSource
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.CachePlaceDataSource
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.CloudAddressDataSource
import com.pedroabinajm.easytaxichallenge.data.repository.datasource.PlaceDataSource
import com.pedroabinajm.easytaxichallenge.di.ActivityScope
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.map.PlaceDetection
import com.pedroabinajm.easytaxichallenge.ui.map.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable


@Module
class PlaceModule {
    @Provides
    @Reusable
    internal fun providePlaceDao(placeDao: PlaceDaoImpl): PlaceDao = placeDao

    @Provides
    @Reusable
    internal fun provideCachePlaceDataSource(placeDataSource: CachePlaceDataSource): PlaceDataSource = placeDataSource

    @Provides
    @Reusable
    internal fun provideCloudAddressDataSource(addressDataSource: CloudAddressDataSource): AddressDataSource = addressDataSource

    @Provides
    @Reusable
    internal fun providePlaceRepository(placeRepository: PlaceRepositoryImpl): PlaceRepository = placeRepository

    @Provides
    @ActivityScope
    internal fun provideViewModelFactory(placeRepository: PlaceRepository, schedulerProvider: ISchedulerProvider) =
            ViewModelFactory(placeRepository, schedulerProvider)
}