//
//  NavigationAssembly.swift
//  TodoApp
//
//  Created by Roman Romanov on 19.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import EasyDi
import Interaction

class NavigationAssembly: Assembly {
    
    lazy var schedulersAssembly: SchedulersAssembly = self.context.assembly()
    lazy var repositoryAssembly: RepositoryAssembly = self.context.assembly()
    
    var mainStore: Store<MainState, MainAction> {
        return define(scope: .lazySingleton, init: MainStore(
            reducer: MainReducer(),
            middleware: [
                ForwardMiddleware(router: self.router, schedulers: self.schedulersAssembly.schedulers),
                ReplaceMiddleware(router: self.router, schedulers: self.schedulersAssembly.schedulers),
                BackMiddleware(router: self.router, schedulers: self.schedulersAssembly.schedulers)
            ],
            initialState: MainStateProvider(restoredState: nil),
            stateSubjectProvider: DefaultStateSubjectProvider<MainState>(),
            restoredState: nil
        ))
    }
    
    private var todoListStore: Store<TodoState, TodoAction> {
        return define(init: TodoStore(
            reducer: TodoReducer(),
            middleware: [
                TaskLoadMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers),
                TaskSwitchMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers),
                TaskArchiveMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers),
                TaskUnarchiveMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers),
                CreateTaskMiddleware(mainStore: self.mainStore),
                ChangedTaskMiddleware(taskRepository: self.repositoryAssembly.taskRepository)
            ],
            initialState: TodoStateProvider(restoredState: nil),
            stateSubjectProvider: DefaultStateSubjectProvider<TodoState>()
        ))
    }
    
    private var createTaskStore: Store<CreateState, CreateAction> {
        return define(init: CreateStore(
            reducer: CreateReducer(),
            middleware: [
                TaskCreateMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers)
            ],
            initialState: CreateStateProvider(restoredState: nil),
            stateSubjectProvider: DefaultStateSubjectProvider<CreateState>()
        ))
    }
    
    var navigator: Navigator {
        return define(
            scope: .lazySingleton,
            init: BasicNavigator(
                factoryMap: [
                    Screen.todoList: TodoListStoreFactory(
                        get: { return self.todoListStore },
                        recreate: { _ in return self.todoListStore}
                    ),
                    Screen.createTask: CreateTaskStoreFactory(
                        get: { return self.createTaskStore },
                        recreate: { _ in return self.createTaskStore}
                    )
                ],
                schedulers: self.schedulersAssembly.schedulers
            )
        )
    }
    
    var router: Router {
        return define(
            scope: .lazySingleton,
            init: CommonRouter(navigator: self.navigator)
        )
    }
}
