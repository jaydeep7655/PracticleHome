package com.seekmi.logistics.network

import android.annotation.SuppressLint
import android.content.Context
import com.example.practiclehome.BuildConfig
import com.example.practiclehome.network.ApiHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Jaydip.
 */
@SuppressLint("StaticFieldLeak")
object ApiCallFactory {
    private val NETWORK_CALL_TIMEOUT = 30
    private lateinit var mContext: Context

    fun setContext(context: Context) {
        mContext = context
    }

    /**
     * Api helper create retrofit Object
     */
    fun create(): ApiHelper {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(NETWORK_CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        builder.followRedirects(false)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(builder.build())
            .build()

        return retrofit.create(ApiHelper::class.java)
    }
}
