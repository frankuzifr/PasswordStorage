package com.frankuzi.passwordstorage.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frankuzi.passwordstorage.domain.model.CreatedPassword
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatedPasswordsDao {
    @Query("SELECT * FROM created_passwords")
    suspend fun getAllPasswords(): Flow<List<CreatedPassword>>

    @Insert
    suspend fun insertPassword(password: CreatedPassword)
}