package com.a65apps.multiplatform.sample.presentation.main

import android.os.Parcelable
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.interaction.navigation.Screen
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class MainParcelable(
    val screen: Screen = Screen.TODO_LIST,
    val backStack: List<Screen> = listOf()
) : Parcelable

internal fun MainParcelable.toDomain(): MainState =
    MainState(
        screen = screen,
        backStack = backStack
    )

internal fun MainState.toParcelable(): MainParcelable =
    MainParcelable(
        screen = screen,
        backStack = backStack
    )
