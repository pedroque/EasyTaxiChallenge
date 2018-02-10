package com.pedroabinajm.easytaxichallenge.extensions

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.pedroabinajm.easytaxichallenge.utils.Constants


fun AppCompatActivity.doOnCheckPermissions(vararg permissions: String, doOn: () -> Unit) {
    if (permissions.any { ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }) {
        ActivityCompat.requestPermissions(this,
                permissions,
                Constants.Permissions.LOCATION)
    } else {
        doOn()
    }
}