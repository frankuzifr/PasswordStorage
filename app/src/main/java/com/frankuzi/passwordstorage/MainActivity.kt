package com.frankuzi.passwordstorage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frankuzi.passwordstorage.data.repository.PasswordsRepositoryImpl
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.domain.model.UploadedPassword
import com.frankuzi.passwordstorage.presentation.Screen
import com.frankuzi.passwordstorage.presentation.create_password.components.AddPasswordContent
import com.frankuzi.passwordstorage.presentation.passwords_list.PasswordsViewModel
import com.frankuzi.passwordstorage.presentation.passwords_list.components.FolderContent
import com.frankuzi.passwordstorage.presentation.passwords_list.components.PasswordsListContent
import com.frankuzi.passwordstorage.ui.theme.PasswordStorageTheme
import com.frankuzi.passwordstorage.utils.getFileName

class MainActivity : ComponentActivity() {
    private val _passwordsViewModel by viewModels<PasswordsViewModel> {
        PasswordsViewModel.provideFactory(
            passwordsRepository = PasswordsRepositoryImpl((application as App).database),
            owner = this
        )
    }

    private val _createPasswordFileResult = registerForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { result ->
        result?.let { uri ->
            val openOutputStream = contentResolver.openOutputStream(uri)
            _passwordsViewModel.createdPasswords.forEachIndexed { index, password ->
                openOutputStream?.write("${password.passwordValue}".toByteArray())
                if (index < _passwordsViewModel.createdPasswords.size - 1) {
                    openOutputStream?.write("\n".toByteArray())
                }
            }
            openOutputStream?.close()
        }
    }

    private val _createPasswordsFolderFileResult = registerForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { result ->
        result?.let { uri ->
            val openOutputStream = contentResolver.openOutputStream(uri)
            _passwordsViewModel.folderPasswords?.let { password ->
                password.passwordsValue.forEachIndexed { index, passwordValue->
                    openOutputStream?.write("${passwordValue}\n".toByteArray())
                    if (index < password.passwordsValue.size - 1) {
                        openOutputStream?.write("\n".toByteArray())
                    }
                }
            }
            openOutputStream?.close()
        }
    }

    private val _openPasswordsFileResult = registerForActivityResult(ActivityResultContracts.OpenDocument()) { result ->
        result?.let { uri ->
            val openInputStream = contentResolver.openInputStream(uri)
            val read = openInputStream?.readBytes()
            read?.let {
                val string = String(it)
                val passwords = string.split("\n")
                _passwordsViewModel.uploadedPasswords.value = UploadedPassword(
                    folderName = getFileName(uri) ?: "",
                    passwordsValue = passwords,
                    folderId = -1L
                )
            }
            openInputStream?.close()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            PasswordStorageTheme(
                dynamicColor = false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(
                        passwordsViewModel = _passwordsViewModel,
                        createPasswordsFile = ::createPasswordsFile,
                        createFolderFile = ::createFolderPasswordsFile,
                        openPasswordsFile = ::openPasswordsFile,
                        onPasswordsUpdated = { passwords ->
                            _passwordsViewModel.createdPasswords = passwords
                        },
                        onUploadedPasswordSelected = {
                            _passwordsViewModel.folderPasswords = it
                        }
                    )
                }
            }
        }
    }

    private fun createPasswordsFile(fileName: String) {
        _createPasswordFileResult.launch(fileName)
    }

    private fun createFolderPasswordsFile() {
        _createPasswordsFolderFileResult.launch("${_passwordsViewModel.folderPasswords?.folderName}.txt")
    }

    private fun openPasswordsFile() {
        _openPasswordsFileResult.launch(arrayOf("text/plain"))
    }
}

@Composable
fun Content(
    passwordsViewModel: PasswordsViewModel,
    createPasswordsFile: (String) -> Unit,
    createFolderFile: () -> Unit,
    openPasswordsFile: () -> Unit,
    onPasswordsUpdated: (List<Password>) -> Unit,
    onUploadedPasswordSelected: (UploadedPassword) -> Unit
) {
    val context = LocalContext.current
    val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val passwordScreenState = passwordsViewModel.passwordsState.collectAsState()
    val navController = rememberNavController()
    var selectedFolder by remember {
        mutableStateOf<UploadedPassword?>(null)
    }

    passwordsViewModel.loadPasswords()

    NavHost(navController = navController, startDestination = Screen.PasswordsList.route) {
        composable(
            route = Screen.PasswordsList.route
        ) {
            PasswordsListContent(
                state = passwordScreenState.value,
                floatButtonClick = {
                    navController.navigate(Screen.AddPassword.route)
                },
                onPasswordCopyClick = { password ->
                    val clip = ClipData.newPlainText(context.resources.getString(R.string.password), password.passwordValue)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, context.resources.getString(R.string.password_was_copied), Toast.LENGTH_SHORT).show()
                },
                onPasswordDeleteClick = { password ->
                    passwordsViewModel.deleteCreatedPassword(password)
                },
                onSavePasswordsClick = {
                    createPasswordsFile.invoke("passwords.txt")
                },
                onPasswordsUpdated = onPasswordsUpdated,
                onFolderClick = { password ->
                    selectedFolder = password
                    onUploadedPasswordSelected.invoke(password)
                    navController.navigate(Screen.Folder.route)
                },
                onFolderDeleteClick = {
                    passwordsViewModel.deleteUploadedPassword(it)
                },
                onRetryClick = {
                    passwordsViewModel.loadPasswords()
                }
            )
        }
        composable(
            route = Screen.AddPassword.route,
            enterTransition = {
                scaleIn(
                    animationSpec = tween(400),
                    initialScale = 0f
                ) +
                slideIn(
                    initialOffset = { IntOffset(it.width, it.height)},
                    animationSpec = tween(400)
                ) +
                fadeIn(
                    initialAlpha = 0f,
                    animationSpec = tween(400)
                )
            },
            exitTransition = {
                slideOut(
                    targetOffset = { IntOffset(it.width, it.height)},
                    animationSpec = tween(600)
                ) +
                scaleOut(
                    animationSpec = tween(600),
                    targetScale = 0f
                ) +
                fadeOut(
                    targetAlpha = 0f,
                    animationSpec = tween(400)
                )
            }
        ) {
            AddPasswordContent(
                onBackPressed = {
                    navController.popBackStack()
                },
                onWritePasswordSaveClick = { password ->
                    passwordsViewModel.addCreatedPassword(password)
                    Toast.makeText(context, context.resources.getString(R.string.password_saved), Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onGeneratePasswordSaveClick = { password ->
                    passwordsViewModel.addCreatedPassword(password)
                    Toast.makeText(context, context.resources.getString(R.string.password_saved), Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onUploadPasswordSaveClick = {
                    val uploadedPasswords = passwordsViewModel.uploadedPasswords.value

                    if (uploadedPasswords != null) {
                        passwordsViewModel.addUploadPasswords(uploadedPasswords)
                        Toast.makeText(context, context.resources.getString(R.string.password_saved), Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, context.resources.getString(R.string.password_is_empty), Toast.LENGTH_SHORT).show()
                    }
                },
                onUploadPasswordsFile = {
                    openPasswordsFile.invoke()
                },
                uploadedPasswords = passwordsViewModel.uploadedPasswords.value
            )
        }
        composable(
            route = Screen.Folder.route
        ) {
            FolderContent(
                onBackPressed = {
                    navController.popBackStack()
                },
                onSavePasswordsClick = {
                    createFolderFile.invoke()
                },
                uploadedPassword = selectedFolder
            )
        }
    }
}