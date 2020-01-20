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
    
    var container: IosContainer<TodoState, TodoAction>?
    
    @Published var isLoading = false
    @Published var error = ""
    @Published var tasks: Array<TaskPresentable> = []
    @Published var showArchive = false
    
    func acceptAction(action: TodoAction) {
        container?.acceptAction(action: action)
    }
    
    func onAppear(store: Store<TodoState, TodoAction>) {
        container?.onAppear(store: store)
    }

    func onDisappear() {
        container?.onDisappear()
    }
}

final class TodoListFactory {
    
    static func create(schedulers: Schedulers, model: TodoListModel) -> IosContainer<TodoState, TodoAction> {
        return IosContainer<TodoState, TodoAction>(schedulers: schedulers) { state in
            print(state)
            model.isLoading = state.isLoading
            model.error = state.error
            model.tasks = state.todoList.map {
                TaskPresentable(
                    id: $0.id,
                    title: $0.title,
                    description: $0.component3(),
                    status: $0.status
                )
            }
            model.showArchive = state.showArchive
        }
    }
}
