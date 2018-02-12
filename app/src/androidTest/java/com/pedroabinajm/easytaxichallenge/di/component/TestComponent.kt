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

package com.pedroabinajm.easytaxichallenge.di.component

import android.app.Application
import com.pedroabinajm.easytaxichallenge.app.MockApplication
import com.pedroabinajm.easytaxichallenge.di.module.TestActivityModule
import com.pedroabinajm.easytaxichallenge.di.module.TestAppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidInjectionModule::class),
    (TestAppModule::class),
    (TestActivityModule::class)]
)
interface TestComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun appModule(appModule: TestAppModule): Builder

        fun build(): TestComponent
    }

    fun inject(app: MockApplication)
}