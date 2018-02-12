package com.pedroabinajm.easytaxichallenge.di.module

import com.pedroabinajm.easytaxichallenge.data.repository.PlaceRepository
import com.pedroabinajm.easytaxichallenge.data.repository.TestPlaceRepository
import com.pedroabinajm.easytaxichallenge.di.ActivityScope
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.map.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class TestPlaceModule {

    @Provides
    @Reusable
    internal fun providePlaceRepository(placeRepository: TestPlaceRepository): PlaceRepository = placeRepository

    @Provides
    @ActivityScope
    internal fun provideViewModelFactory(placeRepository: PlaceRepository, schedulerProvider: ISchedulerProvider) =
            ViewModelFactory(placeRepository, schedulerProvider)
}