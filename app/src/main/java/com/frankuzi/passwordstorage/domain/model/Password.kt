package com.frankuzi.passwordstorage.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class Password (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "password_name") val passwordName: String?,
    @ColumnInfo(name = "password_value") val passwordValue: String,
    @ColumnInfo(name = "entropy") val entropy: String?,
    @ColumnInfo(name = "symbols") val symbols: String?
)