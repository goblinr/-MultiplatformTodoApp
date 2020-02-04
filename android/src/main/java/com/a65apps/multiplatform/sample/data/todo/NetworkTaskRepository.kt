package com.a65apps.multiplatform.sample.data.todo

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.rxjavainterop.asReaktive
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
            .asReaktive()

    override fun switchTask(id: String): Completable = completable { emitter ->
        api.switchTask(id).subscribe(
            {
                emitter.onComplete()
                changed.onNext(Unit)
            },
            { emitter.onError(it) }
        )
    }

    override fun archiveTasks(): Completable =
        api.archiveTasks()
            .doOnComplete { changed.onNext(Unit) }
            .asReaktive()

    override fun unarchiveTask(id: String): Completable {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun createTask(title: String, description: String): Single<Task> =
        api.createTask(TaskBody(title, description)).map { it.toDomain() }
            .doOnSuccess { changed.onNext(Unit) }
            .asReaktive()

    override fun changed(): Observable<Unit> = changed
}
