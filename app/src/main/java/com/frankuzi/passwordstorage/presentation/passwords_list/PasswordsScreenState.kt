package com.frankuzi.passwordstorage.presentation.passwords_list

import com.frankuzi.passwordstorage.domain.model.CreatedPassword
import com.frankuzi.passwordstorage.domain.model.UploadedPasswords
import kotlinx.coroutines.flow.Flow

sealed class PasswordsScreenState {
    object Loading : PasswordsScreenState()
    data class Error(val message: String) : PasswordsScreenState()
    data class Success(
        val createdPasswords: Flow<List<CreatedPassword>>,
        val uploadedPasswords: Flow<List<UploadedPasswords>>
    ): PasswordsScreenState()
}
