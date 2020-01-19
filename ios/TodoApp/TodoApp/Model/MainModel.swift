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

    var schedulers: Schedulers?
    var router: SwiftRouter?
    var container: IosContainer<MainState, MainAction>?

    @Published public var state: MainState? = nil

    func onAppear(navigator: Navigator, store: Store<MainState, MainAction>) {
        router?.attachNavigator(navigator: navigator)
        container?.onAppear(store: store)
    }

    func onDisappear() {
        router?.detachNavigator()
        container?.onDisappear()
    }
}
