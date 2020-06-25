package com.a65apps.multiplatform.interaction.navigation

class CommonRouter(
    private val navigator: Navigator
) : Router {

    override fun forward(route: Route) {
        navigator.executeCommand(Command.Forward(route))
    }

    override fun replace(route: Route) {
        navigator.executeCommand(Command.Replace(route))
    }

    override fun back() {
        navigator.executeCommand(Command.Back)
    }
}
