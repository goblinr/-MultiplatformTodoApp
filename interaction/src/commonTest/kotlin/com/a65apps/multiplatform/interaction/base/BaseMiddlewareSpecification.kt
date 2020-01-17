package com.a65apps.multiplatform.interaction.base

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.mock.TestSchedulers
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.subject.publish.PublishSubject
import com.badoo.reaktive.test.observable.TestObservableObserver
import kotlin.test.BeforeTest

abstract class BaseMiddlewareSpecification<A : Action, S : State> {

    protected val schedulers = TestSchedulers()

    private lateinit var actions: PublishSubject<A>
    private lateinit var states: BehaviorSubject<S>
    private lateinit var middleware: Middleware<A, S>

    protected abstract val defaultState: S

    @BeforeTest
    fun setUp() {
        actions = PublishSubject()
        states = BehaviorSubject(defaultState)
        initMocks()
        middleware = createMiddleware()
    }

    protected open fun initMocks() {
//      default body
    }

    protected fun bind(): TestObservableObserver<A> {
        val observer = TestObservableObserver<A>()
        middleware.bind(
            actions = actions,
            state = states
        ).subscribe(observer)

        return observer
    }

    protected fun acceptAction(action: A) {
        actions.onNext(action)
    }

    protected fun prepareState(state: S) {
        states.onNext(state)
    }

    protected abstract fun createMiddleware(): Middleware<A, S>
}
