package com.frankuzi.passwordstorage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frankuzi.passwordstorage.data.repository.PasswordsRepositoryImpl
import com.frankuzi.passwordstorage.domain.model.Created
import com.frankuzi.passwordstorage.domain.model.Password
import com.frankuzi.passwordstorage.presentation.Screen
import com.frankuzi.passwordstorage.presentation.create_password.components.AddPasswordContent
import com.frankuzi.passwordstorage.presentation.passwords_list.PasswordsViewModel
import com.frankuzi.passwordstorage.presentation.passwords_list.components.PasswordsListContent
import com.frankuzi.passwordstorage.ui.theme.PasswordStorageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val passwordsViewModel by viewModels<PasswordsViewModel> {
                PasswordsViewModel.provideFactory(
                    passwordsRepository = PasswordsRepositoryImpl((application as App).database),
                    owner = this
                )
            }

            passwordsViewModel.loadPasswords()

            PasswordStorageTheme(
                dynamicColor = false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(
                        passwordsViewModel = passwordsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Content(
    passwordsViewModel: PasswordsViewModel
) {
    val context = LocalContext.current
    val clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val passwordScreenState = passwordsViewModel.passwordsState.collectAsState()
    val navController = rememberNavController()

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
                    val clip = ClipData.newPlainText("password", password.passwordValue)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Password was copied", Toast.LENGTH_SHORT).show()
                },
                onPasswordDeleteClick = { password ->
                    passwordsViewModel.deleteCreatedPassword(password)
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
                    Toast.makeText(context, "Password saved", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onGeneratePasswordSaveClick = { password ->
                    passwordsViewModel.addCreatedPassword(password)
                    Toast.makeText(context, "Password saved", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onUploadPasswordSaveClick = {

                }
            )
        }
    }
}