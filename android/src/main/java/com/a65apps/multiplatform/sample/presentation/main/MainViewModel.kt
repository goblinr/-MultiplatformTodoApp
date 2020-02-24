package com.a65apps.multiplatform.sample.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.main.MainAction
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.interaction.navigation.Navigator
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.a65apps.multiplatform.sample.di.ApplicationComponent
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val store: Store<MainState, MainAction>,
    val navigator: Navigator,
    navState: List<Pair<Screen, State>>?
) : ViewModel() {

    val states = store.states()

    init {
        if (navState != null) {
            Log.d("Navigator", "restoring state")
            navigator.restore(navState)
        } else {
            Log.d("Navigator", "navigation state is empty")
        }
    }

    fun onBack() {
        store.acceptAction(MainAction.NavigateBack)
    }
}

class MainViewModelFactory(
    private val appComponent: ApplicationComponent,
    private val restoredState: MainState? = null,
    private val navState: List<Pair<Screen, State>>? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        appComponent.mainComponentFactory
            .create(restoredState, navState)
            .viewModel
            .get() as T
}
