package com.a65apps.multiplatform.sample.presentation.todo

import android.os.Parcel
import android.os.Parcelable
import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.todo.TodoAction
import com.a65apps.multiplatform.interaction.todo.TodoState
import com.a65apps.multiplatform.sample.presentation.ViewState

enum class TaskStatusView {
    PENDING,
    DONE,
    ARCHIVED
}

data class TaskViewState(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatusView
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        TaskStatusView.valueOf(parcel.readString()!!)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(status.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskViewState> {
        override fun createFromParcel(parcel: Parcel): TaskViewState {
            return TaskViewState(parcel)
        }

        override fun newArray(size: Int): Array<TaskViewState?> {
            return arrayOfNulls(size)
        }
    }
}

fun Task.toViewState(): TaskViewState =
    TaskViewState(
        id = id,
        title = title,
        description = description,
        status = TaskStatusView.valueOf(status.name)
    )
fun TaskViewState.toInteraction() =
    Task(
        id = id,
        title = title,
        description = description,
        status = TaskStatus.valueOf(status.name)
    )

data class TodoListViewState(
    val error: String = "",
    val isLoading: Boolean = false,
    val showArchive: Boolean = false,
    val todoList: List<TaskViewState> = listOf(),
    val onSwitchTask: (String) -> Unit = {},
    val onCreateTask: () -> Unit = {},
    val onArchiveTasks: () -> Unit = {},
    val onUnarchiveTasks: () -> Unit = {},
    val onRefresh: () -> Unit = {}
) : ViewState

fun TodoState.toPresentation(actions: (Action) -> Unit) = TodoListViewState(
    error = error,
    isLoading = isLoading,
    showArchive = showArchive,
    todoList = todoList.map { it.toViewState() },
    onSwitchTask = {
        actions(TodoAction.Switch(it))
    },
    onCreateTask = {
        actions(TodoAction.CreateTask)
    },
    onArchiveTasks = {
        actions(TodoAction.Archive)
    },
    onUnarchiveTasks = {
        actions(TodoAction.GoToArchive)
    },
    onRefresh = {
        actions(TodoAction.Load)
    }
)

data class TodoListParcelable(
    var error: String = "",
    var isLoading: Boolean = false,
    var showArchive: Boolean = false
) : Parcelable {
    private constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(error)
        parcel.writeByte(if (isLoading) 1.toByte() else 0.toByte())
        parcel.writeByte(if (showArchive) 1.toByte() else 0.toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoListParcelable> {
        override fun createFromParcel(parcel: Parcel): TodoListParcelable {
            return TodoListParcelable(parcel)
        }

        override fun newArray(size: Int): Array<TodoListParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
