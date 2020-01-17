package com.a65apps.multiplatform.domain

/**
 * Перечисление представляющее собой состояние задачи
 *
 * @property PENDING - задача ожидает выполнения
 * @property DONE - задача выполнена
 * @property ARCHIVED - задача выполнена и заархивирована
 */
enum class TaskStatus {
    PENDING,
    DONE,
    ARCHIVED
}
