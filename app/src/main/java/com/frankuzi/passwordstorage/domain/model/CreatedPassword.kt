package com.frankuzi.passwordstorage.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "created_passwords")
data class CreatedPassword(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "password_name") val passwordName: String?,
    @ColumnInfo(name = "password_value") val passwordValue: String,
    @ColumnInfo(name = "entropy") val entropy: Float?,
    @ColumnInfo(name = "symbols") val symbols: String?
)