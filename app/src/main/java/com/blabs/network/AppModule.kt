package com.blabs.network

import android.content.Context
import com.blabs.blabsnetwork.delegate.NetworkRequestDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkRequestDelegate(
        @ApplicationContext context: Context
    ): NetworkRequestDelegate {
        return NetworkRequestDelegate(
            "https://httpbin.org",
            context = context
        )
    }
}
