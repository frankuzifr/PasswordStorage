package com.frankuzi.passwordstorage.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class CreatedPassword(
    val password: Password,
    val created: Created
)
