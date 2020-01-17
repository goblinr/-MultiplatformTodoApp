package com.a65apps.multiplatform.interaction.main

import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.navigation.Screen

data class MainState(
    val screen: Screen = Screen.TODO_LIST,
    val backStack: List<Screen> = listOf()
) : State
