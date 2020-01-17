package com.a65apps.multiplatform.interaction

/**
 * Интерфейс для предоставления каого-либо состояния по умолчанию
 */
interface StateProvider<S : State> {

    fun get(): S
}
