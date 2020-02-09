package com.a65apps.multiplatform.sample.data.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.data.ExtendedTaskRepository
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.rxjavainterop.asReaktive
import com.badoo.reaktive.single.Single
import javax.inject.Inject

class ExtendedNetworkTaskRepository @Inject constructor(
    private val api: TaskApi,
    private val networkTaskRepository: TaskRepository
) : ExtendedTaskRepository {

    override fun allTasks(): Single<List<Task>> = networkTaskRepository.allTasks()

    override fun switchTask(id: String): Completable = networkTaskRepository.switchTask(id)

    override fun archiveTasks(): Completable = networkTaskRepository.archiveTasks()

    override fun unarchiveTask(id: String): Completable = networkTaskRepository.unarchiveTask(id)

    override fun createTask(title: String, description: String): Single<Task> =
        networkTaskRepository.createTask(title, description)

    override fun changed(): Observable<Unit> = networkTaskRepository.changed()

    override fun archivedTasks(): Single<List<Task>> =
        api.archivedTasks()
            .map { it.map { list -> list.toDomain() } }
            .asReaktive()
}
