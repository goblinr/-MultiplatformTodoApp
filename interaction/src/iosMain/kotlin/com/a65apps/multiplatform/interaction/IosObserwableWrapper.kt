package com.a65apps.multiplatform.interaction

import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.ObservableWrapper
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.Scheduler

class IosObserwableWrapper<T>(
    private val inner: Observable<T>
) : ObservableWrapper<T>(inner) {

    fun observeOn(scheduler: Scheduler): IosObserwableWrapper<T> {
        inner.observeOn(scheduler)
        return this
    }
}
