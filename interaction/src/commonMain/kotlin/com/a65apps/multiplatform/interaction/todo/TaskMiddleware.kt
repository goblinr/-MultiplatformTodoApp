package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.interaction.main.MainAction
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.badoo.reaktive.completable.andThen
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.flatMapSingle
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.ofType
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturn
import com.badoo.reaktive.single.singleOf
import com.badoo.reaktive.single.subscribeOn

class TaskLoadMiddleware(
    private val taskRepository: TaskRepository,
    private val schedulers: Schedulers
) : Middleware<TodoAction, TodoState> {

    override fun bind(
        actions: Observable<TodoAction>,
        state: Observable<TodoState>
    ): Observable<TodoAction> = actions.ofType<TodoAction.Load>()
        .flatMapSingle {
            taskRepository.allTasks()
                .subscribeOn(schedulers.io)
                .map { TodoAction.Loaded(it) }
                .onErrorReturn { TodoAction.Error(it.message ?: "") }
        }
}

class TaskSwitchMiddleware(
    private val taskRepository: TaskRepository,
    private val schedulers: Schedulers
) : Middleware<TodoAction, TodoState> {

    override fun bind(
        actions: Observable<TodoAction>,
        state: Observable<TodoState>
    ): Observable<TodoAction> = actions.ofType<TodoAction.Switch>()
        .flatMapSingle { action ->
            taskRepository.switchTask(action.id)
                .subscribeOn(schedulers.io)
                .andThen(singleOf(TodoAction.Idle))
                .onErrorReturn { TodoAction.Error(it.message ?: "") }
        }
}

class TaskArchiveMiddleware(
    private val taskRepository: TaskRepository,
    private val schedulers: Schedulers
) : Middleware<TodoAction, TodoState> {

    override fun bind(
        actions: Observable<TodoAction>,
        state: Observable<TodoState>
    ): Observable<TodoAction> = actions.ofType<TodoAction.Archive>()
        .flatMapSingle {
            taskRepository.archiveTasks()
                .subscribeOn(schedulers.io)
                .andThen(singleOf(TodoAction.Idle))
                .onErrorReturn { TodoAction.Error(it.message ?: "") }
        }
}

class TaskUnarchiveMiddleware(
    private val taskRepository: TaskRepository,
    private val schedulers: Schedulers
) : Middleware<TodoAction, TodoState> {

    override fun bind(
        actions: Observable<TodoAction>,
        state: Observable<TodoState>
    ): Observable<TodoAction> = actions.ofType<TodoAction.Unarchive>()
        .flatMapSingle { action ->
            taskRepository.unarchiveTask(action.id)
                .subscribeOn(schedulers.io)
                .andThen(singleOf(TodoAction.Idle))
                .onErrorReturn { TodoAction.Error(it.message ?: "") }
        }
}

class CreateTaskMiddleware(
    private val mainStore: Store<MainState, MainAction>
) : Middleware<TodoAction, TodoState> {

    override fun bind(
        actions: Observable<TodoAction>,
        state: Observable<TodoState>
    ): Observable<TodoAction> = actions.ofType<TodoAction.CreateTask>()
        .map {
            mainStore.acceptAction(MainAction.NavigateForward(Screen.CREATE_TASK))
            TodoAction.Idle
        }
}

class ChangedTaskMiddleware(
    private val taskRepository: TaskRepository
) : Middleware<TodoAction, TodoState> {

    override fun bind(
        actions: Observable<TodoAction>,
        state: Observable<TodoState>
    ): Observable<TodoAction> = taskRepository.changed()
        .map { TodoAction.Load }
}
