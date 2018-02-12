package com.pedroabinajm.easytaxichallenge.app

import android.app.Activity
import android.app.Application
import com.pedroabinajm.easytaxichallenge.di.component.DaggerTestComponent
import com.pedroabinajm.easytaxichallenge.di.module.TestAppModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class MockApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerTestComponent.builder()
                .application(this)
                .appModule(TestAppModule())
                .build()
                .inject(this)
    }
}