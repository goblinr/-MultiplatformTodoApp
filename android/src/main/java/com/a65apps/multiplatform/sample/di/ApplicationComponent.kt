package com.a65apps.multiplatform.sample.di

import android.content.Context
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.sample.di.data.AppDataModule
import com.a65apps.multiplatform.sample.di.main.MainComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    SchedulersModule::class,
    AppDataModule::class,
    ApplicationModule::class
])
@Singleton
interface ApplicationComponent {

    val mainComponentFactory: MainComponent.Factory

    val schedulers: Schedulers

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}
