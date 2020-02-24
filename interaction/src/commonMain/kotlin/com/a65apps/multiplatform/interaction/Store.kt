package com.a65apps.multiplatform.interaction

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.plusAssign
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.concatMapSingle
import com.badoo.reaktive.observable.distinctUntilChanged
import com.badoo.reaktive.observable.firstOrError
import com.badoo.reaktive.observable.merge
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.single.map
import com.badoo.reaktive.subject.publish.PublishSubject

/**
 * Хранилище состояния. Связывает поток действий [Action] с редуктором состояний [Reducer] и применяет результат на
 * поток состояний [State]. Так же привязывает посредников бизнес-логики [Middleware] к потокам действий и состояний.
 *
 * @param reducer - редуктор состояний
 * @param middleware - множество посредников бизнес-логики
 * @param initialState - провайдер состояния по умолчанию
 * @param stateSubjectProvider - провайдер потока состояний
 */
open class Store<S : State, A : Action>(
    reducer: Reducer<S, A>,
    middleware: Set<Middleware<A, S>>,
    initialState: StateProvider<S>,
    stateSubjectProvider: StateSubjectProvider<S>
) {
    private val states = stateSubjectProvider.provide(initialState)
    private val actions = PublishSubject<A>()
    private val disposable = CompositeDisposable()

    init {
        disposable += actions.concatMapSingle { action ->
            states.firstOrError()
                .map { latest -> reducer.reduce(latest, action) }
        }.distinctUntilChanged()
            .subscribe(onNext = states::onNext)

        disposable += middleware.map { it.bind(actions, states) }.merge()
            .subscribe(onNext = actions::onNext)
    }

    fun acceptAction(action: A) {
        actions.onNext(action)
    }
    fun acceptUnsafeAction(action: Action) {
        @Suppress("UNCHECKED_CAST")
        actions.onNext(action as A)
    }

    fun states(): Observable<S> = states

    fun dispose() {
        disposable.dispose()
    }
}
