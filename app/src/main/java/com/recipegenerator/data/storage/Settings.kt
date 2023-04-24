package com.recipegenerator.data.storage

import android.content.Context

open class Settings(context: Context) {

    private val mPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun getInt(key: String, defValue: Int) = mPrefs.getInt(key, defValue)

    fun getInt(key: String) = getInt(key, 0)

    fun setInt(key: String, value: Int) = mPrefs.edit().putInt(key, value).apply()

    fun getLong(key: String, defValue: Long) = mPrefs.getLong(key, defValue)

    fun getLong(key: String) = getLong(key, 0L)

    fun setLong(key: String, value: Long) = mPrefs.edit().putLong(key, value).apply()

    fun getBoolean(key: String, defValue: Boolean) = mPrefs.getBoolean(key, defValue)

    fun getBoolean(key: String) = getBoolean(key, false)

    fun setBoolean(key: String, value: Boolean) = mPrefs.edit().putBoolean(key, value).apply()

    fun getFloat(key: String, defValue: Float) = mPrefs.getFloat(key, defValue)

    fun getFloat(key: String) = getFloat(key, 0.0f)

    fun setFloat(key: String, value: Float) = mPrefs.edit().putFloat(key, value).apply()

    fun getDouble(key: String, defValue: Double) =
        mPrefs.getFloat(key, defValue.toFloat()).toDouble()

    fun getDouble(key: String) = getDouble(key, 0.0)

    fun setDouble(key: String, value: Double) = mPrefs.edit().putFloat(key, value.toFloat()).apply()

    fun getString(key: String, defValue: String) = mPrefs.getString(key, defValue)

    fun getString(key: String) = getString(key, "")

    fun setString(key: String, value: String) = mPrefs.edit().putString(key, value).apply()

    fun contains(key: String) = mPrefs.contains(key)

    fun remove(key: String) = mPrefs.edit().remove(key).apply()

    fun remove(vararg keys: String) {
        mPrefs.edit().run {
            keys.forEach { remove(it) }
            apply()
        }
    }

    fun clear() = mPrefs.edit().clear().apply()

}