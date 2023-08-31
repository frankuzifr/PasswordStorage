package com.frankuzi.passwordstorage.presentation.passwords_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frankuzi.passwordstorage.R
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.UploadedPassword
import com.frankuzi.passwordstorage.presentation.passwords_list.PasswordsScreenState

@Composable
fun PasswordsSuccessContent(
    modifier: Modifier = Modifier,
    state: PasswordsScreenState.Success,
    onPasswordCopyClick: (Password) -> Unit,
    onPasswordDeleteClick: (Password) -> Unit,
    onPasswordsUpdated: (List<Password>) -> Unit,
    onFolderDeleteClick: (UploadedPassword) -> Unit,
    onFolderClick: (UploadedPassword) -> Unit
) {
    val createdPasswordState = state.createdPasswords.collectAsState(initial = emptyList())
    val createdPasswords = createdPasswordState.value

    val uploadedPasswordsState = state.uploadedPasswords.collectAsState(initial = emptyList())
    val uploadedPasswords = uploadedPasswordsState.value

    LazyColumn(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp)
    ) {
        if (!createdPasswords.isNullOrEmpty()) {
            onPasswordsUpdated.invoke(createdPasswords)
            item {
                Text(
                    text = stringResource(id = R.string.passwords),
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(createdPasswords) { password ->
                PasswordItem(
                    password = password,
                    onPasswordCopyClick = onPasswordCopyClick,
                    onPasswordDeleteClick = onPasswordDeleteClick
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        if (!uploadedPasswords.isNullOrEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.folders),
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(uploadedPasswords) { password ->
                PasswordFolderItem(
                    password = password,
                    onPasswordDeleteClick = onFolderDeleteClick,
                    onFolderClick = onFolderClick
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
    if (createdPasswords.isNullOrEmpty() && uploadedPasswords.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.storage_is_empty)
            )
        }
    }

}