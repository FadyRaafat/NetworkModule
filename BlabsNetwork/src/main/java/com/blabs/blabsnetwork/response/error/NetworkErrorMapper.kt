package com.blabs.blabsnetwork.response.error

import com.blabs.blabsnetwork.response.NetworkError

/**
 * NetworkErrorMapper
 * This class is responsible for mapping network error
 * @constructor Create empty Network error mapper
 */
object NetworkErrorMapper {
    private val errorMap = mapOf(
        400 to NetworkError.BadRequest,
        401 to NetworkError.Unauthorized,
        404 to NetworkError.NotFound,
        409 to NetworkError.Conflict
    ).withDefault { code ->
        when (code) {
            in 500..599 -> NetworkError.ServerError
            else -> NetworkError.Unknown(code)
        }
    }

    fun fromStatusCode(code: Int): NetworkError = errorMap.getValue(code)
}
