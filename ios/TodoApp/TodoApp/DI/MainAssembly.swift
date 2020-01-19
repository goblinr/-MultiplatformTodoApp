//
//  MainAssembly.swift
//  TodoApp
//
//  Created by Roman Romanov on 19.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import EasyDi
import Interaction

class MainAssemly: Assembly {
    
    lazy var schedulersAssembly: SchedulersAssembly = self.context.assembly()
    lazy var navigationAssembly: NavigationAssembly = self.context.assembly()
    
    var mainStore: Store<MainState, MainAction> {
        return define(scope: .weakSingleton, init: MainStore(
            reducer: MainReducer(),
            middleware: [
                ForwardMiddleware(router: self.navigationAssembly.router, schedulers: self.schedulersAssembly.schedulers),
                ReplaceMiddleware(router: self.navigationAssembly.router, schedulers: self.schedulersAssembly.schedulers),
                BackMiddleware(router: self.navigationAssembly.router, schedulers: self.schedulersAssembly.schedulers)
            ],
            initialState: MainStateProvider(restoredState: nil),
            stateSubjectProvider: DefaultStateSubjectProvider<MainState>(),
            restoredState: nil
        ))
    }
    
    var mainModel: MainModel {
        return define(init: MainModel()) { model in
            model.router = self.navigationAssembly.router
            model.schedulers = self.schedulersAssembly.schedulers
            model.container = IosContainer<MainState, MainAction>(schedulers: self.schedulersAssembly.schedulers) { state in
                print(state)
                model.state = state
            }
            return model
        }
    }
    
    var view: ContentView {
        return define(init: ContentView(navigator:  Navigator(), model: self.mainModel))
    }
}
