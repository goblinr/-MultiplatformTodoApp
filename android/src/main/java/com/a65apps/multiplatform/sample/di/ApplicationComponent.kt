package com.a65apps.multiplatform.sample.di

import com.a65apps.multiplatform.sample.di.data.AppDataModule
import com.a65apps.multiplatform.sample.di.main.MainActivityModule
import com.a65apps.multiplatform.sample.di.main.MainViewModelModule
import com.a65apps.multiplatform.sample.di.navigation.NavigationModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [
    AndroidSupportInjectionModule::class,
    MainActivityModule::class,
    NavigationModule::class,
    SchedulersModule::class,
    MainViewModelModule::class,
    AppDataModule::class
])
@Singleton
interface ApplicationComponent : AndroidInjector<TodoApplication>
