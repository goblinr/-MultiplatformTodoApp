//
//  TodoListView.swift
//  TodoApp
//
//  Created by Roman Romanov on 11.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction
#if DEBUG
import EasyDi
#endif

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
    @ObservedObject var model: TodoListModel
    
    private let createTaskAssembly = CreateTaskAssembly.instance()
    private let todoListAssembly = TodoListAssembly.instance()
    
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
                destination: createTaskAssembly.view,
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
            self.model.onAppear(store: self.todoListAssembly.todoListStore)
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

#if DEBUG

struct TodoListView_Previews: PreviewProvider {
    
    static var modelLoaded: TodoListModel {
        get {
            let instance = TodoListModel()
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
    
    static var modelLoading: TodoListModel {
        get {
            let instance = TodoListModel()
            instance.isLoading = true
            instance.tasks = []
            instance.error = ""
            instance.showArchive = false
            return instance
        }
    }
    
    static var modelError: TodoListModel {
        get {
            let instance = TodoListModel()
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
                model: modelLoaded
                ).previewDisplayName("Loaded")
            TodoListView(
                navigator: Navigator(),
                model: modelLoaded
            ).previewDisplayName("Loaded Dark")
            .environment(\.colorScheme, .dark)
            
            TodoListView(
                navigator: Navigator(),
                model: modelLoading
            ).previewDisplayName("Loading")
            TodoListView(
                navigator: Navigator(),
                model: modelLoading
            ).previewDisplayName("Loading Dark")
            .environment(\.colorScheme, .dark)
            
            TodoListView(
                navigator: Navigator(),
                model: modelError
            ).previewDisplayName("Error")
            TodoListView(
                navigator: Navigator(),
                model: modelError
            ).previewDisplayName("Error Dark")
            .environment(\.colorScheme, .dark)
        }
    }
}
#endif
