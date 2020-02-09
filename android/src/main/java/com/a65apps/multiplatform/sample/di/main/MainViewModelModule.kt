package com.a65apps.multiplatform.sample.di.main

import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.di.ViewModelComponent
import com.a65apps.multiplatform.sample.di.ViewModelComponentBuilder
import com.a65apps.multiplatform.sample.di.archive.ArchiveListViewModelModule
import com.a65apps.multiplatform.sample.di.create.CreateViewModelModule
import com.a65apps.multiplatform.sample.di.todo.TodoListViewModelModule
import com.a65apps.multiplatform.sample.presentation.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Scope
import javax.inject.Singleton

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

@Module(subcomponents = [MainViewModelModule.MainSubcomponent::class])
interface MainViewModelModule {

    @Singleton
    @Binds
    @IntoMap
    @ClassKey(MainViewModel::class)
    fun bindsBuilder(builder: MainSubcomponent.Builder): ComponentBuilder

    @MainScope
    @Subcomponent(
        modules = [
            MainModule::class,
            TodoListViewModelModule::class,
            CreateViewModelModule::class,
            ArchiveListViewModelModule::class
        ]
    )
    interface MainSubcomponent : ViewModelComponent<MainState, MainViewModel> {

        @Subcomponent.Builder
        interface Builder : ViewModelComponentBuilder<MainState, MainViewModel, MainSubcomponent>
    }
}
