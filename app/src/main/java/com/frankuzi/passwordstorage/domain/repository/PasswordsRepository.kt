package com.frankuzi.passwordstorage.domain.repository

import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.Uploaded
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {
    suspend fun insertNewPassword(password: Password)
    suspend fun getCreatedPasswords(): Flow<List<Password>?>
    suspend fun deleteCreatedPassword(password: Password)
    suspend fun uploadPasswords(passwords: Uploaded)
    suspend fun getUploadedPasswords(): Flow<List<Password>?>
}