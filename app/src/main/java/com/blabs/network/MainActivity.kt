package com.blabs.network

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blabs.blabsnetwork.delegate.NetworkRequestDelegate
import com.blabs.blabsnetwork.enums.ContentType
import com.blabs.blabsnetwork.enums.Headers
import com.blabs.blabsnetwork.enums.HttpMethod
import com.blabs.blabsnetwork.response.NetworkError
import com.blabs.blabsnetwork.response.error.NetworkResponse
import com.blabs.network.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var networkRequestDelegate: NetworkRequestDelegate

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            getRequestButton.setOnClickListener {
                exampleGetRequest()
            }
            postRequestButton.setOnClickListener {
                examplePostRequest()
            }
            putRequestButton.setOnClickListener {
                examplePutRequest()
            }
            deleteRequestButton.setOnClickListener {
                exampleDeleteRequest()
            }
            postWithFilesRequestButton.setOnClickListener {
                exampleMultiPartRequest()
            }
        }
    }

    private fun exampleGetRequest() = lifecycleScope.launch {
        val response = networkRequestDelegate.executeRequest<Item, ErrorModel>(
            endPoint = "/get",
            method = HttpMethod.GET,
            contentType = ContentType.JSON,
            headers = mapOf(Headers.AUTHORIZATION.value to "your token"),
            queryParams = mapOf("size" to "60", "page" to "1")
        )
        when (response) {
            is NetworkResponse.Success -> {}
            is NetworkResponse.Error -> {
                when (response.networkError) {
                    is NetworkError.CustomServerError -> {}
                    else -> {}
                }
            }
        }
    }

    private fun examplePostRequest() = lifecycleScope.launch {
        val response = networkRequestDelegate.executeRequest<Item, ErrorModel>(
            endPoint = "/post",
            method = HttpMethod.POST,
            contentType = ContentType.JSON,
            headers = mapOf(Headers.AUTHORIZATION.value to "your token"),
            body = mapOf("productName" to "12")
        )
        when (response) {
            is NetworkResponse.Success -> {}
            is NetworkResponse.Error -> {
                when (response.networkError) {
                    is NetworkError.CustomServerError -> {}
                    else -> {}
                }
            }
        }
    }

    private fun examplePutRequest() = lifecycleScope.launch {
        val response = networkRequestDelegate.executeRequest<Item, ErrorModel>(
            endPoint = "/put",
            method = HttpMethod.PUT,
            contentType = ContentType.JSON,
            headers = mapOf(Headers.AUTHORIZATION.value to "your token"),
            body = mapOf("id" to 1)
        )
        when (response) {
            is NetworkResponse.Success -> {}
            is NetworkResponse.Error -> {
                when (response.networkError) {
                    is NetworkError.CustomServerError -> {}
                    else -> {}
                }
            }
        }
    }

    private fun exampleDeleteRequest() = lifecycleScope.launch {
        val response = networkRequestDelegate.executeRequest<Item, ErrorModel>(
            endPoint = "/delete",
            method = HttpMethod.DELETE,
            contentType = ContentType.JSON,
            headers = mapOf(Headers.AUTHORIZATION.value to "your token")
        )
        when (response) {
            is NetworkResponse.Success -> {}
            is NetworkResponse.Error -> {
                when (response.networkError) {
                    is NetworkError.CustomServerError -> {}
                    else -> {}
                }
            }
        }
    }

    private fun exampleMultiPartRequest() = lifecycleScope.launch {
        val response = networkRequestDelegate.executeRequest<Item, ErrorModel>(
            endPoint = "/multipartPost",
            method = HttpMethod.POST,
            contentType = ContentType.MULTIPART,
            headers = mapOf(
                Headers.ACCEPT.value to Headers.APPLICATION_JSON.value,
                Headers.CONTENT_TYPE.value to ContentType.MULTIPART.mediaType
            ),
            body = mapOf("name" to "test"),
            files = mapOf(
                "civilian_card" to File("/storage/emulated/0/Pictures/IMG_20240225_150447_1.jpg")
            )
        )

        when (response) {
            is NetworkResponse.Success -> println("Success: ${response.data.name}")
            is NetworkResponse.Error -> {
                when (response.networkError) {
                    is NetworkError.CustomServerError -> {}
                    else -> {}
                }
            }
        }
    }

    private fun examplePatchRequest() = lifecycleScope.launch {
        val response = networkRequestDelegate.executeRequest<Item, ErrorModel>(
            endPoint = "/patch",
            method = HttpMethod.PATCH,
            contentType = ContentType.JSON,
            headers = mapOf(Headers.AUTHORIZATION.value to "your token"),
            body = mapOf("id" to 1)
        )
        when (response) {
            is NetworkResponse.Success -> {}
            is NetworkResponse.Error -> {
                when (response.networkError) {
                    is NetworkError.CustomServerError -> {}
                    else -> {}
                }
            }
        }
    }
}
