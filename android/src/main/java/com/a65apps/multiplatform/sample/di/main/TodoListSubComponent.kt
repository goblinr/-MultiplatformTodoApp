package com.a65apps.multiplatform.sample.di.main

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
import com.a65apps.multiplatform.interaction.todo.GoToArchiveMiddleware
import com.a65apps.multiplatform.interaction.todo.TaskArchiveMiddleware
import com.a65apps.multiplatform.interaction.todo.TaskLoadMiddleware
import com.a65apps.multiplatform.interaction.todo.TaskSwitchMiddleware
import com.a65apps.multiplatform.interaction.todo.TaskUnarchiveMiddleware
import com.a65apps.multiplatform.interaction.todo.TodoAction
import com.a65apps.multiplatform.interaction.todo.TodoReducer
import com.a65apps.multiplatform.interaction.todo.TodoState
import com.a65apps.multiplatform.interaction.todo.TodoStateProvider
import com.a65apps.multiplatform.interaction.todo.TodoStore
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.IntoSet
import javax.inject.Provider

@Subcomponent(
    modules = [TodoListModule::class]
)
interface TodoListSubComponent {

    val store: Provider<Store<TodoState, TodoAction>>

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance state: TodoState?): TodoListSubComponent
    }
}

@Module
class TodoListModule {

    @Provides
    fun providesReducer(): Reducer<TodoState, TodoAction> = TodoReducer()

    @Provides
    fun providesDefaultState(restoredState: TodoState?): StateProvider<TodoState> =
        TodoStateProvider(restoredState)

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

    @IntoSet
    @Provides
    fun providesTaskLoadMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<TodoAction, TodoState> =
        TaskLoadMiddleware(taskRepository, schedulers)

    @IntoSet
    @Provides
    fun providesTaskSwitchMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<TodoAction, TodoState> =
        TaskSwitchMiddleware(taskRepository, schedulers)

    @IntoSet
    @Provides
    fun providesTaskArchiveMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<TodoAction, TodoState> =
        TaskArchiveMiddleware(taskRepository, schedulers)

    @IntoSet
    @Provides
    fun providesTaskUnarchiveMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<TodoAction, TodoState> =
        TaskUnarchiveMiddleware(taskRepository, schedulers)

    @IntoSet
    @Provides
    fun providesCreateTaskMiddleware(
        mainStore: Store<MainState, MainAction>
    ): Middleware<TodoAction, TodoState> =
        CreateTaskMiddleware(mainStore)

    @IntoSet
    @Provides
    fun providesChangedTaskMiddleware(
        taskRepository: TaskRepository
    ): Middleware<TodoAction, TodoState> =
        ChangedTaskMiddleware(taskRepository)

    @IntoSet
    @Provides
    fun providesGoToArchiveTaskMiddleware(
        mainStore: Store<MainState, MainAction>
    ): Middleware<TodoAction, TodoState> =
        GoToArchiveMiddleware(mainStore)
}
