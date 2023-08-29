package com.frankuzi.passwordstorage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frankuzi.passwordstorage.domain.model.CreatedPassword
import com.frankuzi.passwordstorage.domain.model.UploadedPasswords

@Database(
    version = 1,
    entities = [
        CreatedPassword::class,
        UploadedPasswords::class
    ])
abstract class PasswordsDatabase: RoomDatabase() {
    abstract fun createdPasswordsDao(): CreatedPasswordsDao
    abstract fun uploadedPasswordsDao(): UploadedPasswordsDao
}