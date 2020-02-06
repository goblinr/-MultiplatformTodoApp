package com.a65apps.multiplatform.sample.presentation.base

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProviders
import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.sample.di.ComponentFactoryHolder
import com.a65apps.multiplatform.sample.di.ViewModelFactory
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe
import dagger.android.support.DaggerFragment
import javax.inject.Inject

private const val SAVED_STATE = "saved_state"

abstract class BaseFragment<S : State, A : Action, P : Parcelable, VM : BaseViewModel<S, A>> :
    DaggerFragment() {

    @Inject
    lateinit var componentFactoryHolder: ComponentFactoryHolder
    @Inject
    lateinit var schedulers: Schedulers

    protected lateinit var viewModel: VM

    private var savedState: S? = null
    private lateinit var renderingDisposable: Disposable

    protected abstract val viewModelClass: Class<VM>

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedState = savedInstanceState?.let {
            it.getParcelable<P>(SAVED_STATE)?.toDomain()
        }
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory<S, VM>(savedState,
                componentFactoryHolder.factory())
        ).get(viewModelClass)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        renderingDisposable = viewModel.state
            .observeOn(schedulers.main)
            .subscribe(isThreadLocal = true) {
                render(it)
            }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        savedState?.apply {
            outState.putParcelable(SAVED_STATE, toParcelable())
        }
    }

    @CallSuper
    override fun onDestroyView() {
        renderingDisposable.dispose()
        super.onDestroyView()
    }

    protected abstract fun render(state: S)

    protected abstract fun P.toDomain(): S

    protected abstract fun S.toParcelable(): P
}
