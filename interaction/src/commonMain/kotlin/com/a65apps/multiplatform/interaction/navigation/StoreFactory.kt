package com.a65apps.multiplatform.interaction.navigation

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.Store

interface StoreFactory<S : State, A : Action> {
    fun create(data: Any? = null): Store<S, A>

    fun restore(state: State): Store<S, A>
}
