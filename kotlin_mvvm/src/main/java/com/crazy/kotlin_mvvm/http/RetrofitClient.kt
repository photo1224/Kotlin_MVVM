package com.crazy.kotlin_mvvm.http

import com.crazy.kotlin_mvvm.BuildConfig
import com.crazy.kotlin_mvvm.utils.Preference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by wtc on 2019/11/1
 */
object RetrofitClient {

    private val token by Preference(Preference.TOKEN, "")

    lateinit var BASE_URL: String

    private val mRetrofit: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    private val okHttpClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(Interceptor { chain ->
                    try {
                        val builder = chain.request().newBuilder()
                        builder.addHeader("Content-Type", "application/x-www-form-urlencoded")
                        builder.addHeader("Authorization", "Bearer $token")
                        return@Interceptor chain.proceed(builder.build())
                    } catch (e: IOException) {
                        e.printStackTrace()
                        val builder = chain.request().newBuilder()
                        return@Interceptor chain.proceed(builder.build())
                    }
                })
                .addInterceptor(LoggingInterceptor())
//                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        }

    fun <T> getApiService(service: Class<T>): T = mRetrofit.create(service)
}