package com.a65apps.multiplatform.interaction

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe

class IosContainer<S: State, A: Action>(
    private val schedulers: Schedulers,
    private val closure: (S) -> Unit
) {

    private var disposable: Disposable? = null
    private var store: Store<S, A>? = null

    fun acceptAction(action: A) {
        store?.acceptAction(action)
    }

    fun onAppear(store: Store<S, A>) {
        dispose()
        this.store = store
        disposable = store.states()
            .observeOn(schedulers.main)
            .subscribe(isThreadLocal = true) {
                closure(it)
            }
    }

    fun onDisappear() {
        dispose()
    }

    private fun dispose() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}
