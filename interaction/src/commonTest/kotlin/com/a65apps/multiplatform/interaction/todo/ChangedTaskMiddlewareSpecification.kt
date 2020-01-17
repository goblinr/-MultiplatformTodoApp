package com.a65apps.multiplatform.interaction.todo

import com.a65apps.multiplatform.interaction.Middleware
import com.a65apps.multiplatform.interaction.base.BaseMiddlewareSpecification
import com.a65apps.multiplatform.interaction.data.TaskRepository
import com.a65apps.multiplatform.interaction.mock.mockTaskRepository
import com.a65apps.multiplatform.interaction.mock.stubbing
import com.a65apps.multiplatform.interaction.mock.verifyChanged
import com.badoo.reaktive.observable.observableOfEmpty
import com.badoo.reaktive.subject.publish.PublishSubject
import com.badoo.reaktive.test.observable.assertNoValues
import com.badoo.reaktive.test.observable.assertValue
import kotlin.test.Test

class ChangedTaskMiddlewareSpecification : BaseMiddlewareSpecification<TodoAction, TodoState>() {

    lateinit var repository: TaskRepository

    override val defaultState: TodoState = TodoState()

    override fun initMocks() {
        repository = mockTaskRepository()
    }

    override fun createMiddleware(): Middleware<TodoAction, TodoState> =
        ChangedTaskMiddleware(repository)

    @Test
    fun `it should emit load action when repository changed`() {
        val changed = PublishSubject<Unit>()
        repository.stubbing {
            stubChanged(changed)
        }
        val observer = bind()

        changed.onNext(Unit)

        observer.assertValue(TodoAction.Load)
        repository.verifyChanged()
    }

    @Test
    fun `it should't accept any action`() {
        repository.stubbing {
            stubChanged(observableOfEmpty())
        }
        val observer = bind()

        acceptAction(TodoAction.Idle)
        acceptAction(TodoAction.Load)
        acceptAction(TodoAction.Archive)
        acceptAction(TodoAction.CreateTask)
        acceptAction(TodoAction.Switch("id"))
        acceptAction(TodoAction.Error("error"))
        acceptAction(TodoAction.Loaded(listOf()))
        acceptAction(TodoAction.Unarchive("id"))

        observer.assertNoValues()
        repository.verifyChanged()
    }
}
