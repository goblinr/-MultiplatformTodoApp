//
//  TodoListModel.swift
//  TodoApp
//
//  Created by Roman Romanov on 13.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

struct TodoListViewModel : ViewState {
    let isLoading: Bool
    let error: String
    let tasks: Array<TaskPresentable>
    let showArchive: Bool
    let onSwitchTask: (String) -> Void
    let onCreateTask: () -> Void
    let onArchiveTasks: () -> Void
    let onUnarchiveTasks: () -> Void
    let onRefresh: () -> Void
    
    init(
        isLoading: Bool = false,
        error: String = "",
        tasks: Array<TaskPresentable> = [],
        showArchive: Bool = false,
        onSwitchTask: @escaping (String) -> Void = {_ in },
        onCreateTask: @escaping () -> Void = {},
        onArchiveTasks: @escaping () -> Void = {},
        onUnarchiveTasks: @escaping () -> Void = {},
        onRefresh: @escaping () -> Void = {}
    ) {
        self.isLoading = isLoading
        self.error = error
        self.tasks = tasks
        self.showArchive = showArchive
        self.onSwitchTask = onSwitchTask
        self.onCreateTask = onCreateTask
        self.onArchiveTasks = onArchiveTasks
        self.onUnarchiveTasks = onUnarchiveTasks
        self.onRefresh = onRefresh
    }
}

extension TodoState {
    func toViewModel(actions: @escaping (Action) -> KotlinUnit) -> TodoListViewModel {
        return TodoListViewModel(
            isLoading: isLoading,
            error: error,
            tasks: todoList.map {
                TaskPresentable(
                    id: $0.id,
                    title: $0.title,
                    description: $0.component3(),
                    status: $0.status
                )
            },
            showArchive: showArchive,
            onSwitchTask: { id in
                _ = actions(TodoAction.Switch(id: id))
            },
            onCreateTask: {
                _ = actions(TodoAction.CreateTask())
            },
            onArchiveTasks: {
                _ = actions(TodoAction.Archive())
            },
            onUnarchiveTasks: {
                _ = actions(TodoAction.GoToArchive())
            },
            onRefresh: {
                _ = actions(TodoAction.Load())
            }
        )
    }
}
