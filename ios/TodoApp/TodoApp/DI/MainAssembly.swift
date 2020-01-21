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
    
    var mainModel: MainViewModel {
        return define(scope: .weakSingleton, init: MainViewModel()) {
            print("Inject MainViewModel")
            $0.router = self.navigationAssembly.router
            $0.schedulers = self.schedulersAssembly.schedulers
            $0.container = MainContainerFactory.create(schedulers: self.schedulersAssembly.schedulers, model: $0)
            return $0
        }
    }
    
    var view: ContentView {
        return define(init: ContentView(navigator:  Navigator(), model: self.mainModel, context: self.context))
    }
}
