package com.blabs.blabsnetwork.request

import com.blabs.blabsnetwork.enums.ContentType
import com.blabs.blabsnetwork.enums.HttpMethod
import com.blabs.blabsnetwork.utils.combineFilesAndBody
import com.blabs.blabsnetwork.utils.toJsonRequestBody
import com.blabs.blabsnetwork.utils.toMultipartRequestBody
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File

/**
 * RequestBuilder
 * This class is responsible for building network request
 * @param baseUrl: String
 * @param gson: Gson
 * @constructor
 */
class RequestBuilder(private val baseUrl: String, private val gson: Gson) : IRequestBuilder {

    /**
     * Method
     * The method of the network request
     */
    private var method: HttpMethod = HttpMethod.GET

    /**
     * Url
     * The url of the network request
     */
    private lateinit var url: HttpUrl

    /**
     * Request body
     * The request body of the network request
     */
    private var requestBody: RequestBody? = null

    /**
     * Headers map
     * The headers map of the network request
     */
    private val headersMap: MutableMap<String, String> = mutableMapOf()

    /**
     * Url
     * This function is responsible for constructing the url
     * @param endPoint: String
     * @param queryParams: MutableMap<String, String>.() -> Unit
     * @return IRequestBuilder
     */
    override fun url(
        endPoint: String, queryParams: MutableMap<String, String>.() -> Unit
    ): IRequestBuilder {
        val queryParamsMap = mutableMapOf<String, String>().apply(queryParams)
        this.url = baseUrl.toHttpUrlOrNull()?.newBuilder()?.apply {
            addPathSegments(endPoint.trimStart('/'))
            queryParamsMap.forEach { (key, value) -> addQueryParameter(key, value) }
        }?.build() ?: throw IllegalArgumentException("Invalid URL")
        return this
    }

    /**
     * Method
     * This function is responsible for providing the request method
     * @param method: HttpMethod
     * @return IRequestBuilder
     */
    override fun method(method: HttpMethod): IRequestBuilder {
        this.method = method
        return this
    }

    /**
     * Headers
     * This function is responsible for providing the request headers
     * @param headers: MutableMap<String, String>.() -> Unit
     * @return IRequestBuilder
     */
    override fun headers(headers: MutableMap<String, String>.() -> Unit): IRequestBuilder {
        headersMap.putAll(mutableMapOf<String, String>().apply(headers))
        return this
    }

    /**
     * Body
     * This function is responsible for providing the request body
     * @param contentType: ContentType
     * @param body: Map<String, Any>
     * @param files: Map<String, File>
     * @return IRequestBuilder
     */
    override fun body(
        contentType: ContentType, body: Map<String, Any>, files: Map<String, File>
    ): IRequestBuilder {
        requestBody = when {
            body.isNotEmpty() && files.isNotEmpty() -> combineFilesAndBody(files, body)
            files.isNotEmpty() -> files.toMultipartRequestBody()
            body.isNotEmpty() -> body.toJsonRequestBody(contentType, gson)
            else -> null
        }
        return this
    }

    /**
     * Build
     * This function is responsible for building network request
     * @return Request
     */
    override fun build(): Request {
        return Request.Builder().url(url).method(this.method.name, requestBody).apply {
            headersMap.forEach { (key, value) -> addHeader(key, value) }
        }.build()
    }

}


