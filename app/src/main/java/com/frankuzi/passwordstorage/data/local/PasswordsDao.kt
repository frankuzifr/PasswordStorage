package com.frankuzi.passwordstorage.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.frankuzi.passwordstorage.domain.model.Password

interface PasswordsDao {
    @Insert
    suspend fun insertPassword(password: Password): Long

    @Delete
    suspend fun deletePassword(password: Password)

    @Query("DELETE FROM passwords WHERE passwords.id == :id")
    suspend fun deletePasswordById(id: Long)
}