package com.ruialmeida.lumbridgelibrary.viewmodel.base

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ruialmeida.lumbridgelibrary.extensions.platform.livedata.update

abstract class BaseStatefulViewModel<State, ErrorState> : ViewModel() {

    private var currentState: State? = null
    private var currentErrorState: ErrorState? = null

    private val stateLive = MutableLiveData<State>()
    private val errorStateLive = MutableLiveData<ErrorState>()

    init {
        setup()
    }

    abstract fun setup()
    abstract fun getDefaultState(): State
    abstract fun getDefaultErrorState(): ErrorState

    protected fun updateState(newState: State) {
        currentState = newState
        stateLive.update(newState)
    }

    protected fun updateErrorState(newState: ErrorState) {
        currentErrorState = newState
        errorStateLive.update(newState)
    }

    //Exposed functions / parameters.
    fun getCurrentStateLive(): LiveData<State> = stateLive
    fun getCurrentState(): State = currentState ?: getDefaultState()
}