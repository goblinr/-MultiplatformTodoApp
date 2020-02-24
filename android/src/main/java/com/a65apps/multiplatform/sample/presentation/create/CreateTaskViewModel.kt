package com.a65apps.multiplatform.sample.presentation.create

import android.os.Parcel
import android.os.Parcelable
import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.interaction.Action
import com.a65apps.multiplatform.interaction.create.CreateAction
import com.a65apps.multiplatform.interaction.create.CreateState
import com.a65apps.multiplatform.interaction.create.FieldType
import com.a65apps.multiplatform.sample.presentation.ViewState
import com.a65apps.multiplatform.sample.presentation.todo.TaskStatusView
import com.a65apps.multiplatform.sample.presentation.todo.TaskViewState
import com.a65apps.multiplatform.sample.presentation.todo.toInteraction
import com.a65apps.multiplatform.sample.presentation.todo.toViewState

data class CreateViewState(
    val isLoading: Boolean = false,
    val error: String = "",
    val title: String = "",
    val description: String = "",
    val result: TaskViewState? = null,
    val onTitleChanged: (String) -> Unit = {},
    val onDescriptionChanged: (String) -> Unit = {},
    val onSubmitClicked: () -> Unit = {}
) : ViewState

fun CreateParcelable.toInteraction() =
    CreateState(
        isLoading = isLoading,
        error = error,
        title = title,
        description = description,
        result = result?.toInteraction()
    )

fun Task.toParcelable() =
    TaskViewState(
        id = id,
        title = title,
        description = description,
        status = TaskStatusView.valueOf(status.name)
    )

fun CreateState.toPresentation(actions: (Action) -> Unit) = CreateViewState(
    isLoading = isLoading,
    error = error,
    title = title,
    description = description,
    result = result?.toViewState(),
    onTitleChanged = {
        actions(
            CreateAction.UpdateField(
                value = it,
                type = FieldType.TITLE
            )
        )
    },
    onDescriptionChanged = {
        actions(
            CreateAction.UpdateField(
                value = it,
                type = FieldType.DESCRIPTION
            )
        )
    },
    onSubmitClicked = {
        actions(CreateAction.Create)
    }
)

data class CreateParcelable(
    val isLoading: Boolean = false,
    val error: String = "",
    val title: String = "",
    val description: String = "",
    val result: TaskViewState? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(TaskViewState::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isLoading) 1 else 0)
        parcel.writeString(error)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeParcelable(result, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreateParcelable> {
        override fun createFromParcel(parcel: Parcel): CreateParcelable {
            return CreateParcelable(parcel)
        }

        override fun newArray(size: Int): Array<CreateParcelable?> {
            return arrayOfNulls(size)
        }
    }
}
