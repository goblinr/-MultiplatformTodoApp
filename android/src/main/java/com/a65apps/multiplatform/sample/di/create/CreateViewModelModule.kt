package com.a65apps.multiplatform.sample.di.create

import com.a65apps.multiplatform.interaction.create.CreateState
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.di.ViewModelComponent
import com.a65apps.multiplatform.sample.di.ViewModelComponentBuilder
import com.a65apps.multiplatform.sample.di.main.MainScope
import com.a65apps.multiplatform.sample.presentation.create.CreateViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class CreateScope

@Module(subcomponents = [CreateViewModelModule.CreateSubcomponent::class])
interface CreateViewModelModule {

    @MainScope
    @Binds
    @IntoMap
    @ClassKey(CreateViewModel::class)
    fun bindsBuilder(builder: CreateSubcomponent.Builder): ComponentBuilder

    @CreateScope
    @Subcomponent(
        modules = [CreateModule::class]
    )
    interface CreateSubcomponent : ViewModelComponent<CreateState, CreateViewModel> {

        @Subcomponent.Builder
        interface Builder : ViewModelComponentBuilder<CreateState, CreateViewModel,
                CreateSubcomponent>
    }
}
