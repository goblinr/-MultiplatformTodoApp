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
    
    let model: TodoListViewModel
    
    var body: some View {
        ZStack {
            List(model.tasks) { task in
                TaskView(task: task).onTapGesture {
                    self.model.onSwitchTask(task.id)
                }
            }
            
            if !model.error.isEmpty {
                Text(model.error)
            }
            
            if model.isLoading {
                ProgressBar()
            }
        }.navigationBarTitle(Screen.todoList.description())
        .navigationBarItems(
            leading: EmptyView(),
            trailing: HStack {
                Button(
                    action: model.onCreateTask,
                    label: { Text("Create") }
                )
                if model.showArchive {
                    Button(
                        action: model.onArchiveTasks,
                        label: { Text("Archive") }
                    )
                }
                Button(
                    action: model.onRefresh,
                    label: { Text("Refresh") }
                )
            }
        )
    }
}

#if DEBUG
struct TodoListView_Previews: PreviewProvider {
    
    static let modelLoaded = TodoListViewModel(
        tasks: [
            TaskPresentable(id: "1", title: "One", description: "First task", status: TaskStatus.pending),
            TaskPresentable(id: "2", title: "Two", description: "Second task", status: TaskStatus.done)
        ],
        showArchive: true
    )
    
    static let modelLoading = TodoListViewModel(
        isLoading: true
    )
    
    static let modelError = TodoListViewModel(
        error: "Error"
    )
    
    static let modelWithContentError = TodoListViewModel(
        error: "Error",
        tasks: [
            TaskPresentable(id: "1", title: "One", description: "First task", status: TaskStatus.pending),
            TaskPresentable(id: "2", title: "Two", description: "Second task", status: TaskStatus.done)
        ],
        showArchive: true
    )
    
    static var data: Array<(key: String, value: TodoListViewModel)> {
        return [
            (key: "Loaded", value: modelLoaded),
            (key: "Loading", value: modelLoading),
            (key: "Error", value: modelError),
            (key: "Error with content", value: modelWithContentError)
        ]
    }
    
    static var previews: some View {
        Group {
            ForEach(data, id: \.key) { entry in
                Group {
                    TodoListView(
                        model: entry.value
                    ).previewDisplayName(entry.key)
                    TodoListView(
                        model: entry.value
                    ).previewDisplayName("\(entry.key) Dark")
                    .environment(\.colorScheme, .dark)
                }
            }
        }
    }
}
#endif
