package com.a65apps.multiplatform.sample.presentation.todo

import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.navigation.StoreFactory
import com.a65apps.multiplatform.interaction.todo.TodoAction
import com.a65apps.multiplatform.interaction.todo.TodoState
import com.a65apps.multiplatform.sample.di.main.MainComponent
import javax.inject.Inject

class TodoListStoreFactory @Inject constructor(
    private val mainComponent: MainComponent
) : StoreFactory<TodoState, TodoAction> {

    override fun create(data: Any?): Store<TodoState, TodoAction> =
        mainComponent.todoListFactory.create(null).store.get()

    override fun restore(state: State): Store<TodoState, TodoAction> =
        mainComponent.todoListFactory.create(state as TodoState).store.get()
}
