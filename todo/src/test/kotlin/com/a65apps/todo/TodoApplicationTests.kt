package com.a65apps.todo

import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.todo.entity.TaskEntity
import com.a65apps.todo.reporitory.TaskJpaRepository
import java.util.Date
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anything
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
    locations = ["classpath:application-integration.properties"]
)
class TodoApplicationTests {

    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var repository: TaskJpaRepository

    @Test
    fun `context loads`() {
    }

    @Test
    fun `it should return all tasks except with status ARCHIVED sorted by date DESC`() {
        // given
        repository.save(TaskEntity("1", "title1", "description1", TaskStatus.PENDING, Date(1)))
        repository.save(TaskEntity("2", "title2", "description2", TaskStatus.DONE, Date(2)))
        repository.save(TaskEntity("3", "title3", "description3", TaskStatus.ARCHIVED, Date(3)))

        // when
        mvc.perform(get("/tasks"))
            // then
            .andExpect(status().isOk)
            .andExpect(
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            )
            .andExpect(jsonPath("$..id", `is`(listOf("2", "1"))))
            .andExpect(jsonPath("$..title", `is`(listOf("title2", "title1"))))
            .andExpect(jsonPath("$..description", `is`(listOf("description2", "description1"))))
            .andExpect(jsonPath("$..status", `is`(listOf("DONE", "PENDING"))))
    }

    @Test
    fun `it should successfully create task`() {
        // when
        mvc.perform(
            post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"title\", \"description\": \"description\"}")
        ) // then
            .andExpect(status().isOk)
            .andExpect(
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            )
            .andExpect(jsonPath("$.id", anything()))
            .andExpect(jsonPath("$.title", `is`("title")))
            .andExpect(jsonPath("$.description", `is`("description")))
            .andExpect(jsonPath("$.status", `is`("PENDING")))
    }

    @Test
    fun `it should successfully switch task status`() {
        // given
        repository.save(TaskEntity("1", "title1", "description1", TaskStatus.PENDING, Date()))

        // when
        mvc.perform(patch("/tasks/1"))
            // then
            .andExpect(status().isOk)
            .andExpect(
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            )
            .andExpect(jsonPath("$.status", `is`("DONE")))

        // when
        mvc.perform(patch("/tasks/1"))
            // then
            .andExpect(status().isOk)
            .andExpect(
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            )
            .andExpect(jsonPath("$.status", `is`("PENDING")))
    }

    @Test
    fun `it should successfully archive all DONE tasks`() {
        // given
        repository.save(TaskEntity("1", "title1", "description1", TaskStatus.PENDING, Date(1)))
        repository.save(TaskEntity("2", "title2", "description2", TaskStatus.DONE, Date(2)))
        repository.save(TaskEntity("3", "title3", "description3", TaskStatus.DONE, Date(3)))

        // when
        mvc.perform(post("/tasks/archive"))
            .andExpect(status().isOk)

        // then
        val tasks = repository.findAll()
        assertTrue(tasks.contains(TaskEntity("1", "title1", "description1", TaskStatus.PENDING, Date(1))))
        assertTrue(tasks.contains(TaskEntity("2", "title2", "description2", TaskStatus.ARCHIVED, Date(2))))
        assertTrue(tasks.contains(TaskEntity("3", "title3", "description3", TaskStatus.ARCHIVED, Date(3))))
    }
}
