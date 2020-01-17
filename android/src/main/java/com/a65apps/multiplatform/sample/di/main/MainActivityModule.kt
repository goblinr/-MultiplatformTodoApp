package com.a65apps.multiplatform.sample.di.main

import com.a65apps.multiplatform.sample.R
import com.a65apps.multiplatform.sample.di.ComponentFactoryHolder
import com.a65apps.multiplatform.sample.presentation.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator

@Module
interface MainActivityModule {

    @ContributesAndroidInjector(modules = [
        Module::class,
        MainFragmentsModule::class
    ])
    fun contributeMainActivity(): MainActivity

    @dagger.Module
    class Module {

        @Provides
        fun providesNavigator(activity: MainActivity): Navigator =
            SupportAppNavigator(activity, R.id.container)

        @Provides
        fun providesComponentFactoryHolder(activity: MainActivity): ComponentFactoryHolder =
            activity
    }
}
