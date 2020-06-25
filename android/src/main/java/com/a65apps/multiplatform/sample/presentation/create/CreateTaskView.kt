package com.a65apps.multiplatform.sample.presentation.create

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Layout
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.layout.Column
import androidx.ui.layout.Spacer
import androidx.ui.layout.Stack
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.fillMaxWidth
import androidx.ui.layout.padding
import androidx.ui.layout.wrapContentSize
import androidx.ui.material.Button
import androidx.ui.material.Card
import androidx.ui.material.CircularProgressIndicator
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.material.darkColorPalette
import androidx.ui.tooling.preview.Preview
import androidx.ui.unit.dp
import androidx.ui.unit.ipx
import com.a65apps.multiplatform.sample.presentation.main.menuState
import com.a65apps.multiplatform.sample.presentation.todo.TaskStatusView
import com.a65apps.multiplatform.sample.presentation.todo.TaskViewState

@Composable
fun HintEditText(
    hintText: @Composable() () -> Unit,
    value: String,
    onTextChanged: (String) -> Unit
) {
    val inputField = @Composable {
        TextField(
            value = TextFieldValue(value),
            onValueChange = { onTextChanged(it.text) }
        )
    }
    if (value.isNotEmpty()) {
        inputField()
    } else {
        Layout(children = {
            inputField()
            hintText()
        }, measureBlock = { measurable, constraints, _ ->
            val inputfieldPlacable = measurable[0].measure(constraints)
            val hintTextPlacable = measurable[1].measure(constraints)
            layout(inputfieldPlacable.width, inputfieldPlacable.height) {
                inputfieldPlacable.place(0.ipx, 0.ipx)
                hintTextPlacable.place(0.ipx, 0.ipx)
            }
        })
    }
}

@Composable
fun CreateTaskView(state: CreateViewState) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colors
    val result = state.result
    menuState().menuActions.value = listOf()

    Stack(modifier = Modifier.fillMaxSize()) {
        if (result == null) {
            Column(
                modifier = Modifier.fillMaxWidth() +
                    Modifier.gravity(Alignment.Center) +
                    Modifier.padding(16.dp)
            ) {
                HintEditText(
                    hintText = {
                        Text(
                            text = "Title",
                            style = typography.body1.copy(
                                color = colors.secondary
                            )
                        )
                    },
                    value = state.title,
                    onTextChanged = state.onTitleChanged
                )
                Spacer(modifier = Modifier.padding(8.dp))
                HintEditText(
                    hintText = {
                        Text(
                            text = "Description",
                            style = typography.body2.copy(
                                color = colors.secondary
                            )
                        )
                    },
                    value = state.description,
                    onTextChanged = state.onDescriptionChanged
                )

                if (state.error.isNotEmpty()) {
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = state.error,
                        style = typography.body2
                            .copy(color = colors.error)
                    )
                }
            }

            Button(
                modifier = Modifier.gravity(Alignment.BottomCenter) +
                        Modifier.padding(16.dp),
                onClick = state.onSubmitClicked
            ) {
                Text(
                    text = "Create",
                    style = typography.body1
                )
            }
        } else {
            Card(
                elevation = 8.dp,
                modifier = Modifier.gravity(Alignment.Center)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = result.title,
                        style = typography.h4,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = result.description,
                        style = typography.body1,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }
    }
}

private val model = CreateViewState(
    title = "Title",
    description = "Description"
)

private val modelError = model.copy(error = "Error!")

private val modelLoading = model.copy(isLoading = true)

private val modelResult = model.copy(
    result = TaskViewState(
        id = "1",
        title = "Title",
        description = "Description",
        status = TaskStatusView.PENDING
    )
)

@Preview
@Composable
fun createLight() {
    MaterialTheme {
        Surface {
            CreateTaskView(model)
        }
    }
}

@Preview
@Composable
fun createDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            CreateTaskView(model)
        }
    }
}

@Preview
@Composable
fun createErrorLight() {
    MaterialTheme {
        Surface {
            CreateTaskView(modelError)
        }
    }
}

@Preview
@Composable
fun createErrorDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            CreateTaskView(modelError)
        }
    }
}

@Preview
@Composable
fun createLoadingLight() {
    MaterialTheme {
        Surface {
            CreateTaskView(modelLoading)
        }
    }
}

@Preview
@Composable
fun createLoadingDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            CreateTaskView(modelLoading)
        }
    }
}

@Preview
@Composable
fun createResultLight() {
    MaterialTheme {
        Surface {
            CreateTaskView(modelResult)
        }
    }
}

@Preview
@Composable
fun createResultDark() {
    MaterialTheme(colors = darkColorPalette()) {
        Surface {
            CreateTaskView(modelResult)
        }
    }
}
