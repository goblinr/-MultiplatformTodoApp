package com.a65apps.multiplatform.sample.data.todo

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {

    @GET("tasks")
    fun tasks(): Single<List<TaskJson>>

    @POST("tasks")
    fun createTask(@Body body: TaskBody): Single<TaskJson>

    @PATCH("tasks/{id}")
    fun switchTask(@Path("id") id: String): Single<TaskJson>

    @POST("tasks/archive")
    fun archiveTasks(): Completable

    @PATCH("tasks/archive/{id}")
    fun unarchiveTask(@Path("id") id: String): Completable

    @GET("tasks/archive")
    fun archivedTasks(): Single<List<TaskJson>>
}
