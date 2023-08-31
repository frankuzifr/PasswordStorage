package com.frankuzi.passwordstorage.presentation.passwords_list.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.ui.theme.Gray_50

@Composable
fun PasswordItem(
    password: Password,
    onPasswordCopyClick: (Password) -> Unit,
    onPasswordDeleteClick: (Password) -> Unit,
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 30.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                PasswordText(text = "Name:")
                Spacer(modifier = Modifier.width(5.dp))
                PasswordText(text = password.passwordName ?: "No name")
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                PasswordText(
                    text = "Password:",
                )
                Spacer(modifier = Modifier.width(5.dp))
                PasswordText(
                    modifier = Modifier,
                    text = password.passwordValue
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                PasswordText(
                    text = "Entropy:"
                )
                Spacer(modifier = Modifier.width(5.dp))
                PasswordText(
                    modifier = Modifier,
                    text = if (password.entropy.isNullOrEmpty()) "" else "H = ${password.entropy}"
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                PasswordText(text = "Symbols:")
                Spacer(modifier = Modifier.width(5.dp))
                PasswordText(
                    modifier = Modifier,
                    text = password.symbols ?: ""
                )
            }
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .onGloballyPositioned { layoutCoordinates ->
                    morePressOffset = DpOffset(layoutCoordinates.size.width.dp - itemWidth, layoutCoordinates.size.height.dp - itemHeight * 2)
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
        PopupMenu(
            password = password,
            isExpanded = isContextMenuVisible,
            onDismissRequest = {
                isContextMenuVisible = false
            },
            offset = morePressOffset,
            onDeleteClick = { password ->
                isContextMenuVisible = false
                onPasswordDeleteClick.invoke(password)
            },
            onCopyClick = {
                isContextMenuVisible = false
                onPasswordCopyClick.invoke(password)
            }
        )
    }
}

@Composable
fun PasswordText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        text = text
    )
}

@Composable
fun PopupMenu(
    password: Password,
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    offset: DpOffset,
    onDeleteClick: (Password) -> Unit,
    onCopyClick: (Password) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismissRequest,
        offset = offset
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Copy")
            },
            onClick = {
                onCopyClick.invoke(password)
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = "Delete")
            },
            onClick = {
                onDeleteClick.invoke(password)
            }
        )
    }
}