package com.frankuzi.passwordstorage.presentation

import androidx.annotation.StringRes
import com.frankuzi.passwordstorage.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object PasswordsList: Screen("passwords_list", R.string.passwords)
    object AddPassword: Screen("add_password", R.string.add_password)
    object Folder: Screen("folder", R.string.folder)
}
