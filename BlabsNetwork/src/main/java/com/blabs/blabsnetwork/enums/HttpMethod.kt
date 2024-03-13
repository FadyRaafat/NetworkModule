package com.blabs.blabsnetwork.enums

/**
 * HttpMethod
 * This enum class is responsible for defining the http methods
 * @property methodName: String
 * @constructor
 */
enum class HttpMethod(private val methodName: String) {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), PATCH("PATCH")
}

/**
 * ContentType
 * This enum class is responsible for defining the content types
 * @property mediaType: String
 * @constructor
 */
enum class ContentType(val mediaType: String) {
    JSON("application/json; charset=utf-8"), FORM("application/x-www-form-urlencoded; charset=utf-8"), MULTIPART(
        "multipart/form-data; charset=utf-8"
    ),
}

/**
 * Headers
 * This enum class is responsible for defining the headers
 * @property value: String
 * @constructor
 */
enum class Headers(val value: String) {
    AUTHORIZATION("Authorization"), CONTENT_TYPE("Content-Type"), ACCEPT("Accept"), APPLICATION_JSON(
        "application/json"
    ),
}

/**
 * BuilderParams
 * This enum class is responsible for defining the builder params
 * @property value: Long
 * @constructor
 */
enum class BuilderParams(val value: Long) {
    REQUEST_TIME_OUT(5)
}