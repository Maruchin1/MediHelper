package com.example.medihelper.custom

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class ActionLiveData<T> : MutableLiveData<T>() {

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasObservers()) {
            throw Throwable("Only one observer at a time may subscribe to ActionLiveData")
        }
        super.observe(owner, Observer {
            if (it != null) {
                observer.onChanged(it)
                value = null
            }
        })
    }

    @MainThread
    fun sendAction(value: T? = null) {
        this.value = value
    }
}