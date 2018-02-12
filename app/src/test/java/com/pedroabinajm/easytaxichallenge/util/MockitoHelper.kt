package com.pedroabinajm.easytaxichallenge.util

import org.mockito.Mockito


object MockitoHelper {
    @Suppress("UNCHECKED_CAST")
    fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}