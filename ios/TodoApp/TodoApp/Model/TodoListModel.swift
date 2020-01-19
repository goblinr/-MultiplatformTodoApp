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
