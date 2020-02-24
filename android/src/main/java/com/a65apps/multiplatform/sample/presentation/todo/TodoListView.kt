package com.a65apps.multiplatform.sample.presentation.todo

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.paint
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.clickable
import androidx.ui.graphics.Color
import androidx.ui.graphics.ColorFilter
import androidx.ui.graphics.vector.VectorAsset
import androidx.ui.graphics.vector.VectorPainter
import androidx.ui.layout.Column
import androidx.ui.layout.Stack
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.padding
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.Divider
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.material.darkColorPalette
import androidx.ui.material.ripple.ripple
import androidx.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import com.a65apps.multiplatform.sample.R
import com.a65apps.multiplatform.sample.presentation.main.MenuAction
import com.a65apps.multiplatform.sample.presentation.main.menuState

@Composable
fun vectorImage(vector: VectorAsset, tint: Color = Color.Transparent) {
    Box(
        modifier = Modifier.paint(
            painter = VectorPainter(asset = vector),
            colorFilter = ColorFilter.tint(tint)
        )
    )
}

@Composable
fun TodoListView(state: TodoListViewState) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colors
    val iconAdd = vectorResource(id = R.drawable.ic_add)
    val iconArchive = vectorResource(id = R.drawable.ic_archive)
    val iconRefresh = vectorResource(id = R.drawable.ic_baseline_refresh)

    menuState().menuActions.value = listOf(
        MenuAction {
            Box(modifier = Modifier.padding(8.dp) +
                    Modifier.clickable(onClick = state.onRefresh) +
                    Modifier.ripple(bounded = false)) {
                vectorImage(
                    vector = iconRefresh,
                    tint = colors.onSurface
                )
            }
        }
    )

    Stack(modifier = Modifier.fillMaxSize()) {
        VerticalScroller(modifier = Modifier.fillMaxSize()) {
            Column {
                state.todoList.forEach { task ->
                    TaskView(
                        task = task,
                        onClick = state.onSwitchTask
                    )
                    Divider(color = colors.onSurface.copy(alpha = 0.3f))
                }
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.gravity(Alignment.Center)
            )
        }
        if (state.error.isNotEmpty()) {
            Text(
                text = state.error,
                style = typography.h4
                    .copy(color = colors.error),
                modifier = Modifier.gravity(Alignment.Center)
            )
        }
        if (!state.isLoading && state.error.isEmpty()) {
            Column(modifier = Modifier.gravity(Alignment.BottomEnd)) {
                FloatingActionButton(
                    modifier = Modifier.padding(all = 16.dp),
                    onClick = state.onCreateTask
                ) {
                    vectorImage(
                        vector = iconAdd,
                        tint = colors.onPrimary
                    )
                }

                if (state.showArchive) {
                    FloatingActionButton(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                        onClick = state.onArchiveTasks
                    ) {
                        vectorImage(
                            vector = iconArchive,
                            tint = colors.onPrimary
                        )
                    }
                }
            }
        }
    }
}

private val model = TodoListViewState(
    todoList = listOf(
        TaskViewState(
            id = "1",
            title = "One",
            description = "One description",
            status = TaskStatusView.PENDING
        ),
        TaskViewState(
            id = "2",
            title = "Two",
            description = "Two description",
            status = TaskStatusView.DONE
        )
    ),
    showArchive = true
)
private val modelLoading = TodoListViewState(
    todoList = listOf(
        TaskViewState(
            id = "1",
            title = "One",
            description = "One description",
            status = TaskStatusView.PENDING
        )
    ),
    isLoading = true
)
private val modelError = TodoListViewState(
    todoList = listOf(
        TaskViewState(
            id = "1",
            title = "One",
            description = "One description",
            status = TaskStatusView.PENDING
        )
    ),
    error = "Error!"
)

@Preview("Todo List Light")
@Composable
fun todoListViewLight() {
    MaterialTheme {
        Surface {
            TodoListView(model)
        }
    }
}

@Preview("Todo List Dark")
@Composable
fun todoListViewDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            TodoListView(model)
        }
    }
}

@Preview("todoListViewLoadingLight")
@Composable
fun todoListViewLoadingLight() {
    MaterialTheme {
        Surface {
            TodoListView(modelLoading)
        }
    }
}

@Preview("todoListViewLoadingDark")
@Composable
fun todoListViewLoadingDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            TodoListView(modelLoading)
        }
    }
}

@Preview("todoListViewErrorLight")
@Composable
fun todoListViewErrorLight() {
    MaterialTheme {
        Surface {
            TodoListView(modelError)
        }
    }
}

@Preview("todoListViewErrorDark")
@Composable
fun todoListViewErrorDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            TodoListView(modelError)
        }
    }
}
