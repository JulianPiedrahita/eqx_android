package com.example.eqx_android.util

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptoUtils {
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val SECRET = "1234567890123456" // Debe ser seguro y gestionado correctamente
    private const val IV = "abcdefghijklmnop" // 16 bytes

    private fun getKey(): SecretKey = SecretKeySpec(SECRET.toByteArray(), "AES")
    private fun getIv(): IvParameterSpec = IvParameterSpec(IV.toByteArray())

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, getKey(), getIv())
        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), getIv())
        val decoded = Base64.decode(data, Base64.DEFAULT)
        return String(cipher.doFinal(decoded))
    }
}
