package com.loconav.locodriver.base

interface DataWrapper<T> {
    fun onFailure(throwable: Throwable?)
    fun onSuccess(data:T, successMessage: String)
}