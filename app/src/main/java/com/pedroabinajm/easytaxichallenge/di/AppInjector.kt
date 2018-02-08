package com.pedroabinajm.easytaxichallenge.di

import com.pedroabinajm.easytaxichallenge.app.BaseApp
import com.pedroabinajm.easytaxichallenge.di.component.DaggerAppComponent
import com.pedroabinajm.easytaxichallenge.di.module.AppModule

object AppInjector {
    @JvmStatic
    fun init(app: BaseApp) {
        DaggerAppComponent.builder()
                .application(app)
                .appModule(AppModule(app))
                .build()
                .inject(app)
    }
}
