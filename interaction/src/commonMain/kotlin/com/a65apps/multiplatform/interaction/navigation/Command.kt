package com.a65apps.multiplatform.interaction.navigation

sealed class Command {

    data class Forward(
        val route: Route
    ) : Command()

    data class Replace(
        val route: Route
    ) : Command()

    object Back : Command()
}
