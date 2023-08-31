package com.frankuzi.passwordstorage.data.repository

import com.frankuzi.passwordstorage.data.local.PasswordsDatabase
import com.frankuzi.passwordstorage.domain.model.Created
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.PasswordsFolder
import com.frankuzi.passwordstorage.domain.model.Uploaded
import com.frankuzi.passwordstorage.domain.model.UploadedPassword
import com.frankuzi.passwordstorage.domain.repository.PasswordsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    override suspend fun uploadPasswords(passwords: UploadedPassword) {
        val uploadedPasswordsDao = database.uploadedPasswordsDao()
        uploadedPasswordsDao.insertUploadedPassword(passwords)
    }

    override suspend fun getUploadedPasswords(): Flow<List<UploadedPassword>?> {
        return database.uploadedPasswordsDao().getAllPasswords().map { folderListMap ->
            folderListMap.map {
                UploadedPassword(
                    folderId = it.key.id,
                    folderName = it.key.folderName,
                    passwordsValue = it.value.map { password -> password.passwordValue }
                )
            }
        }
    }

    override suspend fun deleteUploadedPassword(uploadedPassword: UploadedPassword) {
        database.uploadedPasswordsDao().deleteUploadedPassword(uploadedPassword)
    }
}