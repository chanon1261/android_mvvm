package com.panat.mvvm.retrofit.model

data class ResponseUpload(
    val file: List<File>,
    val isThumbnail: Boolean,
    val size: Size,
    val statusCode: Int
)