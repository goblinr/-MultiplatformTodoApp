package com.a65apps.multiplatform.sample.data.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.google.gson.annotations.SerializedName

data class TaskJson(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("status") val status: String
)

fun TaskJson.toDomain(): Task =
    Task(
        id = id,
        title = title,
        description = description,
        status = TaskStatus.valueOf(status)
    )

data class TaskBody(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)
