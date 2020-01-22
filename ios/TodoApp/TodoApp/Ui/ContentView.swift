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
                if model.screen == Screen.todoList {
                    TodoListView(
                        model: TodoListAssembly.instance(from: context).todoListModel,
                        context: context,
                        inputData: navigator.lastData()
                    )
                } else if model.screen == Screen.createTask {
                    CreateTaskView(
                        model: CreateTaskAssembly.instance(from: context).createTaskModel,
                        context: context,
                        inputData: navigator.lastData()
                    )
                }
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
    static var context: DIContext {
        let context = DIContext()
        RepositoryAssembly.instance(from: context)
            .addSubstitution(for: "taskRepository") { () -> TaskRepository in
                return MockTaskRepository()
            }
        
        return context
    }
    static var listState: MainViewModel {
        let model = MainViewModel()
        model.screen = Screen.todoList
        model.backstack = []
        model.container = MainContainerFactory.create(schedulers: IosSchedulers(), model: model)
        return model
    }
    
    static var previews: some View {
        Group {
            ContentView(
                navigator: Navigator(),
                model: listState,
                context: context
            ).previewDisplayName("Light")
            
            ContentView(
                navigator: Navigator(),
                model: listState,
                context: context
            ).environment(\.colorScheme, .dark)
            .previewDisplayName("Dark")
        }
    }
}
#endif
