package com.a65apps.multiplatform.interaction.create

import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe

class CreateTaskContainer(
    taskRepository: TaskRepository,
    schedulers: Schedulers,
    closure: (CreateState) -> Unit
) {

    public val store = CreateStore(
        reducer = CreateReducer(),
        middleware = setOf(
            TaskCreateMiddleware(taskRepository, schedulers)
        ),
        initialState = CreateStateProvider(null)
    )

    private val disposable = store.states()
        .observeOn(schedulers.main)
        .subscribe(isThreadLocal = true) {
            closure(it)
        }

    fun acceptAction(action: CreateAction) {
        store.acceptAction(action)
    }

    fun dispose() {
        disposable.dispose()
    }
}
