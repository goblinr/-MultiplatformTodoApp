package com.a65apps.multiplatform.sample.data.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.data.ExtendedTaskRepository
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.rxjavainterop.asReaktiveSingle
import com.badoo.reaktive.single.Single
import javax.inject.Inject

class ExtendedNetworkTaskRepository @Inject constructor(
    private val api: TaskApi,
    private val networkTaskRepository: TaskRepository
) : ExtendedTaskRepository, TaskRepository by networkTaskRepository {

    override fun archivedTasks(): Single<List<Task>> =
        api.archivedTasks()
            .map { it.map { list -> list.toDomain() } }
            .asReaktiveSingle()
}
