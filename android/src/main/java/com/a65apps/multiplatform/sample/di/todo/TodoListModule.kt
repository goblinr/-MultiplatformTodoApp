package com.a65apps.multiplatform.sample.di.todo

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.interaction.main.MainAction
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.interaction.todo.ChangedTaskMiddleware
import com.a65apps.multiplatform.interaction.todo.CreateTaskMiddleware
import com.a65apps.multiplatform.interaction.todo.TaskArchiveMiddleware
import com.a65apps.multiplatform.interaction.todo.TaskLoadMiddleware
import com.a65apps.multiplatform.interaction.todo.TaskSwitchMiddleware
import com.a65apps.multiplatform.interaction.todo.TaskUnarchiveMiddleware
import com.a65apps.multiplatform.interaction.todo.TodoAction
import com.a65apps.multiplatform.interaction.todo.TodoReducer
import com.a65apps.multiplatform.interaction.todo.TodoState
import com.a65apps.multiplatform.interaction.todo.TodoStateProvider
import com.a65apps.multiplatform.interaction.todo.TodoStore
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class TodoListModule {

    @TodoListScope
    @Provides
    fun providesReducer(): Reducer<TodoState, TodoAction> = TodoReducer()

    @TodoListScope
    @Provides
    fun providesDefaultState(restoredState: TodoState?): StateProvider<TodoState> =
        TodoStateProvider(restoredState)

    @TodoListScope
    @Provides
    fun providesStore(
        reducer: Reducer<TodoState, TodoAction>,
        middleware: Set<@JvmSuppressWildcards Middleware<TodoAction, TodoState>>,
        initialState: StateProvider<TodoState>
    ): Store<TodoState, TodoAction> =
        TodoStore(
            reducer = reducer,
            middleware = middleware,
            initialState = initialState
        )

    @TodoListScope
    @IntoSet
    @Provides
    fun providesTaskLoadMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<TodoAction, TodoState> =
        TaskLoadMiddleware(taskRepository, schedulers)

    @TodoListScope
    @IntoSet
    @Provides
    fun providesTaskSwitchMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<TodoAction, TodoState> =
        TaskSwitchMiddleware(taskRepository, schedulers)

    @TodoListScope
    @IntoSet
    @Provides
    fun providesTaskArchiveMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<TodoAction, TodoState> =
        TaskArchiveMiddleware(taskRepository, schedulers)

    @TodoListScope
    @IntoSet
    @Provides
    fun providesTaskUnarchiveMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<TodoAction, TodoState> =
        TaskUnarchiveMiddleware(taskRepository, schedulers)

    @TodoListScope
    @IntoSet
    @Provides
    fun providesCreateTaskMiddleware(
        mainStore: Store<MainState, MainAction>
    ): Middleware<TodoAction, TodoState> =
        CreateTaskMiddleware(mainStore)

    @TodoListScope
    @IntoSet
    @Provides
    fun providesChangedTaskMiddleware(
        taskRepository: TaskRepository
    ): Middleware<TodoAction, TodoState> =
        ChangedTaskMiddleware(taskRepository)
}
