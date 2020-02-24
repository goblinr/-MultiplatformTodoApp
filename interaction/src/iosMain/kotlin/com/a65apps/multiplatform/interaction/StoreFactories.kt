package com.a65apps.multiplatform.interaction

import com.a65apps.multiplatform.interaction.create.CreateAction
import com.a65apps.multiplatform.interaction.create.CreateState
import com.a65apps.multiplatform.interaction.navigation.StoreFactory
import com.a65apps.multiplatform.interaction.todo.TodoAction
import com.a65apps.multiplatform.interaction.todo.TodoState

class TodoListStoreFactory(
    private val get: () -> Store<TodoState, TodoAction>,
    private val recreate: (State) -> Store<TodoState, TodoAction>
) : StoreFactory<TodoState, TodoAction> {

    override fun create(data: Any?): Store<TodoState, TodoAction> =
        get()

    override fun restore(state: State): Store<TodoState, TodoAction> =
        recreate(state)
}

class CreateTaskStoreFactory(
    private val get: () -> Store<CreateState, CreateAction>,
    private val recreate: (State) -> Store<CreateState, CreateAction>
) : StoreFactory<CreateState, CreateAction> {

    override fun create(data: Any?): Store<CreateState, CreateAction> =
        get()

    override fun restore(state: State): Store<CreateState, CreateAction> =
        recreate(state)
}
