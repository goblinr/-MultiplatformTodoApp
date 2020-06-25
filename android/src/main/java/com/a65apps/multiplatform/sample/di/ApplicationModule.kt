package com.a65apps.multiplatform.sample.di

import com.a65apps.multiplatform.sample.di.main.MainComponent
import dagger.Module

@Module(
    subcomponents = [MainComponent::class]
)
interface ApplicationModule
