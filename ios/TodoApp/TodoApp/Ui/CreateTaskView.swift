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
    
    @ObservedObject var model: CreateTaskViewModel
    var context: DIContext
    
    @SwiftUI.State private var title = ""
    @SwiftUI.State private var description = ""
    
    var body: some View {
        VStack {
            Spacer()
            
            if model.result == nil {
                TextField("Title", text: $title, onEditingChanged: { value in
                    self.model.acceptAction(
                        action: CreateAction.UpdateField(
                            value: self.title,
                            type: FieldType.title
                        )
                    )
                }).padding()
                
                TextField("Description", text: $description, onEditingChanged: { value in
                    self.model.acceptAction(
                        action: CreateAction.UpdateField(
                            value: self.description,
                            type: FieldType.description_
                        )
                    )
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
                    action: { self.model.acceptAction(action: CreateAction.Create()) },
                    label: { Text("Create") }
                    ).padding()
                    .disabled(model.isLoading)
            }
    }.navigationBarTitle(Screen.createTask.description()).navigationBarBackButtonHidden(true)
        .navigationBarItems(leading: Button(
            action: { self.model.navigateBack() },
            label: { Text("< Back") }
        )).onAppear() {
            self.model.onAppear(store: CreateTaskAssembly.instance(from: self.context).createTaskStore)
        }.onDisappear() {
            self.model.onDisappear()
        }
    }
}

#if DEBUG
struct CreateTaskView_Previews: PreviewProvider {
    
    static var defaultState: CreateTaskViewModel {
        get {
            let model = CreateTaskViewModel()
            return model
        }
    }
    
    static var loadingState: CreateTaskViewModel {
        get {
            let model = CreateTaskViewModel()
            model.isLoading = true
            return model
        }
    }
    
    static var errorState: CreateTaskViewModel {
        get {
            let model = CreateTaskViewModel()
            model.error = "Error"
            return model
        }
    }
    
    static var resultState: CreateTaskViewModel {
        get {
            let model = CreateTaskViewModel()
            model.result = Task(
                id: "id",
                title: "title",
                description: "description",
                status: TaskStatus.pending
            )
            return model
        }
    }
    
    static var previews: some View {
        Group {
            CreateTaskView(
                model: defaultState,
                context: DIContext()
            ).previewDisplayName("Default")
            
            CreateTaskView(
                model: loadingState,
                context: DIContext()
            ).previewDisplayName("Loading")
            
            CreateTaskView(
                model: errorState,
                context: DIContext()
            ).previewDisplayName("Error")
            
            CreateTaskView(
                model: resultState,
                context: DIContext()
            ).previewDisplayName("Result")
        }
    }
}
#endif
