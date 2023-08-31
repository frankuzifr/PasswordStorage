package com.frankuzi.passwordstorage.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.frankuzi.passwordstorage.domain.model.Created
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.Uploaded

@Database(
    version = 1,
    entities = [
        Password::class,
        Created::class,
        Uploaded::class
    ])
abstract class PasswordsDatabase: RoomDatabase() {
    abstract fun createdPasswordsDao(): CreatedPasswordsDao
    abstract fun uploadedPasswordsDao(): UploadedPasswordsDao
}