package com.a65apps.multiplatform.sample.di.archive

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.archive.ArchiveAction
import com.a65apps.multiplatform.interaction.archive.ArchiveReducer
import com.a65apps.multiplatform.interaction.archive.ArchiveState
import com.a65apps.multiplatform.interaction.archive.ArchiveStateProvider
import com.a65apps.multiplatform.interaction.archive.ArchiveStore
import com.a65apps.multiplatform.interaction.archive.TaskLoadArchiveMiddleware
import com.a65apps.multiplatform.interaction.archive.TaskUnarchiveMiddleware
import com.a65apps.multiplatform.interaction.data.ExtendedTaskRepository
import com.a65apps.multiplatform.interaction.data.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class ArchiveListModule {

    @ArchiveListScope
    @Provides
    fun providesReducer(): Reducer<ArchiveState, ArchiveAction> = ArchiveReducer()

    @ArchiveListScope
    @Provides
    fun providesDefaultState(restoredState: ArchiveState?): StateProvider<ArchiveState> =
        ArchiveStateProvider(restoredState)

    @ArchiveListScope
    @Provides
    fun providesStore(
        reducer: Reducer<ArchiveState, ArchiveAction>,
        middleware: Set<@JvmSuppressWildcards Middleware<ArchiveAction, ArchiveState>>,
        initialState: StateProvider<ArchiveState>
    ): Store<ArchiveState, ArchiveAction> =
        ArchiveStore(
            reducer = reducer,
            middleware = middleware,
            initialState = initialState
        )

    @ArchiveListScope
    @IntoSet
    @Provides
    fun providesTaskLoadArchiveMiddleware(
        taskRepository: ExtendedTaskRepository,
        schedulers: Schedulers
    ): Middleware<ArchiveAction, ArchiveState> =
        TaskLoadArchiveMiddleware(taskRepository, schedulers)

    @ArchiveListScope
    @IntoSet
    @Provides
    fun providesTaskUnarchiveMiddleware(
        taskRepository: TaskRepository,
        schedulers: Schedulers
    ): Middleware<ArchiveAction, ArchiveState> =
        TaskUnarchiveMiddleware(taskRepository, schedulers)
}
