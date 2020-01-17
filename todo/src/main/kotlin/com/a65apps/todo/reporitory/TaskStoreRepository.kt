package com.a65apps.todo.reporitory

import com.a65apps.multiplatform.domain.Task
import com.a65apps.todo.entity.TaskPost
import com.a65apps.todo.entity.toDomain
import com.a65apps.todo.entity.toEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class TaskStoreRepository @Autowired constructor(
    private val jpaRepository: TaskJpaRepository
) : TaskRepository {

    override fun tasks(): List<Task> = jpaRepository
        .findAll(Sort.by(Sort.Direction.DESC, "creationDateTime"))
        .map { it.toDomain() }

    override fun save(task: TaskPost): Task =
        jpaRepository.save(task.toEntity()).toDomain()

    override fun save(task: Task): Task =
        jpaRepository.save(task.toEntity(jpaRepository)).toDomain()

    override fun save(tasks: List<Task>) {
        jpaRepository.saveAll(tasks.map { it.toEntity(jpaRepository) })
    }

    override fun getTask(id: String): Task? = jpaRepository.findById(id).takeIf {
        it.isPresent
    }?.get()?.toDomain()
}
