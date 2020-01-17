package com.a65apps.multiplatform.sample.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.a65apps.multiplatform.interaction.State
import dagger.BindsInstance
import javax.inject.Inject
import javax.inject.Provider

interface ViewModelComponent<S : State, V : ViewModel> {

    fun inject(viewModelFactory: ViewModelFactory<S, V>)
}

interface ComponentBuilder

interface ViewModelComponentBuilder<S : State, V : ViewModel,
        out C : ViewModelComponent<S, V>> : ComponentBuilder {

    @BindsInstance
    fun savedState(savedState: S?): ViewModelComponentBuilder<S, V, C>

    fun build(): C
}

class ViewModelFactory<S : State, V : ViewModel>(
    private val savedState: S?,
    private val factory: Map<Class<*>, ComponentBuilder>
) : ViewModelProvider.Factory {

    @Inject
    lateinit var provider: Provider<V>

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val result = factory[modelClass]
        if (result != null) {
            result as ViewModelComponentBuilder<S, V, ViewModelComponent<S, V>>

            val component = result
                .savedState(savedState)
                .build()
            component.inject(this)

            return provider.get() as T
        } else {
            throw IllegalArgumentException()
        }
    }
}

interface ComponentFactoryHolder {
    fun factory(): Map<Class<*>, ComponentBuilder>
}
