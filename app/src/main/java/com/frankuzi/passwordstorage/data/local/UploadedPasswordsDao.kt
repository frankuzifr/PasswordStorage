package com.frankuzi.passwordstorage.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.PasswordsFolder
import com.frankuzi.passwordstorage.domain.model.Uploaded
import com.frankuzi.passwordstorage.domain.model.UploadedPassword
import kotlinx.coroutines.flow.Flow

@Dao
interface UploadedPasswordsDao: PasswordsDao {
    @Query("SELECT * " +
            "FROM passwords, uploaded_passwords, passwords_folders " +
            "WHERE uploaded_passwords.folder_id == passwords_folders.id AND uploaded_passwords.password_id == passwords.id")
    fun getAllPasswords(): Flow<Map<PasswordsFolder, List<Password>>>

    @Transaction
    suspend fun insertUploadedPassword(uploadedPassword: UploadedPassword) {
        val insertFolder = insertFolder(PasswordsFolder(folderName = uploadedPassword.folderName))
        uploadedPassword.passwordsValue.forEach { password ->
            val insertPassword = insertPassword(password = Password(passwordValue = password))
            insertUploaded(uploaded = Uploaded(folderId = insertFolder, passwordId = insertPassword))
        }
    }

    @Transaction
    suspend fun deleteUploadedPassword(uploadedPassword: UploadedPassword) {
        val folderId = uploadedPassword.folderId
        val allUploaded = getAllUploadedByFolderId(folderId)
        allUploaded.forEach {
            deletePasswordById(it.passwordId)
            deleteUploaded(it)
        }
        deleteFolderById(folderId)
    }

    @Query("SELECT * FROM uploaded_passwords WHERE uploaded_passwords.folder_id == :id ")
    suspend fun getAllUploadedByFolderId(id: Long): List<Uploaded>

    @Insert
    suspend fun insertFolder(folder: PasswordsFolder): Long

    @Insert
    suspend fun insertUploaded(uploaded: Uploaded)

    @Query("DELETE FROM passwords_folders WHERE passwords_folders.id == :id")
    suspend fun deleteFolderById(id: Long)

    @Delete
    suspend fun deleteUploaded(uploaded: Uploaded)
}