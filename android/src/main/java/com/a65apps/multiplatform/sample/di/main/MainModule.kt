package com.a65apps.multiplatform.sample.di.main

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Reducer
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.State
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
import com.a65apps.multiplatform.interaction.navigation.BasicNavigator
import com.a65apps.multiplatform.interaction.navigation.CommonRouter
import com.a65apps.multiplatform.interaction.navigation.Navigator
import com.a65apps.multiplatform.interaction.navigation.Router
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.a65apps.multiplatform.interaction.navigation.StoreFactory
import com.a65apps.multiplatform.sample.presentation.create.CreateTaskStoreFactory
import com.a65apps.multiplatform.sample.presentation.todo.TodoListStoreFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

@Module(
    subcomponents = [
        TodoListSubComponent::class,
        CreateSubComponent::class
    ],
    includes = [FactoryModule::class]
)
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

    @MainScope
    @Provides
    fun providesNavigator(
        mapFactory: Map<Screen, @JvmSuppressWildcards StoreFactory<out State, out Action>>,
        schedulers: Schedulers
    ): Navigator = BasicNavigator(mapFactory, schedulers)

    @MainScope
    @Provides
    fun providesRouter(navigator: Navigator): Router = CommonRouter(navigator)
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ScreenKey(val value: Screen)

@Module
interface FactoryModule {

    @MainScope
    @Binds
    @IntoMap
    @ScreenKey(Screen.TODO_LIST)
    fun bindsToDoFactory(factory: TodoListStoreFactory): StoreFactory<out State, out Action>

    @MainScope
    @Binds
    @IntoMap
    @ScreenKey(Screen.CREATE_TASK)
    fun bindsCreateFactory(factory: CreateTaskStoreFactory): StoreFactory<out State, out Action>
}
