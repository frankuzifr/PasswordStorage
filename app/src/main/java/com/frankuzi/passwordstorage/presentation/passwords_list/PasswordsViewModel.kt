package com.frankuzi.passwordstorage.presentation.passwords_list

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.frankuzi.passwordstorage.domain.model.Created
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.UploadedPassword
import com.frankuzi.passwordstorage.domain.repository.PasswordsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasswordsViewModel(
    private val passwordsRepository: PasswordsRepository
) : ViewModel() {

    private var _passwordState = MutableStateFlow<PasswordsScreenState>(PasswordsScreenState.Loading)
    val passwordsState = _passwordState.asStateFlow()

    var uploadedPasswords = mutableStateOf<UploadedPassword?>(null)

    var createdPasswords = emptyList<Password>()
    var folderPasswords: UploadedPassword? = null

    fun loadPasswords() {
        _passwordState.update {
            PasswordsScreenState.Loading
        }

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _passwordState.update {
                PasswordsScreenState.Error(exception.message ?: "")
            }
        }

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val createdPasswords = passwordsRepository.getCreatedPasswords()
            val uploadedPasswords = passwordsRepository.getUploadedPasswords()
            _passwordState.update {
                PasswordsScreenState.Success(
                    createdPasswords = createdPasswords,
                    uploadedPasswords = uploadedPasswords
                )
            }
        }
    }

    fun addCreatedPassword(password: Password) {
        viewModelScope.launch {
            passwordsRepository.insertNewPassword(password)
        }
    }

    fun deleteCreatedPassword(password: Password) {
        viewModelScope.launch {
            passwordsRepository.deleteCreatedPassword(password)
        }
    }

    fun addUploadPasswords(uploadedPassword: UploadedPassword) {
        viewModelScope.launch {
            passwordsRepository.uploadPasswords(uploadedPassword)
        }
    }

    fun deleteUploadedPassword(uploadedPassword: UploadedPassword) {
        viewModelScope.launch {
            passwordsRepository.deleteUploadedPassword(uploadedPassword)
        }
    }

    companion object {
        fun provideFactory(
            passwordsRepository: PasswordsRepository,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
        ) : AbstractSavedStateViewModelFactory =
        object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return PasswordsViewModel(passwordsRepository) as T
            }
        }
    }
}