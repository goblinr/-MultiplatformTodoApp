//
//  TodoListView.swift
//  TodoApp
//
//  Created by Roman Romanov on 11.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction
import EasyDi

struct ProgressBar : View {
    @SwiftUI.State private var spinCircle = false

    var body: some View {
        ZStack {
            Circle()
                .trim(from: 0.5, to: 1)
                .stroke(Color.blue, lineWidth:4)
                .frame(width:100)
                .rotationEffect(.degrees(spinCircle ? 0 : -360), anchor: .center)
                .animation(Animation.linear(duration: 1).repeatForever(autoreverses: false))
        }
        .onAppear {
            self.spinCircle = true
        }
    }
}

struct TodoListView: View {
    
    @ObservedObject var navigator: Navigator
    @ObservedObject var model: TodoListViewModel
    var context: DIContext
    
    var body: some View {
        ZStack {
            List(model.tasks) { task in
                TaskView(task: task).onTapGesture {
                    self.model.acceptAction(
                        action: TodoAction.Switch(
                            id: task.id
                        )
                    )
                }
            }
            
            if !model.error.isEmpty {
                Text(model.error)
            }
            
            if model.isLoading {
                ProgressBar()
            }
            
            NavigationLink(
                destination: CreateTaskAssembly.instance(from: context).view,
                tag: Screen.createTask,
                selection: $navigator.screen
            ) {
                EmptyView()
            }.hidden()
            
        }.navigationBarTitle(Screen.todoList.description())
        .navigationBarBackButtonHidden(true)
        .navigationBarItems(
            leading: HStack {
                Button(
                    action: { self.model.acceptAction(action: TodoAction.CreateTask()) },
                    label: { Text("Create") }
                )
                if model.showArchive {
                    Button(
                        action: { self.model.acceptAction(action: TodoAction.Archive()) },
                        label: { Text("Archive") }
                    )
                }
            },
            trailing: Button(
                action: { self.model.acceptAction(action: TodoAction.Load()) },
                label: { Text("Refresh") }
            )
        ).onAppear() {
            self.model.onAppear(store: TodoListAssembly.instance(from: self.context).todoListStore)
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

#if DEBUG

struct TodoListView_Previews: PreviewProvider {
    
    static var modelLoaded: TodoListViewModel {
        get {
            let instance = TodoListViewModel()
            instance.isLoading = false
            instance.tasks = [
                TaskPresentable(id: "1", title: "One", description: "First task", status: TaskStatus.pending),
                TaskPresentable(id: "2", title: "Two", description: "Second task", status: TaskStatus.done)
            ]
            instance.error = ""
            instance.showArchive = true
            return instance
        }
    }
    
    static var modelLoading: TodoListViewModel {
        get {
            let instance = TodoListViewModel()
            instance.isLoading = true
            instance.tasks = []
            instance.error = ""
            instance.showArchive = false
            return instance
        }
    }
    
    static var modelError: TodoListViewModel {
        get {
            let instance = TodoListViewModel()
            instance.isLoading = false
            instance.tasks = []
            instance.error = "Error"
            instance.showArchive = false
            return instance
        }
    }
    
    static var previews: some View {
        Group {
            TodoListView(
                navigator: Navigator(),
                model: modelLoaded,
                context: DIContext()
            ).previewDisplayName("Loaded")
            TodoListView(
                navigator: Navigator(),
                model: modelLoaded,
                context: DIContext()
            ).previewDisplayName("Loaded Dark")
            .environment(\.colorScheme, .dark)
            
            TodoListView(
                navigator: Navigator(),
                model: modelLoading,
                context: DIContext()
            ).previewDisplayName("Loading")
            TodoListView(
                navigator: Navigator(),
                model: modelLoading,
                context: DIContext()
            ).previewDisplayName("Loading Dark")
            .environment(\.colorScheme, .dark)
            
            TodoListView(
                navigator: Navigator(),
                model: modelError,
                context: DIContext()
            ).previewDisplayName("Error")
            TodoListView(
                navigator: Navigator(),
                model: modelError,
                context: DIContext()
            ).previewDisplayName("Error Dark")
            .environment(\.colorScheme, .dark)
        }
    }
}
#endif
