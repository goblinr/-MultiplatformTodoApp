package com.a65apps.multiplatform.domain

/**
 * Структура данных задачи
 *
 * @property id - уникальный идентификатор задачи
 * @property title - заголовок задачи
 * @property description - подробное описание задачи
 * @property status - текущее состояние задачи [TaskStatus]
 */
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus
)
