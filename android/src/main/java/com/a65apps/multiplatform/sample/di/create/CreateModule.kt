package com.a65apps.multiplatform.sample.di.create

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.create.CreateAction
import com.a65apps.multiplatform.interaction.create.CreateReducer
import com.a65apps.multiplatform.interaction.create.CreateState
import com.a65apps.multiplatform.interaction.create.CreateStateProvider
import com.a65apps.multiplatform.interaction.create.CreateStore
import com.a65apps.multiplatform.interaction.create.TaskCreateMiddleware
import com.a65apps.multiplatform.interaction.data.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class CreateModule {

    @CreateScope
    @Provides
    fun providesReducer(): Reducer<CreateState, CreateAction> = CreateReducer()

    @CreateScope
    @Provides
    fun providesDefaultState(restoredState: CreateState?): StateProvider<CreateState> =
        CreateStateProvider(restoredState)

    @CreateScope
    @Provides
    fun providesStore(
        reducer: Reducer<CreateState, CreateAction>,
        middleware: Set<@JvmSuppressWildcards Middleware<CreateAction, CreateState>>,
        initialState: StateProvider<CreateState>
    ): Store<CreateState, CreateAction> =
        CreateStore(
            reducer = reducer,
            middleware = middleware,
            initialState = initialState
        )

    @CreateScope
    @IntoSet
    @Provides
    fun providesTaskCreateMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<CreateAction, CreateState> =
        TaskCreateMiddleware(taskRepository, schedulers)
}
