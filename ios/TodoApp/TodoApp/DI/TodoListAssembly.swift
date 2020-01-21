//
//  TodoListAssembly.swift
//  TodoApp
//
//  Created by Roman Romanov on 19.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import EasyDi
import Interaction

class TodoListAssembly: Assembly {
    
    lazy var schedulersAssembly: SchedulersAssembly = self.context.assembly()
    lazy var mainAssembly: MainAssemly = self.context.assembly()
    lazy var repositoryAssembly: RepositoryAssembly = self.context.assembly()
    lazy var navigationAssembly: NavigationAssembly = self.context.assembly()
    
    var todoListStore: Store<TodoState, TodoAction> {
        return define(scope: .weakSingleton, init: TodoStore(
            reducer: TodoReducer(),
            middleware: [
                TaskLoadMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers),
                TaskSwitchMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers),
                TaskArchiveMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers),
                TaskUnarchiveMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers),
                CreateTaskMiddleware(mainStore: self.mainAssembly.mainStore),
                ChangedTaskMiddleware(taskRepository: self.repositoryAssembly.taskRepository)
            ],
            initialState: TodoStateProvider(restoredState: nil),
            stateSubjectProvider: DefaultStateSubjectProvider<TodoState>()
        ))
    }
    
    var todoListModel: TodoListViewModel {
        return define(scope: .weakSingleton, init: TodoListViewModel()) {
            print("Inject TodoListViewModel")
            $0.container = TodoListFactory.create(schedulers: self.schedulersAssembly.schedulers, model: $0)
            return $0
        }
    }
}
