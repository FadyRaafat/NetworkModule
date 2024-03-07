package com.blabs.blabsnetwork.response.error

import com.blabs.blabsnetwork.response.NetworkError

/**
 * NetworkResponse
 * This class is responsible for handling network response
 * @param T - The type of the response
 * @param E - The type of the error
 * @constructor Create empty Network response
 */
sealed class NetworkResponse<out T, out E> {
    data class Success<out T>(val data: T) : NetworkResponse<T, Nothing>()
    data class Error<out E>(val networkError: NetworkError, val error: E? = null) :
        NetworkResponse<Nothing, E>()
}

