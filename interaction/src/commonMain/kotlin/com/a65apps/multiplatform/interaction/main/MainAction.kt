package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.navigation.Screen

sealed class MainAction : Action {

    data class NavigateForward(
        val screen: Screen,
        val data: Any? = null
    ) : MainAction()

    data class NavigateReplace(
        val screen: Screen,
        val data: Any? = null
    ) : MainAction()

    object NavigateBack : MainAction()

    object Idle : MainAction()
}
