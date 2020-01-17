package com.a65apps.multiplatform.sample.presentation.create

import android.os.Bundle
import android.os.Parcelable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.create.CreateAction
import com.a65apps.multiplatform.interaction.create.CreateState
import com.a65apps.multiplatform.interaction.create.FieldType
import com.a65apps.multiplatform.sample.R
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.presentation.base.BaseFragment
import com.a65apps.multiplatform.sample.presentation.base.BaseViewModel
import javax.inject.Inject
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_create.*

class CreateViewModel @Inject constructor(
    store: Store<CreateState, CreateAction>,
    factory: Map<Class<*>, @JvmSuppressWildcards ComponentBuilder>
) : BaseViewModel<CreateState, CreateAction>(store, factory)

@Parcelize
data class CreateParcelable(
    val isLoading: Boolean = false,
    val error: String = "",
    val title: String = "",
    val description: String = "",
    val result: TaskParcelable? = null
) : Parcelable

@Parcelize
data class TaskParcelable(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus
) : Parcelable

fun TaskParcelable.toDomain(): Task =
    Task(
        id = id,
        title = title,
        description = description,
        status = status
    )

fun Task.toParcelable(): TaskParcelable =
    TaskParcelable(
        id = id,
        title = title,
        description = description,
        status = status
    )

class CreateFragment : BaseFragment<CreateState, CreateAction, CreateParcelable,
        CreateViewModel>() {

    private var titleWatcher: TextWatcher? = null
    private var descriptionWatcher: TextWatcher? = null

    override val viewModelClass: Class<CreateViewModel>
        get() = CreateViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleWatcher = titleEdit.addTextChangedListener {
            viewModel.acceptAction(
                CreateAction.UpdateField(
                    value = it?.toString() ?: "",
                    type = FieldType.TITLE
                )
            )
        }
        descriptionWatcher = descriptionEdit.addTextChangedListener {
            viewModel.acceptAction(
                CreateAction.UpdateField(
                    value = it?.toString() ?: "",
                    type = FieldType.DESCRIPTION
                )
            )
        }

        submitButton.setOnClickListener {
            viewModel.acceptAction(CreateAction.Create)
        }
    }

    override fun onDestroyView() {
        titleWatcher = null
        descriptionWatcher = null

        super.onDestroyView()
    }

    override fun render(state: CreateState) {
        val result = state.result
        val hasResult = result != null
        loadingTxt.isVisible = state.isLoading
        submitButton.isEnabled = !state.isLoading
        submitButton.isVisible = !hasResult
        titleLayout.isVisible = !hasResult
        descriptionLayout.isVisible = !hasResult
        titleLayout.isEnabled = !state.isLoading
        descriptionLayout.isEnabled = !state.isLoading
        errorTxt.isVisible = state.error.isNotEmpty()
        errorTxt.text = state.error
        resultContainer.isVisible = hasResult
        if (result != null) {
            titleTxt.text = result.title
            descriptionTxt.text = result.description
        }
    }

    override fun CreateParcelable.toDomain(): CreateState =
        CreateState(
            isLoading = isLoading,
            error = error,
            title = title,
            description = description,
            result = result?.toDomain()
        )

    override fun CreateState.toParcelable(): CreateParcelable =
        CreateParcelable(
            isLoading = isLoading,
            error = error,
            title = title,
            description = description,
            result = result?.toParcelable()
        )
}
