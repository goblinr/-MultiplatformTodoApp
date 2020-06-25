package com.a65apps.multiplatform.interaction.navigation

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.State
import com.badoo.reaktive.observable.Observable

interface Navigator {

    val state: Observable<Triple<Screen, State, (Action) -> Unit>>

    val snapShot: List<Pair<Screen, State>>

    val isEmpty: Boolean

    fun executeCommand(command: Command)

    fun restore(state: List<Pair<Screen, State>>)
}
