package com.a65apps.multiplatform.interaction

/**
 * Интерфейс для определения сущности как состояние
 */
interface State

expect fun <T : State> T.freeze(): T
