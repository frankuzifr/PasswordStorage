package com.frankuzi.passwordstorage.presentation.passwords_list.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.frankuzi.passwordstorage.R
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.UploadedPassword
import com.frankuzi.passwordstorage.presentation.passwords_list.PasswordsScreenState
import com.frankuzi.passwordstorage.ui.theme.DeepOrange200
import com.frankuzi.passwordstorage.ui.theme.DeepOrangeA700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordsListContent(
    state: PasswordsScreenState,
    floatButtonClick: () -> Unit,
    onPasswordCopyClick: (Password) -> Unit,
    onPasswordDeleteClick: (Password) -> Unit,
    onSavePasswordsClick: () -> Unit,
    onPasswordsUpdated: (List<Password>) -> Unit,
    onFolderDeleteClick: (UploadedPassword) -> Unit,
    onFolderClick: (UploadedPassword) -> Unit,
    onRetryClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            onSavePasswordsClick.invoke()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_save_24),
                            contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    floatButtonClick.invoke()
                },
                shape = CircleShape,
                containerColor = DeepOrange200
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) { paddingValue ->
        when (state) {
            is PasswordsScreenState.Success -> {
                PasswordsSuccessContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValue.calculateTopPadding()),
                    state = state,
                    onPasswordCopyClick = onPasswordCopyClick,
                    onPasswordDeleteClick = onPasswordDeleteClick,
                    onPasswordsUpdated = onPasswordsUpdated,
                    onFolderDeleteClick = onFolderDeleteClick,
                    onFolderClick = onFolderClick
                )
            }
            is PasswordsScreenState.Error -> {
                PasswordsErrorContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValue.calculateTopPadding()),
                    onRetryClick = onRetryClick,
                    errorState = state
                )
            }
            is PasswordsScreenState.Loading -> {
                LoadingContent()
            }
        }
    }
}