package com.pedroabinajm.easytaxichallenge.ui.commons

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

class AutoClearedValue<T> {
    var value: T? = null
        private set

    constructor(fragment: Fragment, value: T) {
        val fragmentManager = fragment.fragmentManager
        fragmentManager?.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentViewDestroyed(fm: FragmentManager?, f: Fragment?) {
                        super.onFragmentViewDestroyed(fm, f)
                        if (f == fragment) {
                            this@AutoClearedValue.value = null
                            fragmentManager.unregisterFragmentLifecycleCallbacks(this)
                        }
                    }
                }, false
        )
        this.value = value
    }

    @Suppress("unused")
    constructor(activity: Activity, value: T) {
        activity.application.registerActivityLifecycleCallbacks(
                object : Application.ActivityLifecycleCallbacks {
                    override fun onActivityDestroyed(a: Activity?) {
                        if (activity == a) {
                            this@AutoClearedValue.value = null
                            activity.application.unregisterActivityLifecycleCallbacks(this)
                        }
                    }
                    override fun onActivityPaused(p0: Activity?) {}
                    override fun onActivityResumed(p0: Activity?) {}
                    override fun onActivityStarted(p0: Activity?) {}
                    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {}
                    override fun onActivityStopped(p0: Activity?) {}
                    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {}
                }
        )
        this.value = value
    }

    fun get(): T = value!!
}