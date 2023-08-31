package com.frankuzi.passwordstorage.domain.model

data class UploadedPassword(
    val folderId: Long,
    val passwordsValue: List<String>,
    val folderName: String
)