package com.frankuzi.passwordstorage.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.Uploaded
import kotlinx.coroutines.flow.Flow

@Dao
interface UploadedPasswordsDao {
    @Query("SELECT * FROM passwords")
    fun getAllUploadedPasswords(): Flow<List<Password>>

    @Insert
    suspend fun insertUploadedPassword(passwordsFolder: Uploaded)
}