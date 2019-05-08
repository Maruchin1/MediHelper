package com.example.medihelper

import android.app.Application
import android.util.Log

class MainApplication : Application() {

    private val TAG = MainApplication::class.simpleName

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        Repository.init(this)
    }

    companion object {
        val TAG = MainApplication::class.simpleName
    }
}