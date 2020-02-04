//
//  MainModel.swift
//  TodoApp
//
//  Created by Roman Romanov on 11.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

final class MainViewModel : ObservableObject {

    var schedulers: Schedulers?
    var router: SwiftRouter?
    var container: Container<MainState, MainAction>?

    var screen: Screen? = nil
    var backstack: Array<Screen>? = nil

    func onAppear(navigator: Navigator, store: Store<MainState, MainAction>) {
        router?.attachNavigator(navigator: navigator)
        container?.onAppear(store: store)
    }

    func onDisappear() {
        router?.detachNavigator()
        container?.onDisappear()
    }
}

final class MainContainerFactory {
    
    static func create(schedulers: Schedulers, model: MainViewModel) -> Container<MainState, MainAction> {
        return Container<MainState, MainAction>(schedulers) { state in
            print(state)
            model.screen = state.screen
            model.backstack = state.backStack
            model.objectWillChange.send()
        }
    }
}
