package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.navigation.Route
import com.a65apps.multiplatform.interaction.navigation.Router
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.ofType

class ForwardMiddleware(
    private val router: Router,
    private val schedulers: Schedulers
) : Middleware<MainAction, MainState> {

    override fun bind(
        actions: Observable<MainAction>,
        state: Observable<MainState>
    ): Observable<MainAction> = actions.ofType<MainAction.NavigateForward>()
        .observeOn(schedulers.main)
        .map {
            router.forward(Route(it.screen, it.data))
            MainAction.Idle
        }
}

class ReplaceMiddleware(
    private val router: Router,
    private val schedulers: Schedulers
) : Middleware<MainAction, MainState> {

    override fun bind(
        actions: Observable<MainAction>,
        state: Observable<MainState>
    ): Observable<MainAction> = actions.ofType<MainAction.NavigateReplace>()
        .observeOn(schedulers.main)
        .map {
            router.replace(Route(it.screen, it.data))
            MainAction.Idle
        }
}

class BackMiddleware(
    private val router: Router,
    private val schedulers: Schedulers
) : Middleware<MainAction, MainState> {

    override fun bind(
        actions: Observable<MainAction>,
        state: Observable<MainState>
    ): Observable<MainAction> = actions.ofType<MainAction.NavigateBack>()
        .observeOn(schedulers.main)
        .map {
            router.back()
            MainAction.Idle
        }
}
