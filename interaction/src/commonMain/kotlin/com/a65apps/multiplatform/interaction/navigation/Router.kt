package com.a65apps.multiplatform.interaction.navigation

interface Router {

    fun forward(route: Route)

    fun replace(route: Route)

    fun back()
}
