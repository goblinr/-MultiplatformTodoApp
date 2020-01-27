package com.a65apps.multiplatform.interaction.archive

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.data.ExtendedTaskRepository
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.completable.andThen
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.flatMapSingle
import com.badoo.reaktive.observable.ofType
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturn
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.subscribeOn

class TaskLoadArchiveMiddleware(
    private val taskRepository: ExtendedTaskRepository,
    private val schedulers: Schedulers
) : Middleware<ArchiveAction, ArchiveState> {

    override fun bind(
        actions: Observable<ArchiveAction>,
        state: Observable<ArchiveState>
    ): Observable<ArchiveAction> = actions.ofType<ArchiveAction.Load>()
        .flatMapSingle {
            taskRepository.archivedTasks()
                .subscribeOn(schedulers.io)
                .map { ArchiveAction.Loaded(it) }
                .onErrorReturn { ArchiveAction.Error(it.message ?: "") }
        }
}

class TaskUnarchiveMiddleware(
    private val taskRepository: TaskRepository,
    private val schedulers: Schedulers
) : Middleware<ArchiveAction, ArchiveState> {

    override fun bind(
        actions: Observable<ArchiveAction>,
        state: Observable<ArchiveState>
    ): Observable<ArchiveAction> =
        actions.ofType<ArchiveAction.UnarchiveTask>()
            .flatMapSingle { action ->
                taskRepository.unarchiveTask(action.id)
                    .subscribeOn(schedulers.io)
                    .andThen(singleOf(ArchiveAction.Load))
                    .onErrorReturn { ArchiveAction.Error(it.message ?: "") }
            }
}
