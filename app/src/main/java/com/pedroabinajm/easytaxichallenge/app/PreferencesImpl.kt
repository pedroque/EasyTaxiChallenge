package com.pedroabinajm.easytaxichallenge.app

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesImpl @Inject
constructor(private val prefs: SharedPreferences) : Preferences