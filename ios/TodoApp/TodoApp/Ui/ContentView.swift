//
//  ContentView.swift
//  TodoApp
//
//  Created by Roman Romanov on 09.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction
import EasyDi

struct ContentView: View {
    
    @ObservedObject var navigator: Navigator
    @ObservedObject var model: MainViewModel
    var context: DIContext
    
    var body: some View {
        NavigationView {
            VStack {
                NavigationLink(
                    destination: TodoListView(
                        navigator: self.navigator.self,
                        model: TodoListAssembly.instance(from: context).todoListModel,
                        context: self.context
                    ),
                    tag: Screen.todoList,
                    selection: $navigator.screen
                ) {
                    EmptyView()
                }.hidden()
            }
        }.onAppear() {
            self.model.onAppear(
                navigator: self.navigator.self,
                store: MainAssemly.instance(from: self.context).mainStore
            )
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

#if DEBUG
struct ContentView_Previews: PreviewProvider {
    
    static let defaultState = MainViewModel()
    static var context: DIContext {
        let context = DIContext()
        RepositoryAssembly.instance(from: context)
            .addSubstitution(for: "taskRepository") { () -> TaskRepository in
                return MockTaskRepository()
            }
        
        return context
    }
    static var defaultNavigation: Navigator {
        let nav = Navigator()
        nav.screen = Screen.todoList
        return nav
    }
    
    static var previews: some View {
        Group {
            ContentView(
                navigator: defaultNavigation,
                model: defaultState,
                context: context
            )
            ContentView(
                navigator: defaultNavigation,
                model: defaultState,
                context: context
            ).environment(\.colorScheme, .dark)
        }
    }
}
#endif
