package com.frankuzi.passwordstorage.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "uploaded_passwords")
data class Uploaded(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "folder_id") val folderId: Long,
    @ColumnInfo(name = "password_id") val passwordId: Long
)