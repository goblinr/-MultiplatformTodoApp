//
//  MainModel.swift
//  TodoApp
//
//  Created by Roman Romanov on 11.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

final class MainModel : ObservableObject {

    @Environment(\.schedulers) var schedulers: Schedulers
    private var container: MainContainer? = nil

    @Published public var state: MainState? = nil

    func onAppear(router: Router) {
        container?.dispose()
        
        container = MainContainer(
        router: router, schedulers: self.schedulers) { newState in
            self.state = newState
            print(newState)
        }
    }

    func onDisappear() {
        container?.dispose()
        container = nil
    }
    
    func getContainer() -> MainContainer {
        return container!
    }
}
