package com.a65apps.multiplatform.sample.di.data

import com.a65apps.multiplatform.sample.di.data.todo.TodoDataModule
import dagger.Module

@Module(
    includes = [
        TodoDataModule::class
    ]
)
interface AppDataModule
