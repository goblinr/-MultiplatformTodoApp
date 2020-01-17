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
    
    @ObservedObject var router: SwiftUIRouter = SwiftUIRouter()
    @ObservedObject var model = MainModel()
    
    var body: some View {
        NavigationView {
            VStack {
                NavigationLink(destination: TodoListView(mainStore: { self.model.getContainer().store }, router: router), tag: Screen.todoList, selection: $router.screen) {
                    EmptyView()
                }.hidden()
            }
        }.onAppear() {
            self.model.onAppear(router: self.router.self)
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

#if DEBUG
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .environment(\.taskRepository, MockTaskRepository())
            .environment(\.schedulers, IosSchedulers())
    }
}
#endif
