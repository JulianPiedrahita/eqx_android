package com.example.eqx_android.util

import android.util.Log

object Logger {
    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }
    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}
