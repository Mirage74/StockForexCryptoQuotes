package com.balex.stockforexcryptoquotes.data.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object Storage {

    private const val FILE_NAME = "encrypted_preferences"
    private const val TOKEN_KEY = "shared_prefs_token"
    const val NO_USER_TOKEN_IN_SHARED_PREFERENCES = "NO_USER_TOKEN_IN_SHARED_PREFERENCES"
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)


    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        return (EncryptedSharedPreferences.create(
            FILE_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ))
    }

    fun saveToken(context: Context, token: String) {
        getEncryptedSharedPreferences(context).edit().apply {
            putString(TOKEN_KEY, token)
        }.apply()
    }

    fun getToken(context: Context): String {
        return getEncryptedSharedPreferences(context).getString(
            TOKEN_KEY,
            NO_USER_TOKEN_IN_SHARED_PREFERENCES
        ).toString()
    }
}