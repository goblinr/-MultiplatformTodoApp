package com.a65apps.todo.reporitory

import com.a65apps.todo.entity.TaskEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TaskJpaRepository : JpaRepository<TaskEntity, String>
