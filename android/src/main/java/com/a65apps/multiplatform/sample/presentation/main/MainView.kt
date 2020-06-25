package com.a65apps.multiplatform.sample.presentation.main

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.StructurallyEqual
import androidx.compose.mutableStateOf
import androidx.compose.remember
import androidx.compose.staticAmbientOf
import androidx.ui.foundation.Text
import androidx.ui.material.ColorPalette
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.ScaffoldState
import androidx.ui.material.TopAppBar
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette
import androidx.ui.tooling.preview.Preview
import com.a65apps.multiplatform.sample.presentation.ViewState
import com.a65apps.multiplatform.sample.presentation.create.CreateTaskView
import com.a65apps.multiplatform.sample.presentation.create.CreateViewState
import com.a65apps.multiplatform.sample.presentation.todo.TodoListView
import com.a65apps.multiplatform.sample.presentation.todo.TodoListViewState

data class MainViewState(
    val screen: ScreenView = ScreenView.TODO_LIST,
    val state: ViewState = TodoListViewState()
) : ViewState

data class MenuAction(
    val icon: @Composable() () -> Unit
)

class MenuAmbient(
    menuActions: List<MenuAction> = listOf()
) {
    val menuActions = mutableStateOf(menuActions, StructurallyEqual)
}

private val MenuStateAmbient = staticAmbientOf { MenuAmbient() }
@Composable
fun menuState(): MenuAmbient = MenuStateAmbient.current

@Composable
fun MainView(
    state: MutableState<MainViewState>,
    colors: ColorPalette = lightColorPalette()
) {
    val scaffoldState = remember { ScaffoldState() }
    val menuState = menuState()
    val (screen, viewState) = state.value

    MaterialTheme(colors = colors) {
        Scaffold(
            scaffoldState = scaffoldState,
            topAppBar = {
                TopAppBar(
                    title = { Text(screen.name) },
                    actions = {
                        menuState.menuActions.value.forEach {
                            it.icon()
                        }
                    }
                )
            },
            bodyContent = {
                when (screen) {
                    ScreenView.TODO_LIST -> TodoListView(
                        viewState as TodoListViewState
                    )
                    ScreenView.CREATE_TASK -> CreateTaskView(
                        viewState as CreateViewState
                    )
                    ScreenView.ARCHIVE_LIST -> TODO()
                }
            }
        )
    }
}

@Preview("light")
@Composable
fun PreviewLightMain() {
    MainView(
        state = mutableStateOf(MainViewState())
    )
}

@Preview("night")
@Composable
fun PreviewNightMain() {
    MainView(
        state = mutableStateOf(MainViewState()),
        colors = darkColorPalette()
    )
}
