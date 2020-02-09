package com.a65apps.multiplatform.interaction.data

import com.a65apps.multiplatform.domain.Task
import com.badoo.reaktive.single.Single

interface ExtendedTaskRepository : TaskRepository {

    fun archivedTasks(): Single<List<Task>>
}
