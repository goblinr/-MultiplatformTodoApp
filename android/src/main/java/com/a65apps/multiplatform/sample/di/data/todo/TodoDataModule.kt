package com.a65apps.multiplatform.sample.di.data.todo

import com.a65apps.multiplatform.interaction.data.ExtendedTaskRepository
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.sample.data.todo.ExtendedNetworkTaskRepository
import com.a65apps.multiplatform.sample.data.todo.NetworkTaskRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = [TodoNetworkModule::class]
)
interface TodoDataModule {

    @Binds
    @Singleton
    fun bindsNetworkTaskRepository(repository: NetworkTaskRepository): TaskRepository

    @Binds
    @Singleton
    fun bindsExtendedNetworkTaskRepository(repository: ExtendedNetworkTaskRepository): ExtendedTaskRepository
}
