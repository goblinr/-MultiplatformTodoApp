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
    
    var container: IosContainer<CreateState, CreateAction>?
    
    @Published var isLoading = false
    @Published var error = ""
    @Published var title = ""
    @Published var description = ""
    @Published var result: Task? = nil
    
    func acceptAction(action: CreateAction) {
        container?.acceptAction(action: action)
    }
    
    func onAppear(store: Store<CreateState, CreateAction>) {
        container?.onAppear(store: store)
    }

    func onDisappear() {
        container?.onDisappear()
    }
}
