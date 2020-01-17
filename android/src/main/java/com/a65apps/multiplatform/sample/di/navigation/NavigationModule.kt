package com.a65apps.multiplatform.sample.di.navigation

import com.a65apps.multiplatform.interaction.navigation.Router
import com.a65apps.multiplatform.sample.presentation.navigation.MainRouter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder

@Module
class NavigationModule {

    private val cicerone = Cicerone.create(MainRouter())

    @Provides
    @Singleton
    fun providesRouter(): Router = cicerone.router

    @Provides
    @Singleton
    fun providesNavigatorHolder(): NavigatorHolder = cicerone.navigatorHolder
}
