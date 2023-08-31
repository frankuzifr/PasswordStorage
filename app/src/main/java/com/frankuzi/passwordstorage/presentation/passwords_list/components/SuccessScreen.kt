package com.frankuzi.passwordstorage.presentation.passwords_list.components

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.presentation.passwords_list.PasswordsScreenState

@Composable
fun PasswordsSuccessContent(
    modifier: Modifier = Modifier,
    state: PasswordsScreenState.Success,
    onPasswordCopyClick: (Password) -> Unit,
    onPasswordDeleteClick: (Password) -> Unit
) {
    val createdPasswordState = state.createdPasswords.collectAsState(initial = emptyList())
    val createdPasswords = createdPasswordState.value

    val uploadedPasswordsState = state.uploadedPasswords.collectAsState(initial = emptyList())
    val uploadedPasswords = uploadedPasswordsState.value
    Log.i("DB", createdPasswords.toString())

    LazyColumn(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
    ) {
        createdPasswords?.let { passwords ->
            item {
                Text(
                    text = "Passwords:",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(passwords) { password ->
                PasswordItem(
                    password = password,
                    onPasswordCopyClick = onPasswordCopyClick,
                    onPasswordDeleteClick = onPasswordDeleteClick
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        uploadedPasswords?.let {
            item {
                Text(text = "Uploaded")
            }
            items(it) {
                Text(text = it.passwordValue)
            }
        }
    }
}