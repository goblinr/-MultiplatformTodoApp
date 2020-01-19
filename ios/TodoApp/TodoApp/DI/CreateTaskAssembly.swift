//
//  CreateTaskAssembly.swift
//  TodoApp
//
//  Created by Roman Romanov on 19.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import EasyDi
import Interaction

class CreateTaskAssembly: Assembly {
    
    lazy var schedulersAssembly: SchedulersAssembly = self.context.assembly()
    lazy var repositoryAssembly: RepositoryAssembly = self.context.assembly()
    lazy var mainAssembly: MainAssemly = self.context.assembly()
    
    var createTaskStore: Store<CreateState, CreateAction> {
        return define(init: CreateStore(
            reducer: CreateReducer(),
            middleware: [
                TaskCreateMiddleware(taskRepository: self.repositoryAssembly.taskRepository, schedulers: self.schedulersAssembly.schedulers)
            ],
            initialState: CreateStateProvider(restoredState: nil),
            stateSubjectProvider: DefaultStateSubjectProvider<CreateState>()
        ))
    }
    
    var createTaskModel: CreateTaskModel {
        return define(init: CreateTaskModel()) { model in
            model.container = IosContainer<CreateState, CreateAction>(schedulers: self.schedulersAssembly.schedulers) { state in
                print(state)
                model.isLoading = state.isLoading
                model.error = state.error
                model.title = state.title
                model.description = state.component4()
                model.result = state.result
            }
            return model
        }
    }
    
    var view: CreateTaskView {
        return define(init: CreateTaskView(model: self.createTaskModel, mainStore: self.mainAssembly.mainStore))
    }
}
