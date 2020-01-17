package com.a65apps.multiplatform.sample.di

import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.sample.core.AndroidSchedulers
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface SchedulersModule {

    @Binds
    @Singleton
    fun bindsSchedulers(schedulers: AndroidSchedulers): Schedulers
}
