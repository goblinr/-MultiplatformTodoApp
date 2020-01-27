package com.a65apps.multiplatform.sample.di.main

import com.a65apps.multiplatform.sample.di.archive.ArchiveListFragmentModule
import com.a65apps.multiplatform.sample.di.create.CreateFragmentModule
import com.a65apps.multiplatform.sample.di.todo.TodoListFragmentModule
import com.a65apps.multiplatform.sample.presentation.archive.ArchiveListFragment
import com.a65apps.multiplatform.sample.presentation.create.CreateFragment
import com.a65apps.multiplatform.sample.presentation.todo.TodoListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainFragmentsModule {

    @ContributesAndroidInjector(modules = [TodoListFragmentModule::class])
    fun contributesTodoListFragment(): TodoListFragment

    @ContributesAndroidInjector(modules = [CreateFragmentModule::class])
    fun contributesCreateFragment(): CreateFragment

    @ContributesAndroidInjector(modules = [ArchiveListFragmentModule::class])
    fun contributesArchiveListFragment(): ArchiveListFragment
}
