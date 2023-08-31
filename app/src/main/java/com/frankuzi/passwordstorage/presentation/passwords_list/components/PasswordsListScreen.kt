package com.frankuzi.passwordstorage.presentation.passwords_list.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.frankuzi.passwordstorage.R
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.presentation.passwords_list.PasswordsScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordsListContent(
    state: PasswordsScreenState,
    floatButtonClick: () -> Unit,
    onPasswordCopyClick: (Password) -> Unit,
    onPasswordDeleteClick: (Password) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    floatButtonClick.invoke()
                },
                shape = CircleShape
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
                    onPasswordDeleteClick = onPasswordDeleteClick
                )
            }
            is PasswordsScreenState.Error -> {
                PasswordsErrorContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValue.calculateTopPadding())
                )
            }
            is PasswordsScreenState.Loading -> {

            }
        }
    }
}