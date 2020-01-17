package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.navigation.Router
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe

class MainContainer(
    router: Router,
    schedulers: Schedulers,
    closure: (MainState) -> Unit
) {

    public val store: MainStore = MainStore(
        reducer = MainReducer(),
        middleware = setOf(
            ForwardMiddleware(router, schedulers),
            ReplaceMiddleware(router, schedulers),
            BackMiddleware(router, schedulers)
        ),
        initialState = MainStateProvider(null),
        restoredState = null
    )

    private val disposable = store.states()
        .observeOn(schedulers.main)
        .subscribe(isThreadLocal = true) {
            closure(it)
        }

    fun acceptAction(action: MainAction) {
        store.acceptAction(action)
    }

    fun dispose() {
        disposable.dispose()
    }
}
