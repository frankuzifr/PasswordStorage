package com.frankuzi.passwordstorage.presentation.create_password.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.frankuzi.passwordstorage.R
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.ui.theme.Gray_50
import kotlin.math.log
import kotlin.math.roundToLong
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordContent(
    onBackPressed: () -> Unit,
    onWritePasswordSaveClick: (Password) -> Unit,
    onGeneratePasswordSaveClick: (Password) -> Unit,
    onUploadPasswordSaveClick: () -> Unit,
) {
    val context = LocalContext.current
    var writePasswordValue by rememberSaveable {
        mutableStateOf("")
    }
    var writePasswordNameValue by rememberSaveable {
        mutableStateOf("")
    }
    var generateSymbolsValue by rememberSaveable {
        mutableStateOf("")
    }
    var generatePasswordNameValue by rememberSaveable {
        mutableStateOf("")
    }
    var generatePasswordValue by rememberSaveable {
        mutableStateOf("")
    }
    var generateEntropyValue by rememberSaveable {
        mutableStateOf("")
    }
    var addPasswordType by rememberSaveable {
        mutableStateOf(AddPasswordType.Write)
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.add_password))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackPressed.invoke()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .width(100.dp)
                    .clip(CircleShape),
                onClick = {
                    when (addPasswordType) {
                        AddPasswordType.Write -> {
                            if (writePasswordValue.isNullOrEmpty()) {
                                Toast.makeText(context, "Password is empty", Toast.LENGTH_SHORT).show()
                                return@FloatingActionButton
                            }

                            val password = Password(
                                passwordName = writePasswordNameValue,
                                passwordValue = writePasswordValue,
                                entropy = null,
                                symbols = null
                            )
                            onWritePasswordSaveClick.invoke(password)
                        }
                        AddPasswordType.Generate -> {
                            if (generatePasswordValue.isNullOrEmpty()) {
                                Toast.makeText(context, "Password is empty", Toast.LENGTH_SHORT).show()
                                return@FloatingActionButton
                            }

                            val password = Password(
                                passwordName = generatePasswordNameValue,
                                passwordValue = generatePasswordValue,
                                entropy = generateEntropyValue,
                                symbols = generateSymbolsValue
                            )
                            onGeneratePasswordSaveClick.invoke(password)
                        }
                        AddPasswordType.Upload -> {

                        }
                    }
                }
            ) {
                Text(text = "Save")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){ paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValue.calculateTopPadding())
        ) {

            WritePassword(
                isSelected = addPasswordType == AddPasswordType.Write,
                onSelect = {
                    addPasswordType = AddPasswordType.Write

                },
                passwordValue = writePasswordValue,
                onPasswordValueChange = {
                    writePasswordValue = it
                },
                passwordNameValue = writePasswordNameValue,
                onPasswordNameValueChange = {
                    writePasswordNameValue = it
                }
            )
            GeneratePassword(
                isSelected = addPasswordType == AddPasswordType.Generate,
                onSelect = {
                    addPasswordType = AddPasswordType.Generate
                },
                symbolsValue = generateSymbolsValue,
                onSymbolsValueChange = {
                    generateSymbolsValue = it
                },
                passwordNameValue = generatePasswordNameValue,
                onPasswordNameValueChange = {
                    generatePasswordNameValue = it
                },
                passwordValue = generatePasswordValue,
                entropy = generateEntropyValue,
                onGenerateClick = {
                    val sb: StringBuilder = StringBuilder(10)
                    val countSymbols = Random.nextInt(6, 20)
                    for (i in 0 until countSymbols) {
                        sb.append(generateSymbolsValue[Random.nextInt(generateSymbolsValue.length)])
                    }
                    val entropy = countSymbols *
                            log(generateSymbolsValue.length.toFloat(), 10f) /
                            log(2f, 20f)
                    generatePasswordValue = sb.toString()
                    generateEntropyValue = "${String.format("%.2f", entropy)} bit"
                }
            )
            UploadPassword(
                isSelected = addPasswordType == AddPasswordType.Upload,
                onSelect = {
                    addPasswordType = AddPasswordType.Upload
                }
            )
        }
    }
}

@Composable
fun WritePassword(
    isSelected: Boolean,
    onSelect: () -> Unit,
    passwordValue: String,
    onPasswordValueChange: (String) -> Unit,
    passwordNameValue: String,
    onPasswordNameValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onSelect.invoke()
            }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onSelect.invoke()
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = "Password name",
                    color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                PasswordTypeTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp)
                        .focusProperties { canFocus = isSelected },
                    textFieldValue = passwordNameValue,
                    onValueChange = onPasswordNameValueChange,
                    tintText = "Enter password name",
                    isSelected = isSelected
                )
            }
            Row {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = "Password",
                    color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                PasswordTypeTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp)
                        .focusProperties { canFocus = isSelected },
                    textFieldValue = passwordValue,
                    onValueChange = onPasswordValueChange,
                    tintText = "Enter password",
                    isSelected = isSelected
                )
            }
        }
    }
}

@Composable
fun GeneratePassword(
    isSelected: Boolean,
    onSelect: () -> Unit,
    passwordNameValue: String,
    onPasswordNameValueChange: (String) -> Unit,
    symbolsValue: String,
    onSymbolsValueChange: (String) -> Unit,
    passwordValue: String,
    entropy: String,
    onGenerateClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onSelect.invoke()
            }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onSelect.invoke()
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = "Password name",
                    color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                PasswordTypeTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp)
                        .focusProperties { canFocus = isSelected },
                    textFieldValue = passwordNameValue,
                    onValueChange = onPasswordNameValueChange,
                    tintText = "Enter password name",
                    isSelected = isSelected
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = "Symbols",
                    color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                PasswordTypeTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp)
                        .focusProperties { canFocus = isSelected },
                    textFieldValue = symbolsValue,
                    onValueChange = onSymbolsValueChange,
                    tintText = "Enter symbols",
                    isSelected = isSelected
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    text = "Entropy",
                    color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = entropy,
                    color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Text(
                    text = "Password",
                    color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = passwordValue,
                    color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                enabled = isSelected,
                onClick = {
                    onGenerateClick.invoke()
                }
            ) {
                Text(
                    text = "Generate"
                )
            }
        }
    }
}

@Composable
fun UploadPassword(
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                onSelect.invoke()
            }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onSelect.invoke()
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            LazyColumn(
                modifier = Modifier
                    .height(100.dp)
                    .padding(top = 12.dp)
            ) {
                items(10) {
                    Text(
                        text = "asdasd",
                        color = if (isSelected) LocalContentColor.current else LocalContentColor.current.copy(0.5f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                enabled = isSelected,
                onClick = {

                }
            ) {
                Text(text = "Select passwords")
            }
        }
    }
}

@Composable
fun PasswordTypeTextField(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    tintText: String,
    isSelected: Boolean
) {
    BasicTextField(
        modifier = modifier,
        value = textFieldValue,
        onValueChange = onValueChange,
        maxLines = 1,
        textStyle = TextStyle.Default.copy(
            color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        ),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(Gray_50)
                    .padding(5.dp)
            ) {
                if (textFieldValue.isEmpty()) {
                    Text(
                        text = tintText,
                        color = if (isSelected) LocalContentColor.current.copy(alpha = 0.7f) else LocalContentColor.current.copy(alpha = 0.4f)
                    )
                }
                innerTextField.invoke()
            }
        }
    )
}

@Preview
@Composable
fun Preview() {
    UploadPassword(
        isSelected = true,
        onSelect = { null },
    )
}