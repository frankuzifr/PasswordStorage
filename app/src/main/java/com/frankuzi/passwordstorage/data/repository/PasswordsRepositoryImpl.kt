package com.frankuzi.passwordstorage.data.repository

import com.frankuzi.passwordstorage.data.local.PasswordsDatabase
import com.frankuzi.passwordstorage.domain.model.Created
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.Uploaded
import com.frankuzi.passwordstorage.domain.repository.PasswordsRepository
import kotlinx.coroutines.flow.Flow

class PasswordsRepositoryImpl(
    private val database: PasswordsDatabase
) : PasswordsRepository {

    override suspend fun insertNewPassword(password: Password) {
        val createdPasswordsDao = database.createdPasswordsDao()
        createdPasswordsDao.insertCreatedPassword(password)
    }

    override suspend fun getCreatedPasswords(): Flow<List<Password>?> {
        return database.createdPasswordsDao().getAllPasswords()
    }

    override suspend fun deleteCreatedPassword(password: Password) {
        database.createdPasswordsDao().deleteCreatedPassword(password)
    }

    override suspend fun uploadPasswords(passwords: Uploaded) {
        val uploadedPasswordsDao = database.uploadedPasswordsDao()
        uploadedPasswordsDao.insertUploadedPassword(passwords)
    }

    override suspend fun getUploadedPasswords(): Flow<List<Password>?> {
        return database.uploadedPasswordsDao().getAllUploadedPasswords()
    }
}