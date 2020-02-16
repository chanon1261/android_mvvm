package com.panat.mvvm.retrofit.model.UploadResponse

data class ResponseUpload(
    val file: List<File>,
    val isThumbnail: Boolean,
    val size: Size,
    val statusCode: Int
)