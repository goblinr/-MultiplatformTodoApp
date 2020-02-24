//
//  CreateTaskView.swift
//  TodoApp
//
//  Created by Roman Romanov on 14.01.2020.
//  Copyright © 2020 65apps. All rights reserved.
//

import SwiftUI
import Interaction
import EasyDi

struct CreateTaskView: View {
    
    let model: CreateTaskViewModel
    
    @SwiftUI.State private var title = ""
    @SwiftUI.State private var description = ""
    
    var body: some View {
        VStack {
            Spacer()
            
            if model.result == nil {
                TextField("Title", text: $title, onEditingChanged: { value in
                    self.model.onTitleChanged(self.title)
                }).padding()
                
                TextField("Description", text: $description, onEditingChanged: { value in
                    self.model.onDescriptionChanged(self.description)
                }).padding()
            } else {
                Text(model.result?.title ?? "")
                    .bold()
                    .padding()
                Text(model.result?.component3() ?? "")
                    .padding()
            }
            
            if model.isLoading {
                Text("Loading...")
            }
            
            if !model.error.isEmpty {
                Text(model.error)
                    .foregroundColor(Color.red)
            }
            
            Spacer()
            
            if model.result == nil {
                Button(
                    action: model.onSubmitClicked,
                    label: { Text("Create") }
                    ).padding()
                    .disabled(model.isLoading)
            }
    }.navigationBarTitle(Screen.createTask.description())
            .navigationBarItems(leading: Button(
                action: model.onBack,
                label: { Text("< Back") }
            ), trailing: EmptyView()
        )
    }
}

#if DEBUG
struct CreateTaskView_Previews: PreviewProvider {
    
    static let listState = CreateTaskViewModel()
    
    static let loadingState = CreateTaskViewModel(
        isLoading: true
    )
    
    static let errorState = CreateTaskViewModel(
        error: "Error"
    )
    
    static let resultState = CreateTaskViewModel(
        result: Task(
            id: "id",
            title: "title",
            description: "description",
            status: TaskStatus.pending
        )
    )
    
    static var data: Array<(key: String, value: CreateTaskViewModel)> {
        return [
            (key: "Default", value: listState),
            (key: "Loading", value: loadingState),
            (key: "Error", value: errorState),
            (key: "Result", value: resultState)
        ]
    }
    
    static var previews: some View {
        Group {
            ForEach(data, id: \.key) { entry in
                Group {
                    CreateTaskView(
                        model: entry.value
                    ).previewDisplayName(entry.key)
                    CreateTaskView(
                        model: entry.value
                    ).previewDisplayName("\(entry.key) Dark")
                    .darkModeFix()
                }
            }
        }
    }
}
#endif
