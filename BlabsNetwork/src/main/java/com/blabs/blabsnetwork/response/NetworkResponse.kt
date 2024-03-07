package com.blabs.blabsnetwork.response


/**
 * NetworkError
 * This class is responsible for handling network error types
 */
sealed class NetworkError {
    data object BadRequest : NetworkError()
    data object Unauthorized : NetworkError()
    data object NotFound : NetworkError()
    data object ServerError : NetworkError()
    data class Unknown(val code: Int) : NetworkError()
    data class Exception(val exception: Throwable) : NetworkError()
    data class CustomServerError(val serverErrorCode: Int) : NetworkError()
}
