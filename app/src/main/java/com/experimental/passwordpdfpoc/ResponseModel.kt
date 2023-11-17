package com.experimental.passwordpdfpoc

data class ResponseModel constructor(
    val responseData: Data
)

data class Data constructor(
    val pdf: String,
    val pdfkey: String
)