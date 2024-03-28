package com.blabs.blabsnetwork.utils

import com.blabs.blabsnetwork.enums.ContentType
import com.google.gson.Gson
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * toJsonRequestBody
 * This function is responsible for converting map to request body
 * @param contentType: ContentType
 * @param gson: Gson
 * @return RequestBody
 */
fun Map<String, Any>.toJsonRequestBody(contentType: ContentType, gson: Gson): RequestBody =
    gson.toJson(this).toRequestBody(contentType.mediaType.toMediaTypeOrNull())

/**
 * toMultipartRequestBody
 * This function is responsible for converting map to multipart request body
 * @return RequestBody
 */
fun Map<String, File>.toMultipartRequestBody(): RequestBody =
    MultipartBody.Builder().setType(MultipartBody.FORM).apply {
        this@toMultipartRequestBody.forEach { (key, value) ->
            addFormDataPart(key, value.name, value.asRequestBody())
        }
    }.build()

/**
 * combineFilesAndBody
 * This function is responsible for combining files and body
 * @param files: Map<String, File>
 * @param body: Map<String, Any>
 * @return MultipartBody
 */
fun combineFilesAndBody(
    files: Map<String, File>,
    body: Map<String, Any>
): MultipartBody {
    val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
    body.forEach { (key, value) ->
        multipartBodyBuilder.addFormDataPart(key, value.toString())
    }
    files.forEach { (key, value) ->
        multipartBodyBuilder.addFormDataPart(key, value.name, value.asRequestBody())
    }
    return multipartBodyBuilder.build()
}
