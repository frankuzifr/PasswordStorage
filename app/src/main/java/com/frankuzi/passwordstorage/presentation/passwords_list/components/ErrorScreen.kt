package com.frankuzi.passwordstorage.presentation.passwords_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.frankuzi.passwordstorage.R
import com.frankuzi.passwordstorage.presentation.passwords_list.PasswordsScreenState

@Composable
fun PasswordsErrorContent(
    modifier: Modifier = Modifier,
    errorState: PasswordsScreenState.Error,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (errorState.message.isNullOrEmpty()) stringResource(id = R.string.error) else errorState.message
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                onRetryClick.invoke()
            }
        ) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}