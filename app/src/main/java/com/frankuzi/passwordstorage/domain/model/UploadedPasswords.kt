package com.frankuzi.passwordstorage.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "uploaded_passwords")
data class UploadedPasswords(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "folder_name") val folderName: String,
    @ColumnInfo(name = "passwords") val passwords: List<String>
)