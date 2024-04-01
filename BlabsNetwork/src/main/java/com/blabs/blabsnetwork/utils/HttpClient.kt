package com.blabs.blabsnetwork.utils

import android.content.Context
import com.blabs.blabsnetwork.enums.BuilderParams
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import okhttp3.Authenticator
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * provideOkHttpClient
 * This function is responsible for providing okHttpClient
 * @return OkHttpClient
 */
fun provideOkHttpClient(
    context: Context,
    cookieJar: CookieJar? = null,
    authenticator: Authenticator? = null,
    interceptor: Interceptor? = null
) = OkHttpClient
    .Builder()
    .readTimeout(BuilderParams.REQUEST_TIME_OUT.value, TimeUnit.MINUTES)
    .connectTimeout(BuilderParams.REQUEST_TIME_OUT.value, TimeUnit.MINUTES)
    .addInterceptor(ChuckerInterceptor(context = context))
    .addInterceptor(provideLoggingInterceptor())
    .addNetworkInterceptor(provideHttpLoggingInterceptor())
    .apply {
        cookieJar?.let {
            cookieJar((it))
        }
        interceptor?.let {
            addInterceptor(it)
        }
        authenticator?.let {
            authenticator(it)
        }
    }.build()

/**
 * provideHttpLoggingInterceptor
 * This function is responsible for providing httpLoggingInterceptor
 * @return HttpLoggingInterceptor
 */
fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}

/**
 * provideLoggingInterceptor
 * This function is responsible for providing loggingInterceptor
 * @return Interceptor
 */
fun provideLoggingInterceptor(): Interceptor = Interceptor { chain ->
    val request = chain.request()
    println("Sending request: ${request.url} with headers ${request.headers}")
    val response = chain.proceed(request)
    println("Received response for: ${response.request.url} with status code ${response.code}")
    response
}

/**
 * provideGson
 * This function is responsible for providing gson
 * @return Gson
 */
fun provideGson(): Gson {
    return GsonBuilder().setLenient().serializeNulls().create()
}
