//
//  CreateTaskModel.swift
//  TodoApp
//
//  Created by Roman Romanov on 14.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

final class CreateTaskModel : ObservableObject {
    
    @Environment(\.schedulers) var schedulers: Schedulers
    @Environment(\.taskRepository) var taskRepository: TaskRepository
    private var container: CreateTaskContainer? = nil
    
    @Published var isLoading = false
    @Published var error = ""
    @Published var title = ""
    @Published var description = ""
    @Published var result: Task? = nil
    
    func onAppear() {
        container?.dispose()
        container = CreateTaskContainer(
            taskRepository: taskRepository,
            schedulers: schedulers
        ) { state in
            self.isLoading = state.isLoading
            self.error = state.error
            self.title = state.title
            self.description = state.component4()
            self.result = state.result
        }
    }
    
    func onDisappear() {
        container?.dispose()
        container = nil
    }
    
    func acceptAction(action: CreateAction) {
        container?.acceptAction(action: action)
    }
}
