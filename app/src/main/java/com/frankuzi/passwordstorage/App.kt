package com.frankuzi.passwordstorage

import android.app.Application
import androidx.room.Room
import com.frankuzi.passwordstorage.data.local.PasswordsDatabase

class App : Application() {
    val database by lazy { Room.databaseBuilder(
        applicationContext,
        PasswordsDatabase::class.java,
        "passwords-database"
    ).build() }
}