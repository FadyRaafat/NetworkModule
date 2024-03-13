package com.blabs.blabsnetwork.delegate

import com.blabs.blabsnetwork.enums.ContentType
import com.blabs.blabsnetwork.enums.HttpMethod
import com.blabs.blabsnetwork.response.NetworkError
import com.blabs.blabsnetwork.response.error.NetworkErrorMapper
import com.blabs.blabsnetwork.response.error.NetworkResponse
import com.blabs.blabsnetwork.request.RequestBuilder
import com.blabs.blabsnetwork.utils.provideGson
import com.blabs.blabsnetwork.utils.provideOkHttpClient
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * NetworkRequestDelegate
 * This class is responsible for making network requests
 * @param baseUrl: String
 * @property gson: Gson
 * @property okHttpClient: OkHttpClient
 * @property requestBuilder: RequestBuilder
 * @constructor
 */

class NetworkRequestDelegate(
    /**
     * baseUrl: String
     * The base url of the network request
     */
    private val baseUrl: String
) {
    /**
     * Gson instance
     * @return Gson
     */
    val gson by lazy { provideGson() }

    /**
     * OkHttpClient instance
     * @return OkHttpClient
     */
    val okHttpClient by lazy { provideOkHttpClient() }

    /**
     * RequestBuilder instance
     * @return RequestBuilder
     */
    val requestBuilder by lazy { RequestBuilder(baseUrl, gson) }

    /**
     * executeRequest
     * This function is responsible for making network request
     * @param endPoint: String
     * @param method: HttpMethod
     * @param contentType: ContentType
     * @param headers: Map<String, String>
     * @param queryParams: Map<String, String>
     * @param body: Map<String, Any>
     * @param files: Map<String, File>
     * @param T: The model of the response
     * @param E: The model of the error
     * @return NetworkResponse<T, E>
     * @return Unit
     */
    suspend inline fun <reified T, reified E> executeRequest(
        /**
         * endPoint: String
         * The endpoint of the request
         */
        endPoint: String,
        /**
         * method: HttpMethod
         * The method of the request
         */
        method: HttpMethod = HttpMethod.GET,
        /**
         * contentType: ContentType
         * The content type of the request
         */
        contentType: ContentType = ContentType.JSON,
        /**
         * headers: Map<String, String>
         * The headers of the request
         */
        headers: Map<String, String> = emptyMap(),
        /**
         * queryParams: Map<String, String>
         * The query parameters of the request
         */
        queryParams: Map<String, String> = emptyMap(),
        /**
         * body: Map<String, Any>
         * The body of the request
         */
        body: Map<String, Any> = emptyMap(),
        /**
         * files: Map<String, File>
         * The files of the request
         */
        files: Map<String, File> = emptyMap()
    ): NetworkResponse<T, E> {
        return suspendCoroutine { continuation ->
            okHttpClient.newCall(
                requestBuilder.url(endPoint) { putAll(queryParams) }
                    .headers { putAll(headers) }
                    .method(method)
                    .body(contentType, body, files)
                    .build())
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        /**
                         * onFailure
                         * This function is responsible for handling the failure of the request
                         * @param call: Call
                         * @param e: IOException
                         * @return Unit
                         */
                        continuation.resume(NetworkResponse.Error(NetworkError.Exception(e)))
                    }

                    override fun onResponse(call: Call, response: Response) {
                        CoroutineScope(Dispatchers.IO).launch {
                            response.use {
                                val responseBody = it.body?.string()

                                /**
                                 * !response.isSuccessful
                                 * in this case, the response is not successful
                                 * so we need to handle the error
                                 * hence we return a NetworkResponse.Error
                                 * after parsing the error
                                 * to the passed error model *E*
                                 * and passing it to the callback
                                 * if parsing failed
                                 * we return a NetworkResponse.Error
                                 */
                                if (!response.isSuccessful) {
                                    val error = try {
                                        val errorModel: E = gson.fromJson(
                                            responseBody, object : TypeToken<E>() {}.type
                                        )
                                        NetworkResponse.Error(
                                            NetworkError.CustomServerError(response.code),
                                            errorModel
                                        )
                                    } catch (e: JsonSyntaxException) {
                                        NetworkResponse.Error(
                                            NetworkErrorMapper.fromStatusCode(response.code)
                                        )
                                    }
                                    continuation.resume(error)
                                    return@launch
                                }

                                /**
                                 * responseBody == null
                                 * in this case, the response body is null
                                 * so we need to handle the error
                                 * hence we return a NetworkResponse.Error
                                 */
                                if (responseBody == null) {
                                    continuation.resume(
                                        NetworkResponse.Error(
                                            NetworkError.Exception(RuntimeException("Response body is null"))
                                        )
                                    )
                                    return@launch
                                }

                                /**
                                 * response.isSuccessful == true
                                 * in this case, the response is successful
                                 * so we need to parse the response
                                 * to the passed model *T*
                                 * and passing it to the callback
                                 * if parsing failed failed
                                 * we return a NetworkResponse.Error
                                 */
                                try {
                                    val result = gson.fromJson<T>(
                                        responseBody, object : TypeToken<T>() {}.type
                                    )
                                    continuation.resume(NetworkResponse.Success(result))
                                } catch (e: JsonSyntaxException) {
                                    continuation.resume(
                                        NetworkResponse.Error(
                                            NetworkError.Exception(
                                                e
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                })
        }
    }


}