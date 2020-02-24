package com.a65apps.multiplatform.sample.data.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.rxjavainterop.asReaktiveCompletable
import com.badoo.reaktive.rxjavainterop.asReaktiveSingle
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.publish.PublishSubject
import javax.inject.Inject

class NetworkTaskRepository @Inject constructor(
    private val api: TaskApi
) : TaskRepository {

    private val changed: Subject<Unit> = PublishSubject()

    override fun allTasks(): Single<List<Task>> =
        api.tasks().map { it.map { list -> list.toDomain() } }
            .asReaktiveSingle()

    override fun switchTask(id: String): Completable = api.switchTask(id)
        .doOnSuccess {
            changed.onNext(Unit)
        }
        .ignoreElement()
        .asReaktiveCompletable()

    override fun archiveTasks(): Completable =
        api.archiveTasks()
            .doOnComplete { changed.onNext(Unit) }
            .asReaktiveCompletable()

    override fun unarchiveTask(id: String): Completable =
        api.unarchiveTask(id)
            .doOnComplete { changed.onNext(Unit) }
            .asReaktiveCompletable()

    override fun createTask(title: String, description: String): Single<Task> =
        api.createTask(TaskBody(title, description)).map { it.toDomain() }
            .doOnSuccess { changed.onNext(Unit) }
            .asReaktiveSingle()

    override fun changed(): Observable<Unit> = changed
}
