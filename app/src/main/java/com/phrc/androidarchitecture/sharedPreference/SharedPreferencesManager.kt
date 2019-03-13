package com.phrc.androidarchitecture.sharedPreference

import android.content.SharedPreferences
import com.phrc.androidarchitecture.util.OpenClass
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@OpenClass
class SharedPreferencesManager(sharedPreferences: SharedPreferences) {

    enum class Key {
        TOKEN
    }

    var token by sharedPreferences.string("", Key.TOKEN.name)

    private inline fun <T> SharedPreferences.delegate(
            defaultValue: T, key: String,
            crossinline getter: SharedPreferences.(String, T) -> T,
            crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
    ): ReadWriteProperty<Any, T> =
            object : ReadWriteProperty<Any, T> {
                override fun getValue(thisRef: Any, property: KProperty<*>): T =
                        getter(key, defaultValue)!!

                override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
                        edit().setter(key, value).apply()
            }

    fun SharedPreferences.int(def: Int = 0, key: String): ReadWriteProperty<Any, Int> =
            delegate(
                    def,
                    key,
                    SharedPreferences::getInt,
                    SharedPreferences.Editor::putInt
            )

    final fun SharedPreferences.boolean(def: Boolean = false, key: String): ReadWriteProperty<Any, Boolean> =
            delegate(
                    def,
                    key,
                    SharedPreferences::getBoolean,
                    SharedPreferences.Editor::putBoolean
            )

    final fun SharedPreferences.string(def: String = "", key: String): ReadWriteProperty<Any, String> =
            delegate(
                    def,
                    key,
                    SharedPreferences::getString,
                    SharedPreferences.Editor::putString
            )
}