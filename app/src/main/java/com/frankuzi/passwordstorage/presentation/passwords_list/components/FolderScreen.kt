package com.frankuzi.passwordstorage.presentation.passwords_list.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.frankuzi.passwordstorage.R
import com.frankuzi.passwordstorage.domain.model.UploadedPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderContent(
    onBackPressed: () -> Unit,
    onSavePasswordsClick: () -> Unit,
    uploadedPassword: UploadedPassword?
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = uploadedPassword?.folderName ?: "")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressed.invoke()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
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
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValue.calculateTopPadding())
        ) {
            uploadedPassword?.let {password ->
                items(password.passwordsValue) {
                    Text(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp),
                        text = it
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }
    }
}