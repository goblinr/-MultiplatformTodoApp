package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.interaction.main.MainAction
import com.a65apps.multiplatform.interaction.main.MainState
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe

class TodoListContainer(
    mainStore: Store<MainState, MainAction>,
    taskRepository: TaskRepository,
    schedulers: Schedulers,
    closure: (TodoState) -> Unit
) {

    public val store = TodoStore(
        reducer = TodoReducer(),
        middleware = setOf(
            TaskLoadMiddleware(taskRepository, schedulers),
            TaskSwitchMiddleware(taskRepository, schedulers),
            TaskArchiveMiddleware(taskRepository, schedulers),
            TaskUnarchiveMiddleware(taskRepository, schedulers),
            CreateTaskMiddleware(mainStore),
            ChangedTaskMiddleware(taskRepository)
        ),
        initialState = TodoStateProvider(null)
    )

    private val disposable = store.states()
        .observeOn(schedulers.main)
        .subscribe(isThreadLocal = true) {
            closure(it)
        }

    fun acceptAction(action: TodoAction) {
        store.acceptAction(action)
    }

    fun dispose() {
        disposable.dispose()
    }
}
