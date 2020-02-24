package com.a65apps.multiplatform.sample.presentation.main

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.mutableStateOf
import androidx.lifecycle.ViewModelProvider
import androidx.ui.core.setContent
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette
import com.a65apps.multiplatform.interaction.State
import com.a65apps.multiplatform.interaction.create.CreateState
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.interaction.navigation.Screen
import com.a65apps.multiplatform.interaction.todo.TodoState
import com.a65apps.multiplatform.sample.di.component
import com.a65apps.multiplatform.sample.presentation.create.CreateParcelable
import com.a65apps.multiplatform.sample.presentation.create.toInteraction
import com.a65apps.multiplatform.sample.presentation.create.toParcelable
import com.a65apps.multiplatform.sample.presentation.create.toPresentation
import com.a65apps.multiplatform.sample.presentation.todo.TodoListParcelable
import com.a65apps.multiplatform.sample.presentation.todo.toPresentation
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.firstOrError
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.single.blockingGet

private const val APP_STATE = "app_state"
private const val NAV_STATE = "nav_state"

enum class ScreenView {
    TODO_LIST,
    CREATE_TASK,
    ARCHIVE_LIST
}

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state = savedInstanceState?.let {
            it.getParcelable<MainParcelable>(APP_STATE)?.toInteraction()
        }
        val navState = savedInstanceState
            ?.getParcelableArrayList<BackStackEntry>(NAV_STATE)?.map {
                it.screen to it.state.toInteraction(it.screen)
            }

        viewModel = ViewModelProvider(this, MainViewModelFactory(component, state, navState))
            .get(MainViewModel::class.java)

        val viewState = mutableStateOf(MainViewState())
        disposable = viewModel.navigator.state
            .observeOn(component.schedulers.main)
            .subscribe(isThreadLocal = true) { (screen, state, actions) ->
                val screenView = ScreenView.valueOf(screen.name)
                val newState = when (screenView) {
                    ScreenView.TODO_LIST -> (state as TodoState).toPresentation(actions)
                    ScreenView.CREATE_TASK -> (state as CreateState).toPresentation(actions)
                    ScreenView.ARCHIVE_LIST -> TODO()
                }
                viewState.value = MainViewState(screenView, newState)
            }

        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        setContent {
            MainView(
                state = viewState,
                colors = if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    darkColorPalette()
                } else {
                    lightColorPalette()
                }
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            APP_STATE,
            viewModel.states.firstOrError(IllegalStateException())
                .blockingGet().toParcelable()
        )
        val navState = viewModel.navigator.snapShot
        val list = arrayListOf<BackStackEntry>()
        list.addAll(
            navState.map { (screen, state) ->
                BackStackEntry(
                    screen = screen,
                    state = state.toParcelable(screen)
                )
            }
        )
        outState.putParcelableArrayList(NAV_STATE, list)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        viewModel.onBack()
        Handler(Looper.getMainLooper()).post {
            if (viewModel.navigator.isEmpty) {
                finish()
            }
        }
    }

    private fun State.toParcelable(screen: Screen): Parcelable = when (screen) {
        Screen.TODO_LIST -> {
            this as TodoState
            TodoListParcelable(
                error = error,
                isLoading = isLoading,
                showArchive = showArchive
            )
        }
        Screen.CREATE_TASK -> {
            this as CreateState
            CreateParcelable(
                isLoading = isLoading,
                error = error,
                title = title,
                description = description,
                result = result?.toParcelable()
            )
        }
        Screen.ARCHIVE_LIST -> TODO()
    }

    private fun Parcelable.toInteraction(screen: Screen): State = when (screen) {
        Screen.TODO_LIST -> {
            this as TodoListParcelable
            TodoState(
                error = error,
                isLoading = isLoading,
                showArchive = showArchive
            )
        }
        Screen.CREATE_TASK -> {
            this as CreateParcelable
            this.toInteraction()
        }
        Screen.ARCHIVE_LIST -> TODO()
    }
}

data class BackStackEntry(
    val screen: Screen,
    val state: Parcelable
) : Parcelable {
    constructor(parcel: Parcel) : this(
        Screen.valueOf(parcel.readString()!!),
        parcel.readParcelable<Parcelable>(BackStackEntry::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(screen.name)
        parcel.writeParcelable(state, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BackStackEntry> {
        override fun createFromParcel(parcel: Parcel): BackStackEntry {
            return BackStackEntry(parcel)
        }

        override fun newArray(size: Int): Array<BackStackEntry?> {
            return arrayOfNulls(size)
        }
    }
}

data class MainParcelable(
    val screen: Screen,
    val backStack: List<Screen>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        Screen.valueOf(parcel.readString()!!),
        with(mutableListOf<String>()) {
            parcel.readStringList(this)
            this
        }.map { Screen.valueOf(it) }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(screen.name)
        parcel.writeStringList(backStack.map { it.name })
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainParcelable> {
        override fun createFromParcel(parcel: Parcel): MainParcelable {
            return MainParcelable(parcel)
        }

        override fun newArray(size: Int): Array<MainParcelable?> {
            return arrayOfNulls(size)
        }
    }
}

private fun MainParcelable.toInteraction(): MainState =
    MainState(
        screen = screen,
        backStack = backStack
    )
private fun MainState.toParcelable(): MainParcelable =
    MainParcelable(
        screen = screen,
        backStack = backStack
    )
