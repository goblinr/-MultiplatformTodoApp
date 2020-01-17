package com.a65apps.multiplatform.interaction

import com.badoo.reaktive.observable.Observable

/**
 * Посредник бизнес-логики. Вклинивается в между последовательностью [Action] -> [Reducer] ->[State]
 * с помощью метода [bind]
 */
interface Middleware<A : Action, S : State> {

    fun bind(actions: Observable<A>, state: Observable<S>): Observable<A>
}
