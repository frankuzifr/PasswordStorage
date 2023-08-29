package com.frankuzi.passwordstorage.data.repository

import com.frankuzi.passwordstorage.data.local.PasswordsDatabase
import com.frankuzi.passwordstorage.domain.model.CreatedPassword
import com.frankuzi.passwordstorage.domain.model.UploadedPasswords
import com.frankuzi.passwordstorage.domain.repository.PasswordsRepository
import kotlinx.coroutines.flow.Flow

class PasswordsRepositoryImpl(
    private val database: PasswordsDatabase
) : PasswordsRepository {

    override suspend fun insertNewPassword(password: CreatedPassword) {
        val createdPasswordsDao = database.createdPasswordsDao()
        createdPasswordsDao.insertPassword(password)
    }

    override suspend fun getCreatedPasswords(): Flow<List<CreatedPassword>> {
        return database.createdPasswordsDao().getAllPasswords()
    }

    override suspend fun uploadPasswords(passwords: UploadedPasswords) {
        val uploadedPasswordsDao = database.uploadedPasswordsDao()
        uploadedPasswordsDao.insertUploadedPassword(passwords)
    }

    override suspend fun getUploadedPasswords(): Flow<List<UploadedPasswords>> {
        return database.uploadedPasswordsDao().getAllUploadedPasswords()
    }
}