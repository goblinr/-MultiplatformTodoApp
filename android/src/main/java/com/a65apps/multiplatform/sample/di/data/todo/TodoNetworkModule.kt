package com.a65apps.multiplatform.sample.di.data.todo

import com.a65apps.multiplatform.sample.data.todo.TaskApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class TodoNetworkModule {

    @Provides
    @Singleton
    fun providesOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        return builder.build()
    }

    @Provides
    @Singleton
    fun providesTaskApi(client: OkHttpClient): TaskApi = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(
            HttpUrl.Builder()
                .host("192.168.1.123")
                .scheme("http")
                .port(8080)
                .build()
        )
        .build()
        .create(TaskApi::class.java)
}
