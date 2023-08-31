package com.frankuzi.passwordstorage.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.frankuzi.passwordstorage.domain.model.Created
import com.frankuzi.passwordstorage.domain.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatedPasswordsDao {
    @Query(
        "SELECT *" +
                "FROM created_passwords, passwords " +
                "WHERE created_passwords.password_id == passwords.id"
    )
    fun getAllPasswords(): Flow<List<Password>>

    @Transaction
    suspend fun insertCreatedPassword(password: Password) {
        val id = insertPassword(password)
        insertCreated(Created(passwordId = id))
    }

    @Transaction
    suspend fun deleteCreatedPassword(password: Password) {
        deleteCreated(password.id)
        deletePassword(password)
    }

    @Insert
    suspend fun insertPassword(password: Password): Long

    @Insert
    suspend fun insertCreated(created: Created)

    @Delete
    suspend fun deletePassword(password: Password)

    @Query("DELETE FROM created_passwords WHERE password_id == :id")
    suspend fun deleteCreated(id: Long)
}