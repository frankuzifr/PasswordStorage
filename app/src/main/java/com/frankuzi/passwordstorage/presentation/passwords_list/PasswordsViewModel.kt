package com.frankuzi.passwordstorage.presentation.passwords_list

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.frankuzi.passwordstorage.domain.model.CreatedPassword
import com.frankuzi.passwordstorage.domain.repository.PasswordsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PasswordsViewModel(
    val passwordsRepository: PasswordsRepository
) : ViewModel() {

    private var _passwordState = MutableStateFlow<PasswordsScreenState>(PasswordsScreenState.Loading)
    val passwordsState = _passwordState.asStateFlow()

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

    fun addCreatedPassword(password: CreatedPassword) {

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