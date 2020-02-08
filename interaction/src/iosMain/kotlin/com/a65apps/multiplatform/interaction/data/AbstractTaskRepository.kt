package com.a65apps.multiplatform.interaction.data

import com.a65apps.multiplatform.domain.Task
import com.badoo.reaktive.completable.Completable
import com.badoo.reaktive.completable.completable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.single
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.publish.PublishSubject

abstract class AbstractTaskRepository : TaskRepository {

    private val changed: Subject<Unit> = PublishSubject()

    override fun allTasks(): Single<List<Task>> = single { emitter ->
        blockingTasks({
            emitter.onSuccess(it)
        }) { emitter.onError(it) }
    }

    override fun switchTask(id: String): Completable = completable { emitter ->
        blockingSwitchTask(id, {
            emitter.onComplete()
            changed.onNext(Unit)
        }) { emitter.onError(it) }
    }

    override fun archiveTasks(): Completable = completable { emitter ->
    blockingArchiveTask({
            emitter.onComplete()
            changed.onNext(Unit)
        }) { emitter.onError(it) }
    }

    override fun unarchiveTask(id: String): Completable = completable { emitter ->
        blockingUnarchiveTask({
            emitter.onComplete()
            changed.onNext(Unit)
        }) { emitter.onError(it) }
    }

    override fun createTask(title: String, description: String): Single<Task> = single { emitter ->
        blockingCreateTask(title, description, {
            emitter.onSuccess(it)
            changed.onNext(Unit)
        }) { emitter.onError(it) }
    }

    override fun changed(): Observable<Unit> = changed

    abstract fun blockingTasks(success: (List<Task>) -> Unit, error: (Throwable) -> Unit)

    abstract fun blockingSwitchTask(id: String, success: () -> Unit, error: (Throwable) -> Unit)

    abstract fun blockingArchiveTask(success: () -> Unit, error: (Throwable) -> Unit)

    abstract fun blockingUnarchiveTask(success: () -> Unit, error: (Throwable) -> Unit)

    abstract fun blockingCreateTask(
        title: String,
        description: String,
        success: (Task) -> Unit,
        error: (Throwable) -> Unit
    )
}
