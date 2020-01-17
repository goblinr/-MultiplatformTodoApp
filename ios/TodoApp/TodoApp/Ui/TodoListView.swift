//
//  TodoListView.swift
//  TodoApp
//
//  Created by Roman Romanov on 11.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction

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
    
    let mainStore: () -> MainStore
    @ObservedObject var router: SwiftUIRouter
    @ObservedObject private var model = TodoListModel()
    
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
            
            NavigationLink(destination: CreateTaskView(mainStore: mainStore), tag: Screen.createTask, selection: $router.screen) {
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
        )
        .onAppear() {
            self.model.onAppear(mainStore: self.mainStore())
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

#if DEBUG
struct TodoListView_Previews: PreviewProvider {
    
    static var previews: some View {
        TodoListView(mainStore: { MainStore(
            reducer: MainReducer(),
            middleware: [],
            initialState: MainStateProvider(restoredState: nil),
            stateSubjectProvider: DefaultStateSubjectProvider(),
            restoredState: nil)},
            router: SwiftUIRouter()
            ).environment(\.taskRepository, MockTaskRepository())
            .environment(\.schedulers, IosSchedulers())
    }
}
#endif
