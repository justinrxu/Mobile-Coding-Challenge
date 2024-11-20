package com.example.mobilecodingchallenge.domain.di

import com.example.mobilecodingchallenge.BuildConfig
import com.example.mobilecodingchallenge.data.api.ImgurService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {
    class AuthInterceptor(private val clientID: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Client-ID $clientID")
                .build()
            return chain.proceed(request)
        }
    }
    @Provides
    fun providesRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.IMGUR_CLIENT_ID))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.imgur.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesFetchItemsService(retrofit: Retrofit): ImgurService {
        return retrofit.create(ImgurService::class.java)
    }
}