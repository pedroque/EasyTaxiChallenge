package com.pedroabinajm.easytaxichallenge.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Named(val value: String = "")
