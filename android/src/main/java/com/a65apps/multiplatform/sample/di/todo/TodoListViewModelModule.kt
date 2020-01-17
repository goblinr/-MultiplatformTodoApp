package com.a65apps.multiplatform.sample.di.todo

import com.a65apps.multiplatform.interaction.todo.TodoState
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.di.ViewModelComponent
import com.a65apps.multiplatform.sample.di.ViewModelComponentBuilder
import com.a65apps.multiplatform.sample.di.main.MainScope
import com.a65apps.multiplatform.sample.presentation.todo.TodoListViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class TodoListScope

@Module(subcomponents = [TodoListViewModelModule.TodoListSubcomponent::class])
interface TodoListViewModelModule {

    @MainScope
    @Binds
    @IntoMap
    @ClassKey(TodoListViewModel::class)
    fun bindsBuilder(builder: TodoListSubcomponent.Builder): ComponentBuilder

    @TodoListScope
    @Subcomponent(
        modules = [TodoListModule::class]
    )
    interface TodoListSubcomponent : ViewModelComponent<TodoState, TodoListViewModel> {

        @Subcomponent.Builder
        interface Builder : ViewModelComponentBuilder<TodoState, TodoListViewModel,
                TodoListSubcomponent>
    }
}
