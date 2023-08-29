package com.frankuzi.passwordstorage.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frankuzi.passwordstorage.domain.model.UploadedPasswords
import kotlinx.coroutines.flow.Flow

@Dao
interface UploadedPasswordsDao {
    @Query("SELECT * FROM uploaded_passwords")
    suspend fun getAllUploadedPasswords(): Flow<List<UploadedPasswords>>

    @Insert
    suspend fun insertUploadedPassword(passwordsFolder: UploadedPasswords)
}