package com.a65apps.multiplatform.sample.di.archive

import com.a65apps.multiplatform.interaction.archive.ArchiveState
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.di.ViewModelComponent
import com.a65apps.multiplatform.sample.di.ViewModelComponentBuilder
import com.a65apps.multiplatform.sample.di.main.MainScope
import com.a65apps.multiplatform.sample.presentation.archive.ArchiveListViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class ArchiveListScope

@Module(subcomponents = [ArchiveListViewModelModule.ArchiveListSubcomponent::class])
interface ArchiveListViewModelModule {

    @MainScope
    @Binds
    @IntoMap
    @ClassKey(ArchiveListViewModel::class)
    fun bindsBuilder(builder: ArchiveListSubcomponent.Builder): ComponentBuilder

    @ArchiveListScope
    @Subcomponent(
        modules = [ArchiveListModule::class]
    )
    interface ArchiveListSubcomponent : ViewModelComponent<ArchiveState, ArchiveListViewModel> {

        @Subcomponent.Builder
        interface Builder : ViewModelComponentBuilder<ArchiveState, ArchiveListViewModel,
                ArchiveListSubcomponent>
    }
}
