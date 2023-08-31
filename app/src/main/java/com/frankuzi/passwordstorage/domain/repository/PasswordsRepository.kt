package com.frankuzi.passwordstorage.domain.repository

import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.PasswordsFolder
import com.frankuzi.passwordstorage.domain.model.UploadedPassword
import kotlinx.coroutines.flow.Flow

interface PasswordsRepository {
    suspend fun insertNewPassword(password: Password)
    suspend fun getCreatedPasswords(): Flow<List<Password>?>
    suspend fun deleteCreatedPassword(password: Password)
    suspend fun uploadPasswords(passwords: UploadedPassword)
    suspend fun getUploadedPasswords(): Flow<List<UploadedPassword>?>
    suspend fun deleteUploadedPassword(uploadedPassword: UploadedPassword)
}
