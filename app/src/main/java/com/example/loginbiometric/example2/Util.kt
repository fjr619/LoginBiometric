package com.example.loginbiometric.example2

import android.content.SharedPreferences

//ini digunakan untuk check apakah relogin atau login biasa
var SharedPreferences.username
    get() = getString("username", "")
    set(value) {
        edit().putString("username", value).apply()
    }

//save di encrypted pref, ini digunakan untuk kebutuhan login menggunakan biometric
var SharedPreferences.fpusername
    get() = getString("fpusername", "")
    set(value) {
        edit().putString("fpusername", value).apply()
    }

//save di encrypted pref, ini digunakan untuk kebutuhan login menggunakan biometric
var SharedPreferences.fppassword
    get() = getString("fppassword", "")
    set(value) {
        edit().putString("fppassword", value).apply()
    }

var SharedPreferences.fpenabled
    get() = getBoolean("fpenabled", false)
    set(value) {
        edit().putBoolean("fpenabled", value).apply()
    }

//untuk state dari biometric
enum class BiometricMode {
    SAVE, READ
}