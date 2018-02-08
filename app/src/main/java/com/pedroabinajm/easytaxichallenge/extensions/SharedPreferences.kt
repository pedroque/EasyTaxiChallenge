package com.pedroabinajm.easytaxichallenge.extensions

import android.content.SharedPreferences
import java.util.*


fun SharedPreferences.getDate(key: String): Date? {
    return if (getLong(key, 0) >= 0) Date(getLong(key, 0)) else null
}

fun SharedPreferences.Editor.putDate(key: String, date: Date?) : SharedPreferences.Editor {
    putLong(key, date?.time ?: 0)
    return this
}