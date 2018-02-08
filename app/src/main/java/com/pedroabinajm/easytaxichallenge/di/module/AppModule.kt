/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pedroabinajm.easytaxichallenge.di.module

import android.app.Application
import android.content.Context
import com.pedroabinajm.easytaxichallenge.app.BaseApp
import com.pedroabinajm.easytaxichallenge.app.Preferences
import com.pedroabinajm.easytaxichallenge.app.PreferencesImpl
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class AppModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun provideMiraApp() = application as BaseApp

    @Provides
    @Singleton
    internal fun provideApplication() = application

    @Provides
    @Singleton
    internal fun provideApplicationContext() = application.applicationContext

    @Provides
    @Singleton
    internal fun provideSharedPreferences() = application.getSharedPreferences("mira", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    internal fun providePreferences(preferences: PreferencesImpl): Preferences = preferences

    @Provides
    @Singleton
    internal fun provideSchedulerProvider(schedulerProvider: SchedulerProvider): ISchedulerProvider = schedulerProvider
}
