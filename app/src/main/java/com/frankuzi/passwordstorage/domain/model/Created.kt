package com.frankuzi.passwordstorage.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "created_passwords")
data class Created(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "password_id") val passwordId: Long
)