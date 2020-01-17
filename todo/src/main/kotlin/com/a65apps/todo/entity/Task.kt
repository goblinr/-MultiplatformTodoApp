package com.a65apps.todo.entity

import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.todo.reporitory.TaskJpaRepository
import java.util.Date
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityNotFoundException
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(name = "tasks")
data class TaskEntity(
    @Id
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    @Temporal(TemporalType.TIMESTAMP)
    val creationDateTime: Date
)

fun TaskEntity.toDomain(): Task =
    Task(
        id = id,
        title = title,
        description = description,
        status = status
    )

fun Task.toEntity(jpaRepository: TaskJpaRepository): TaskEntity =
    jpaRepository.findById(id).map {
        it.copy(
            title = title,
            description = description,
            status = status
        )
    }.orElseThrow { EntityNotFoundException() }

data class TaskPost(
    val title: String,
    val description: String
)

fun TaskPost.toEntity(): TaskEntity =
    TaskEntity(
        id = UUID.randomUUID().toString(),
        title = title,
        description = description,
        status = TaskStatus.PENDING,
        creationDateTime = Date()
    )
