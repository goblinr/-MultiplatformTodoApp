package com.a65apps.multiplatform.interaction

/**
 * Редуктор состояний. Преобразовывает входящее состояние [State] в соответствии с входящим действием [Action]
 * на новое состояние
 */
interface Reducer<S : State, A : Action> {
    fun reduce(state: S, action: A): S
}
