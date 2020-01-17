package com.a65apps.multiplatform.sample.presentation.navigation

import com.a65apps.multiplatform.interaction.navigation.Route
import com.a65apps.multiplatform.interaction.navigation.Router as MppRouter
import ru.terrakok.cicerone.Router

class MainRouter : Router(), MppRouter {

    override fun forward(route: Route) {
        navigateTo(Screens.transform(route))
    }

    override fun replace(route: Route) {
        replaceScreen(Screens.transform(route))
    }

    override fun back() {
        exit()
    }
}
