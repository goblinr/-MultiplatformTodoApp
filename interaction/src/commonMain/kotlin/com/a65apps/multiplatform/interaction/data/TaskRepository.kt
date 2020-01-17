package com.a65apps.multiplatform.interaction.data

import com.a65apps.multiplatform.domain.Task
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.Single

interface TaskRepository {

    fun allTasks(): Single<List<Task>>

    fun switchTask(id: String): Completable

    fun archiveTasks(): Completable

    fun unarchiveTask(id: String): Completable

    fun createTask(title: String, description: String): Single<Task>

    fun changed(): Observable<Unit>
}
