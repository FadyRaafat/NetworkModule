package com.blabs.blabsnetwork.request

import com.blabs.blabsnetwork.enums.ContentType
import com.blabs.blabsnetwork.enums.HttpMethod
import okhttp3.Request
import java.io.File

/**
 * IRequestBuilder
 * This interface is responsible for building network request
 */
interface IRequestBuilder {
    fun url(endPoint: String, queryParams: MutableMap<String, String>.() -> Unit): IRequestBuilder

    fun method(method: HttpMethod): IRequestBuilder

    fun headers(headers: MutableMap<String, String>.() -> Unit = {}): IRequestBuilder

    fun body(
        contentType: ContentType = ContentType.JSON, body: Map<String, Any> , files: Map<String, File>
    ): IRequestBuilder


    fun build(): Request

}
