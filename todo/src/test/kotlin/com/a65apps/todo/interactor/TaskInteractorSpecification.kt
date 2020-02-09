package com.a65apps.todo.interactor

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.todo.entity.ErrorResponse
import com.a65apps.todo.entity.TaskPost
import com.a65apps.todo.reporitory.TaskRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import org.springframework.test.util.AssertionErrors.assertEquals

class TaskInteractorSpecification {

    private lateinit var taskRepository: TaskRepository
    private lateinit var interactor: TaskInteractor

    @BeforeEach
    fun setUp() {
        taskRepository = mock(TaskRepository::class.java)
        interactor = TaskInteractor(taskRepository)
    }

    @Test
    fun `it should return all tasks except with status ARCHIVED`() {
        val tasks = listOf(
            Task("1", "title1", "description1", TaskStatus.PENDING),
            Task("2", "title2", "description2", TaskStatus.DONE),
            Task("3", "title3", "description3", TaskStatus.ARCHIVED)
        )
        val expected = listOf(
            Task("1", "title1", "description1", TaskStatus.PENDING),
            Task("2", "title2", "description2", TaskStatus.DONE)
        )
        `when`(taskRepository.tasks()).thenReturn(tasks)

        val result = interactor.tasks()

        assertEquals(
            "result is different, expected: $expected actual: ${result.body}",
            expected,
            result.body
        )
        assertEquals(
            "status code is not OK",
            HttpStatus.OK,
            result.statusCode
        )
    }

    @Test
    fun `it should return bad request when task creation title is empty`() {
        val expected = ErrorResponse("Title should't be empty")
        val result = interactor.createTask(TaskPost(
            title = "",
            description = ""
        ))

        assertEquals(
            "result is different, expected: $expected actual: ${result.body}",
            expected,
            result.body
        )
        assertEquals(
            "status code is not BAD_REQUEST",
            HttpStatus.BAD_REQUEST,
            result.statusCode
        )
    }

    @Test
    fun `it should return bad request when task creation description is empty`() {
        val expected = ErrorResponse("Description should't be empty")
        val result = interactor.createTask(TaskPost(
            title = "title",
            description = ""
        ))

        assertEquals(
            "result is different, expected: $expected actual: ${result.body}",
            expected,
            result.body
        )
        assertEquals(
            "status code is not BAD_REQUEST",
            HttpStatus.BAD_REQUEST,
            result.statusCode
        )
    }

    @Test
    fun `it should return created task when creation successful`() {
        val post = TaskPost(
            title = "title",
            description = "description"
        )
        val expected = Task("1", "title1", "description1", TaskStatus.PENDING)
        `when`(taskRepository.save(post)).thenReturn(expected)

        val result = interactor.createTask(post)

        assertEquals(
            "result is different, expected: $expected actual: ${result.body}",
            expected,
            result.body
        )
        assertEquals(
            "status code is not OK",
            HttpStatus.OK,
            result.statusCode
        )
    }

    @Test
    fun `it should switch task status to DONE when it PENDING and switchTask called`() {
        val id = "id"
        val before = Task(id, "title", "description", TaskStatus.PENDING)
        val expected = Task(id, "title", "description", TaskStatus.DONE)
        `when`(taskRepository.getTask(id)).thenReturn(before)
        `when`(taskRepository.save(expected)).thenReturn(expected)

        val result = interactor.switchTask(id)

        assertEquals(
            "result is different, expected: $expected actual: ${result.body}",
            expected,
            result.body
        )
        assertEquals(
            "status code is not OK",
            HttpStatus.OK,
            result.statusCode
        )
    }

    @Test
    fun `it should switch task status to PENDING when it DONE and switchTask called`() {
        val id = "id"
        val before = Task(id, "title", "description", TaskStatus.DONE)
        val expected = Task(id, "title", "description", TaskStatus.PENDING)
        `when`(taskRepository.getTask(id)).thenReturn(before)
        `when`(taskRepository.save(expected)).thenReturn(expected)

        val result = interactor.switchTask(id)

        assertEquals(
            "result is different, expected: $expected actual: ${result.body}",
            expected,
            result.body
        )
        assertEquals(
            "status code is not OK",
            HttpStatus.OK,
            result.statusCode
        )
    }

    @Test
    fun `it should return not found error when switched task is not found`() {
        val id = "id"
        val expected = ErrorResponse("Not found")
        `when`(taskRepository.getTask(id)).thenReturn(null)

        val result = interactor.switchTask(id)

        assertEquals(
            "result is different, expected: $expected actual: ${result.body}",
            expected,
            result.body
        )
        assertEquals(
            "status code is not NOT_FOUND",
            HttpStatus.NOT_FOUND,
            result.statusCode
        )
    }

    @Test
    fun `it should return all tasks with status ARCHIVED`() {
        val tasks = listOf(
            Task("1", "title1", "description1", TaskStatus.PENDING),
            Task("2", "title2", "description2", TaskStatus.DONE),
            Task("3", "title3", "description3", TaskStatus.ARCHIVED),
            Task("4", "title4", "description4", TaskStatus.ARCHIVED)
        )
        val expected = listOf(
            Task("3", "title3", "description3", TaskStatus.ARCHIVED),
            Task("4", "title4", "description4", TaskStatus.ARCHIVED)
        )
        `when`(taskRepository.tasks()).thenReturn(tasks)

        val result = interactor.archivedTasks()

        assertEquals(
            "result is different, expected: $expected actual: ${result.body}",
            expected,
            result.body
        )
        assertEquals(
            "status code is OK",
            HttpStatus.OK,
            result.statusCode
        )
    }
}
