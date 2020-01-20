//
//  ContentView.swift
//  TodoApp
//
//  Created by Roman Romanov on 09.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

struct ContentView: View {
    
    @ObservedObject var navigator: Navigator
    @ObservedObject var model: MainModel
    
    private let todoListAssembly = TodoListAssembly.instance()
    private let mainAssembly = MainAssemly.instance()
    
    var body: some View {
        NavigationView {
            VStack {
                NavigationLink(
                    destination: TodoListView(navigator: self.navigator.self, model: todoListAssembly.todoListModel),
                    tag: Screen.todoList,
                    selection: $navigator.screen
                ) {
                    EmptyView()
                }.hidden()
            }
        }.onAppear() {
            self.model.onAppear(navigator: self.navigator.self, store: self.mainAssembly.mainStore)
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

#if DEBUG
struct ContentView_Previews: PreviewProvider {
    
    static let defaultState = MainModel()
    static var defaultNavigation: Navigator {
        get {
            let nav = Navigator()
            nav.screen = Screen.todoList
            return nav
        }
    }
    
    static var previews: some View {
        Group {
            ContentView(
                navigator: defaultNavigation,
                model: defaultState
            )
            
            ContentView(
                navigator: defaultNavigation,
                model: defaultState
            ).environment(\.colorScheme, .dark)
        }
    }
}
#endif
