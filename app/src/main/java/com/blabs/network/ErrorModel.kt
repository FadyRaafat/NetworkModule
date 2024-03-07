package com.blabs.network


data class ErrorModel(
    val errors: List<Error?>?, val success: Boolean?
) {
    data class Error(
        val msg: String?, val type: String?
    )
}