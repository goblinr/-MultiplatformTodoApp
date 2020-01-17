package com.a65apps.multiplatform.interaction

import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject

interface StateSubjectProvider<S : State> {

    fun provide(initialState: StateProvider<S>): Subject<S>
}

class DefaultStateSubjectProvider<S : State> : StateSubjectProvider<S> {

    override fun provide(initialState: StateProvider<S>): Subject<S> =
        BehaviorSubject(initialState.get())
}
