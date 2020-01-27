package com.a65apps.multiplatform.sample.presentation.archive

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.archive.ArchiveAction
import com.a65apps.multiplatform.interaction.archive.ArchiveState
import com.a65apps.multiplatform.sample.R
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.presentation.base.BaseFragment
import com.a65apps.multiplatform.sample.presentation.base.BaseViewModel
import javax.inject.Inject
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_archive_list.archiveListRv
import kotlinx.android.synthetic.main.fragment_archive_list.errorTxt
import kotlinx.android.synthetic.main.fragment_archive_list.root
import kotlinx.android.synthetic.main.fragment_archive_list.updateStr

class ArchiveListViewModel @Inject constructor(
    store: Store<ArchiveState, ArchiveAction>,
    factory: Map<Class<*>, @JvmSuppressWildcards ComponentBuilder>
) : BaseViewModel<ArchiveState, ArchiveAction>(store, factory)

@Parcelize
data class ArchiveParcelable(
    val isLoading: Boolean,
    val error: String
) : Parcelable

class ArchiveListFragment : BaseFragment<ArchiveState, ArchiveAction, ArchiveParcelable,
        ArchiveListViewModel>() {

    override val viewModelClass: Class<ArchiveListViewModel>
        get() = ArchiveListViewModel::class.java

    private var archiveAdapter: ArchiveListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_archive_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        archiveAdapter = ArchiveListAdapter {
            viewModel.acceptAction(ArchiveAction.UnarchiveTask(it))
        }

        val touchCallback = ArchiveItemTouchHelper(archiveAdapter!!) { task ->
            viewModel.acceptAction(ArchiveAction.UnarchiveTask(task.id))
        }
        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(archiveListRv)

        with(archiveListRv) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
            adapter = archiveAdapter
        }

        updateStr.setOnRefreshListener {
            viewModel.acceptAction(ArchiveAction.Load)
        }
    }

    override fun render(state: ArchiveState) {
        archiveAdapter?.submitList(state.archiveTodoList)

        val transition = AutoTransition()
        transition.excludeChildren(R.id.updateStr, true)
        transition.excludeTarget(R.id.updateStr, true)
        TransitionManager.beginDelayedTransition(root, transition)
        updateStr.isRefreshing = state.isLoading
        val hasError = state.error.isNotEmpty()
        errorTxt.isVisible = hasError
        archiveListRv.isVisible = !hasError
        errorTxt.text = state.error
    }

    override fun ArchiveParcelable.toDomain(): ArchiveState =
        ArchiveState(
            isLoading = isLoading,
            error = error
        )

    override fun ArchiveState.toParcelable(): ArchiveParcelable =
        ArchiveParcelable(
            isLoading = isLoading,
            error = error
        )
}
