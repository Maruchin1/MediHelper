package com.maruchin.medihelper.data.local.framework

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class SharedPrefLiveData<T> (
    private val sharedPreferences: SharedPreferences,
    private val preferenceKey: String,
    private val defaultValue: T
) : LiveData<T>() {

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == preferenceKey) {
            postValueFromSharedPref(key)
        }
    }

    override fun onActive() {
        super.onActive()
        postValueFromSharedPref(preferenceKey)
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun postValueFromSharedPref(key: String) {
        when (defaultValue) {
            is Int -> postValue(sharedPreferences.getInt(key, defaultValue) as T)
            is Long -> postValue(sharedPreferences.getLong(key, defaultValue) as T)
            is Float -> postValue(sharedPreferences.getFloat(key, defaultValue) as T)
            is Boolean -> postValue(sharedPreferences.getBoolean(key, defaultValue) as T)
            is String -> postValue(sharedPreferences.getString(key, defaultValue) as T)
            is Set<*> -> postValue(sharedPreferences.getStringSet(key, defaultValue as Set<String>) as T)
            else -> throw IllegalStateException("Unsupported type")
        }
    }
}