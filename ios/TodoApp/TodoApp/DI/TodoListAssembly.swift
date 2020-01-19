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
        return define(init: TodoStore(
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
    
    var todoListModel: TodoListModel {
        return define(init: TodoListModel()) { model in
            model.container = IosContainer<TodoState, TodoAction>(schedulers: self.schedulersAssembly.schedulers) { state in
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
            return model
        }
    }
}
