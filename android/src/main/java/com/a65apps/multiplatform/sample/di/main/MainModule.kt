package com.a65apps.multiplatform.sample.di.main

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.StateProvider
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.main.BackMiddleware
import com.a65apps.multiplatform.interaction.main.ForwardMiddleware
import com.a65apps.multiplatform.interaction.main.MainAction
import com.a65apps.multiplatform.interaction.main.MainReducer
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.interaction.main.MainStateProvider
import com.a65apps.multiplatform.interaction.main.MainStore
import com.a65apps.multiplatform.interaction.main.ReplaceMiddleware
import com.a65apps.multiplatform.interaction.navigation.Router
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module
class MainModule {

    @MainScope
    @Provides
    fun providesReducer(): Reducer<MainState, MainAction> = MainReducer()

    @MainScope
    @Provides
    fun providesDefaultState(restoredState: MainState?): StateProvider<MainState> =
        MainStateProvider(restoredState)

    @MainScope
    @Provides
    fun providesStore(
        reducer: Reducer<MainState, MainAction>,
        middleware: Set<@JvmSuppressWildcards Middleware<MainAction, MainState>>,
        initialState: StateProvider<MainState>,
        restoredState: MainState?
    ): Store<MainState, MainAction> =
        MainStore(
            reducer = reducer,
            middleware = middleware,
            initialState = initialState,
            restoredState = restoredState
        )

    @MainScope
    @IntoSet
    @Provides
    fun providesForwardMiddleware(
        router: Router,
        schedulers: Schedulers
    ): Middleware<MainAction, MainState> =
        ForwardMiddleware(router, schedulers)

    @MainScope
    @IntoSet
    @Provides
    fun providesReplaceMiddleware(
        router: Router,
        schedulers: Schedulers
    ): Middleware<MainAction, MainState> =
        ReplaceMiddleware(router, schedulers)

    @MainScope
    @IntoSet
    @Provides
    fun providesBackMiddleware(
        router: Router,
        schedulers: Schedulers
    ): Middleware<MainAction, MainState> =
        BackMiddleware(router, schedulers)
}
