package com.a65apps.multiplatform.interaction

import com.a65apps.multiplatform.interaction.navigation.Navigator
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe

class ApplicationContainer(
    navigator: Navigator,
    schedulers: Schedulers,
    states: (Screen, State, (Action) -> Unit) -> Unit
) {
    private val disposable = navigator.state
        .observeOn(schedulers.main)
        .subscribe(isThreadLocal = true) { (screen, state, actions) ->
            states(screen, state, actions)
        }

    fun dispose() {
        disposable.dispose()
    }
}
