//
//  CreateTaskModel.swift
//  TodoApp
//
//  Created by Roman Romanov on 14.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

final class CreateTaskViewModel : ObservableObject {
    
    var container: Container<CreateState, CreateAction>?
    var mainStore: Store<MainState, MainAction>?
    
    var isLoading = false
    var error = ""
    var title = ""
    var description = ""
    var result: Task? = nil
    
    func acceptAction(action: CreateAction) {
        container?.acceptAction(action: action)
    }
    
    func onAppear(store: Store<CreateState, CreateAction>) {
        container?.onAppear(store: store)
    }

    func onDisappear() {
        container?.onDisappear()
    }
    
    func navigateBack() {
        mainStore?.acceptAction(action: MainAction.NavigateBack())
    }
}

final class CreateTaskFactory {
    
    static func create(schedulers: Schedulers, model: CreateTaskViewModel) -> Container<CreateState, CreateAction> {
        return Container<CreateState, CreateAction>(schedulers) { state in
            print(state)
            model.isLoading = state.isLoading
            model.error = state.error
            model.title = state.title
            model.description = state.component4()
            model.result = state.result
            model.objectWillChange.send()
        }
    }
}
