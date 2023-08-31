package com.frankuzi.passwordstorage.presentation.passwords_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.frankuzi.passwordstorage.R
import com.frankuzi.passwordstorage.domain.model.UploadedPassword
import com.frankuzi.passwordstorage.ui.theme.Gray_50

@Composable
fun PasswordFolderItem(
    password: UploadedPassword,
    onPasswordDeleteClick: (UploadedPassword) -> Unit,
    onFolderClick: (UploadedPassword) -> Unit
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var morePressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    var itemWidth by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Gray_50)
            .onSizeChanged {
                itemHeight = with(density) {
                    it.height.toDp()
                }
                itemWidth = with(density) {
                    it.width.toDp()
                }
            }
            .clickable {
                onFolderClick.invoke(password)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 30.dp)
        ) {
            PasswordText(text = stringResource(id = R.string.name))
            Spacer(modifier = Modifier.width(5.dp))
            PasswordText(text = password.folderName)
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .onGloballyPositioned { layoutCoordinates ->
                    morePressOffset = DpOffset(
                        layoutCoordinates.size.width.dp - itemWidth,
                        layoutCoordinates.size.height.dp - itemHeight * 3
                    )
                },
            onClick = {
                isContextMenuVisible = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
        UploadedPasswordPopupMenu(
            password = password,
            isExpanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            offset = morePressOffset,
            onDeleteClick = { password ->
                isContextMenuVisible = false
                onPasswordDeleteClick.invoke(password)
            }
        )
    }
}

@Composable
fun UploadedPasswordPopupMenu(
    password: UploadedPassword,
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    offset: DpOffset,
    onDeleteClick: (UploadedPassword) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismissRequest,
        offset = offset
    ) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(id = R.string.delete))
            },
            onClick = {
                onDeleteClick.invoke(password)
            }
        )
    }
}