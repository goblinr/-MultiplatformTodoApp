package com.a65apps.multiplatform.sample.presentation.base

import androidx.lifecycle.ViewModel
import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.di.ComponentFactoryHolder

open class BaseViewModel<S : State, A : Action>(
    private val store: Store<S, A>,
    private val factory: Map<Class<*>, @JvmSuppressWildcards ComponentBuilder>
) : ViewModel(), ComponentFactoryHolder {

    val state = store.states()

    override fun onCleared() {
        store.dispose()
        super.onCleared()
    }

    fun acceptAction(action: A) {
        store.acceptAction(action)
    }

    override fun factory(): Map<Class<*>, ComponentBuilder> = factory
}
