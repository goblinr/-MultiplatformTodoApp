package com.a65apps.multiplatform.interaction.create

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.flatMapSingle
import com.badoo.reaktive.observable.ofType
import com.badoo.reaktive.observable.withLatestFrom
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturn
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.subscribeOn

class TaskCreateMiddleware(
    private val taskRepository: TaskRepository,
    private val schedulers: Schedulers
) : Middleware<CreateAction, CreateState> {

    override fun bind(
        actions: Observable<CreateAction>,
        state: Observable<CreateState>
    ): Observable<CreateAction> = actions.ofType<CreateAction.Create>()
        .withLatestFrom(state) { _, lastState ->
            lastState
        }.flatMapSingle { lastState ->
            if (lastState.title.isEmpty()) {
                return@flatMapSingle singleOf(CreateAction.Error("Title should't be empty"))
            }
            if (lastState.description.isEmpty()) {
                return@flatMapSingle singleOf(CreateAction.Error("Description should't be empty"))
            }

            taskRepository.createTask(lastState.title, lastState.description)
                .subscribeOn(schedulers.io)
                .map { CreateAction.CreateResult(it) }
                .onErrorReturn { CreateAction.Error(it.message ?: "") }
        }
}
