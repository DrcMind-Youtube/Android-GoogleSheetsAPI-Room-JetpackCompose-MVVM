package com.drcmind.stockshare.util

sealed class Result<T>(val data : T? = null, val throwable: Throwable? = null){
    class Success<T>(data: T?) : Result<T>(data)
    class Error<T>(data: T?, throwable: Throwable) : Result<T>(data, throwable)
    class Loading<T> : Result<T>()
}
