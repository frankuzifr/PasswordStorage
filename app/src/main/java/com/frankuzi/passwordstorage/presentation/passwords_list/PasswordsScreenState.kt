package com.frankuzi.passwordstorage.presentation.passwords_list

import com.frankuzi.passwordstorage.domain.model.Created
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.Uploaded
import kotlinx.coroutines.flow.Flow

sealed class PasswordsScreenState {
    object Loading : PasswordsScreenState()
    data class Error(val message: String) : PasswordsScreenState()
    data class Success(
        val createdPasswords: Flow<List<Password>?>,
        val uploadedPasswords: Flow<List<Password>?>
    ): PasswordsScreenState()
}
