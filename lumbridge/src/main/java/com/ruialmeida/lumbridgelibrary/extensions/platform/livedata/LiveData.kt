package com.ruialmeida.lumbridgelibrary.extensions.platform.livedata

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.update(value: T) {
    if (Looper.getMainLooper().isCurrentThread) {
        //This means we are on the UI thread.
        setValue(value)
    } else {
        //Non UI thread.
        postValue(value)
    }
}