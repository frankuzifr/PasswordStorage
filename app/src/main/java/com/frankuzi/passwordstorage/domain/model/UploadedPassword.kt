package com.frankuzi.passwordstorage.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class UploadedPassword(
    val password: Password,
    val folderName: String
)