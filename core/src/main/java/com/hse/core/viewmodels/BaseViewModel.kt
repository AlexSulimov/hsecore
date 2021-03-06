package com.hse.core.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hse.core.enums.LoadingState

abstract class BaseViewModel : ViewModel() {
    abstract val loadingState: MutableLiveData<LoadingState>?
    var params: ViewModelParams? = null
        set(value) {
            field = value
            for (p in paramsReadyListeners) p.invoke(value)
        }

    private var paramsReadyListeners = arrayListOf<(params: ViewModelParams?) -> Unit>()

    fun doOnParamsReady(unit: (params: ViewModelParams?) -> Unit) {
        paramsReadyListeners.add(unit)
    }
}

open class ViewModelParams