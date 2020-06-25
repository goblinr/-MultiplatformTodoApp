package com.a65apps.multiplatform.sample.di.main

import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.a65apps.multiplatform.sample.presentation.main.MainViewModel
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Provider

@MainScope
@Subcomponent(
    modules = [MainModule::class]
)
interface MainComponent {

    val viewModel: Provider<MainViewModel>

    val todoListFactory: TodoListSubComponent.Factory
    val createFactory: CreateSubComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance state: MainState?,
            @BindsInstance navState: List<Pair<Screen, State>>?
        ): MainComponent
    }
}
