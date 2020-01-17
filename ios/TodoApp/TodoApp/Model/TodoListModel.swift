//
//  TodoListModel.swift
//  TodoApp
//
//  Created by Roman Romanov on 13.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

final class TodoListModel : ObservableObject {
    
    @Environment(\.schedulers) var schedulers: Schedulers
    @Environment(\.taskRepository) var taskRepository: TaskRepository
    private var container: TodoListContainer? = nil
    
    @Published var isLoading = false
    @Published var error = ""
    @Published var tasks: Array<TaskPresentable> = []
    @Published var showArchive = false
    
    func onAppear(
        mainStore: MainStore
    ) {
        container?.dispose()
        container = TodoListContainer(
            mainStore: mainStore,
            taskRepository: taskRepository,
            schedulers: schedulers
        ) { state in
            self.isLoading = state.isLoading
            self.error = state.error
            self.tasks = state.todoList.map {
                TaskPresentable(
                    id: $0.id,
                    title: $0.title,
                    description: $0.component3(),
                    status: $0.status
                )
            }
            self.showArchive = state.showArchive
        }
    }
    
    func onDisappear() {
        container?.dispose()
        container = nil
    }
    
    func acceptAction(action: TodoAction) {
        container?.acceptAction(action: action)
    }
}
