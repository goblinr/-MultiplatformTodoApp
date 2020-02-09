package com.a65apps.multiplatform.sample.presentation.todo

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.todo.TodoAction
import com.a65apps.multiplatform.interaction.todo.TodoState
import com.a65apps.multiplatform.sample.R
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.presentation.base.BaseFragment
import com.a65apps.multiplatform.sample.presentation.base.BaseViewModel
import javax.inject.Inject
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_todo_list.archiveBtn
import kotlinx.android.synthetic.main.fragment_todo_list.createBtn
import kotlinx.android.synthetic.main.fragment_todo_list.errorTxt
import kotlinx.android.synthetic.main.fragment_todo_list.root
import kotlinx.android.synthetic.main.fragment_todo_list.todoListRv
import kotlinx.android.synthetic.main.fragment_todo_list.updateStr

class TodoListViewModel @Inject constructor(
    store: Store<TodoState, TodoAction>,
    factory: Map<Class<*>, @JvmSuppressWildcards ComponentBuilder>
) : BaseViewModel<TodoState, TodoAction>(store, factory)

@Parcelize
data class TodoParcelable(
    val isLoading: Boolean,
    val error: String,
    val showArchive: Boolean
) : Parcelable

class TodoListFragment : BaseFragment<TodoState, TodoAction, TodoParcelable, TodoListViewModel>() {

    override val viewModelClass: Class<TodoListViewModel>
        get() = TodoListViewModel::class.java

    private var todoAdapter: TodoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoAdapter = TodoListAdapter {
            viewModel.acceptAction(TodoAction.Switch(it))
        }

        with(todoListRv) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            adapter = todoAdapter
        }

        updateStr.setOnRefreshListener {
            viewModel.acceptAction(TodoAction.Load)
        }

        createBtn.setOnClickListener {
            viewModel.acceptAction(TodoAction.CreateTask)
        }
        archiveBtn.setOnClickListener {
            viewModel.acceptAction(TodoAction.Archive)
        }
    }

    override fun onDestroyView() {
        todoAdapter = null
        super.onDestroyView()
    }

    override fun render(state: TodoState) {
        todoAdapter?.submitList(state.todoList)

        val transition = AutoTransition()
        transition.excludeChildren(R.id.updateStr, true)
        transition.excludeTarget(R.id.updateStr, true)
        TransitionManager.beginDelayedTransition(root, transition)
        updateStr.isRefreshing = state.isLoading
        val hasError = state.error.isNotEmpty()
        errorTxt.isVisible = hasError
        createBtn.isVisible = !hasError
        todoListRv.isVisible = !hasError
        errorTxt.text = state.error
        archiveBtn.isVisible = state.showArchive
    }

    override fun TodoParcelable.toDomain(): TodoState =
        TodoState(
            isLoading = isLoading,
            error = error,
            showArchive = showArchive
        )

    override fun TodoState.toParcelable(): TodoParcelable =
        TodoParcelable(
            isLoading = isLoading,
            error = error,
            showArchive = showArchive
        )

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_archive -> {
                viewModel.acceptAction(TodoAction.GoToArchive)
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
