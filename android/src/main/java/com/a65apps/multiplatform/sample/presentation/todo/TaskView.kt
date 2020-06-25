package com.a65apps.multiplatform.sample.presentation.todo

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.material.darkColorPalette
import androidx.ui.material.ripple.ripple
import androidx.ui.text.style.TextDecoration
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp

@Composable
fun TaskView(
    task: TaskViewState,
    onClick: (String) -> Unit = {}
) {
    val typography = MaterialTheme.typography

    Clickable(
        modifier = Modifier.ripple(),
        onClick = {
            onClick(task.id)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth() + Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = if (task.status == TaskStatusView.DONE)
                    typography.h5.copy(textDecoration = TextDecoration.LineThrough)
                else
                    typography.h5
            )
            Text(
                text = task.description,
                style = if (task.status == TaskStatusView.DONE)
                    typography.body1.copy(textDecoration = TextDecoration.LineThrough)
                else
                    typography.body1
            )
        }
    }
}

@Preview
@Composable
fun taskViewPendingLight() {
    MaterialTheme {
        Surface {
            TaskView(
                TaskViewState(
                    id = "1",
                    title = "Title",
                    description = "Description",
                    status = TaskStatusView.PENDING
                )
            )
        }
    }
}

@Preview
@Composable
fun taskViewDoneLight() {
    MaterialTheme {
        Surface {
            TaskView(
                TaskViewState(
                    id = "1",
                    title = "Title",
                    description = "Description",
                    status = TaskStatusView.DONE
                )
            )
        }
    }
}

@Preview
@Composable
fun taskViewPendingDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            TaskView(
                TaskViewState(
                    id = "1",
                    title = "Title",
                    description = "Description",
                    status = TaskStatusView.PENDING
                )
            )
        }
    }
}

@Preview
@Composable
fun taskViewDoneDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            TaskView(
                TaskViewState(
                    id = "1",
                    title = "Title",
                    description = "Description",
                    status = TaskStatusView.DONE
                )
            )
        }
    }
}
