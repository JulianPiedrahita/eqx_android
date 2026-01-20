package com.example.eqx_android.util

import android.content.Context
import android.os.SystemClock

class LoginAttemptLimiter(private val context: Context) {
    companion object {
        private const val PREFS_NAME = "login_attempts"
        private const val KEY_ATTEMPTS = "attempts"
        private const val KEY_LAST_ATTEMPT = "last_attempt"
        private const val MAX_ATTEMPTS = 5
        private const val BLOCK_TIME_MS = 5 * 60 * 1000 // 5 minutos
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun registerAttempt() {
        val now = SystemClock.elapsedRealtime()
        val attempts = prefs.getInt(KEY_ATTEMPTS, 0) + 1
        prefs.edit().putInt(KEY_ATTEMPTS, attempts).putLong(KEY_LAST_ATTEMPT, now).apply()
    }

    fun isBlocked(): Boolean {
        val attempts = prefs.getInt(KEY_ATTEMPTS, 0)
        val last = prefs.getLong(KEY_LAST_ATTEMPT, 0)
        val now = SystemClock.elapsedRealtime()
        return attempts >= MAX_ATTEMPTS && (now - last) < BLOCK_TIME_MS
    }

    fun reset() {
        prefs.edit().clear().apply()
    }
}
