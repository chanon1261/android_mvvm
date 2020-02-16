package com.panat.mvvm.retrofit.model

data class File(
    val destination: String,
    val encoding: String,
    val fieldname: String,
    val filename: String,
    val mimetype: String,
    val originalname: String,
    val path: String,
    val size: Int
)