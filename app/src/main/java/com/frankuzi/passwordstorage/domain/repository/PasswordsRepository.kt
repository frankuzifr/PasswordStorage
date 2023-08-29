package com.frankuzi.passwordstorage.domain.repository

import com.frankuzi.passwordstorage.domain.model.CreatedPassword
import com.frankuzi.passwordstorage.domain.model.UploadedPasswords
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {
    suspend fun insertNewPassword(password: CreatedPassword)
    suspend fun getCreatedPasswords(): Flow<List<CreatedPassword>>
    suspend fun uploadPasswords(passwords: UploadedPasswords)
    suspend fun getUploadedPasswords(): Flow<List<UploadedPasswords>>
}