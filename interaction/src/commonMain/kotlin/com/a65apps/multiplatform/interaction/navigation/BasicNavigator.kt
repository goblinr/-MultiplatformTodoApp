package com.a65apps.multiplatform.interaction.navigation

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.Store
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.distinctUntilChanged
import com.badoo.reaktive.observable.firstOrError
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.single.blockingGet
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.utils.ThreadLocalHolder

class BasicNavigator(
    private val factoryMap: Map<Screen, StoreFactory<out State, out Action>>,
    private var schedulers: Schedulers
) : Navigator {

    override val snapShot: List<Pair<Screen, State>>
        get() = backStack.get()?.map {
            it.screen to it.store.states().firstOrError().blockingGet()
        } ?: listOf()

    override val isEmpty: Boolean
        get() = backStack.get()?.isEmpty() ?: true

    private var backStack = ThreadLocalHolder(listOf<BackStackEntry>())

    private val stateSubject = BehaviorSubject(
        Triple(DEFAULT_SCREEN, DEFAULT_STATE, DEFAULT_ACTIONS)
    )

    override val state: Observable<Triple<Screen, State, (Action) -> Unit>> = stateSubject
        .distinctUntilChanged()

    override fun executeCommand(command: Command) {
        when (command) {
            is Command.Forward -> {
                if (!isEmpty) {
                    unsubscribeLast()
                }
                val route = command.route
                val store = createStore(route)
                addEntry(route, store)
            }
            is Command.Replace -> {
                if (!isEmpty) {
                    dropLast()
                }
                val route = command.route
                val store = createStore(route)
                addEntry(route, store)
            }
            Command.Back -> {
                if (!isEmpty) {
                    dropLast()
                }
                if (!isEmpty) {
                    subscribeLast()
                }
            }
        }
        stateChanged()
    }

    private fun addEntry(
        route: Route,
        store: Store<out State, out Action>
    ) {
        backStack.get()?.let {
            val stack = it + BackStackEntry(
                route.screen,
                store,
                ThreadLocalHolder(store.states()
                    .observeOn(schedulers.main)
                    .subscribe(isThreadLocal = true) {
                        stateChanged()
                    })
            )
            backStack.set(stack)
        }
    }

    override fun restore(state: List<Pair<Screen, State>>) {
        backStack.set(state.map { (screen, screenState) ->
            val factory = factoryMap[screen] ?: throw IllegalArgumentException()
            val store = factory.restore(screenState)
            BackStackEntry(
                screen = screen,
                store = store,
                disposable = ThreadLocalHolder(Disposable())
            )
        })
        if (!isEmpty) {
            subscribeLast()
        }
        stateChanged()
    }

    private fun stateChanged() {
        val entry = backStack.get()?.lastOrNull() ?: return
        val state = entry.store.states().firstOrError().blockingGet()
        val actions: (Action) -> Unit = {
            entry.store.acceptUnsafeAction(it)
        }
        stateSubject.onNext(
            Triple(
                entry.screen,
                state,
                actions
            )
        )
    }

    private fun createStore(route: Route): Store<out State, out Action> {
        val factory = factoryMap[route.screen] ?: throw IllegalArgumentException()
        return factory.create(route.data)
    }

    private fun dropLast() {
        backStack.get()?.let {
            val last = it.last()
            last.disposable.get()?.dispose()
            last.store.dispose()
            backStack.set(it - last)
        }
    }

    private fun unsubscribeLast() {
        backStack.get()?.let {
            it.last().disposable.get()?.dispose()
        }
    }

    private fun subscribeLast() {
        val entry = backStack.get()?.last() ?: return
        entry.disposable.get()?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }

        val disposable = entry.store.states()
            .observeOn(schedulers.main)
            .subscribe(isThreadLocal = true) {
                stateChanged()
            }
        entry.disposable.set(disposable)
    }

    private class BackStackEntry(
        val screen: Screen,
        val store: Store<out State, out Action>,
        val disposable: ThreadLocalHolder<Disposable>
    )
}
