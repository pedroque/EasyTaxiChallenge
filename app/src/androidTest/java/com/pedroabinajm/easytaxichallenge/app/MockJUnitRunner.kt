package com.pedroabinajm.easytaxichallenge.app

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

class MockJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return Instrumentation.newApplication(MockApplication::class.java, context)
    }
}