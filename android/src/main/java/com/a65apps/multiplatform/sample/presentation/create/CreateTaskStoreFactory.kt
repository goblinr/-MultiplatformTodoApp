package com.a65apps.multiplatform.sample.presentation.create

import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.create.CreateAction
import com.a65apps.multiplatform.interaction.create.CreateState
import com.a65apps.multiplatform.interaction.navigation.StoreFactory
import com.a65apps.multiplatform.sample.di.main.MainComponent
import javax.inject.Inject

class CreateTaskStoreFactory @Inject constructor(
    private val mainComponent: MainComponent
) : StoreFactory<CreateState, CreateAction> {

    override fun create(data: Any?): Store<CreateState, CreateAction> =
        mainComponent.createFactory.create(null).store.get()

    override fun restore(state: State): Store<CreateState, CreateAction> =
        mainComponent.createFactory.create(state as CreateState).store.get()
}
